package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.factory.IProduceFactory;
import astoria.dummymaker.factory.impl.GenProduceFactory;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.andrey.db.wiki.cassandra.CRUD.AuthorsCRUD;
import ru.ifmo.andrey.db.wiki.cassandra.CRUD.SpacesCRUD;
import ru.ifmo.andrey.db.wiki.cassandra.controllers.datastax.CassandraConnector;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AttachmentCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AuthorCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.PageCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.SpaceCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.Neo4jSessionFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
public class AttachmentsController {
    @Autowired
    private AttachmentsRepository attachmentsRepository;

    private CassandraConnector cassandraConnector;
    private AuthorsCRUD authorsLog;

    private PageCRUD pagesRels;
    private AuthorCRUD authorRels;
    private SpaceCRUD spacesRels;
    private AttachmentCRUD attachmentRels;

    @PostConstruct
    private void initialize() {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect("159.65.199.183", 9042);
        authorsLog = new AuthorsCRUD(cassandraConnector.getSession());

        pagesRels = new PageCRUD();
        authorRels = new AuthorCRUD();
        spacesRels = new SpaceCRUD();
        attachmentRels = new AttachmentCRUD();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/attachments/generate")
    public void genAttachments() {
        IProduceFactory factory = new GenProduceFactory();
        List<Attachment> list = factory.produce(Attachment.class, 500000);
        Iterator<Attachment> iter = list.iterator();
        for (char a = 'a'; a < 'z'; a++) {
            for (char b = 'a'; b < 'z'; b++) {
                for (char c = 'a'; c < 'z'; c++) {
                    for (char d = 'a'; d < 'z'; d++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(a).append(b).append(c).append(d);
                        if (iter.hasNext()) {
                            Attachment n = iter.next();
                            n.setFilename(sb.toString());
                            n.setUploader(sb.toString());
                            this.newAttachment(n);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/attachments/{filename}")
    public Attachment attachment(@PathVariable String filename) {
        return attachmentsRepository.findByFilename(filename).orElseThrow(
                () -> new NotFoundException("Attachment '" + filename + "' does not exist"));
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/attachments/{name}/referred-by")
    public List<String> usedInPages(@PathVariable String name,
                                    @RequestParam(name = "page", defaultValue = "0") String page,
                                    @RequestParam(name = "count", defaultValue = "10") String count) {
        int pageVal = Utils.intFromRq(page);
        if (pageVal < 0) {
            throw new BadRequestException("Page index must not be less than zero");
        }

        int countVal = Utils.intFromRq(count);
        if (countVal < 1) {
            throw new BadRequestException("Page index must not be less than one");
        }

        var ref = Neo4jSessionFactory.getPagesIncludesAttachment(name).iterator();
        for (int i = 0; i < pageVal * countVal; ++i) {
            if (!ref.hasNext()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            }
            ref.next();
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < countVal; ++i) {
            if (!ref.hasNext() && result.isEmpty()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            } else if (!ref.hasNext()) { break; }
            result.add(ref.next().toString());
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/attachments")
    public boolean newAttachment(@RequestBody Attachment newAttachment) {
        if (attachmentsRepository.findByFilename(newAttachment.getFilename()).isPresent()) {
            return false;
        } else {
            var newAttachmentRel = new ru.ifmo.astoria.db.wiki.neo4j.entity.Attachment(newAttachment.getFilename());

            if (newAttachment.getUploader() != null) {
                Long uploaderId = authorRels.getIdByName(newAttachment.getUploader());
                if (uploaderId == null) {
                    throw new BadRequestException("Uploader '" + newAttachment.getUploader() + "' does not exist");
                }

                var uploaderRel = authorRels.find(uploaderId);
                if (uploaderRel == null) {
                    throw new BadRequestException("Uploader '" + newAttachment.getUploader() + "' does not exist");
                }

                uploaderRel.setAttachment(newAttachmentRel);
                authorRels.createOrUpdate(uploaderRel);
            }

            authorsLog.insertNow(newAttachment.getUploader(), "attachments",
                    "uploaded " + newAttachment.getFilename());
            attachmentRels.createOrUpdate(newAttachmentRel);
            attachmentsRepository.insert(newAttachment);
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/attachments/{filename}")
    public boolean deleteAttachment(@PathVariable String filename,
                                    @RequestParam(name = "author") String author) {
        attachmentsRepository.delete(attachmentsRepository.findByFilename(filename).orElseThrow(
                () -> new NotFoundException("Attachment '" + filename + "' does not exist")));
        authorsLog.insertNow(author, "attachments", "deleted " + filename);

        Long relId = attachmentRels.getIdByName(filename);
        if (relId != null) {
            attachmentRels.delete(relId);
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/attachments")
    public boolean modifyAttachment(@RequestBody Attachment modifiedAttachment) {
        Optional<Attachment> currentAttachment = attachmentsRepository.findByFilename(modifiedAttachment.getFilename());
        if (currentAttachment.isPresent()) {
            modifiedAttachment.setId(currentAttachment.get().getId());
            attachmentsRepository.save(modifiedAttachment);
        } else {
            return false;
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/attachments")
    public List<Attachment> attachments(@RequestParam(name = "page", defaultValue = "0") String page,
                            @RequestParam(name = "count", defaultValue = "10") String count) {
        int pageVal = Utils.intFromRq(page);
        if (pageVal < 0) {
            throw new BadRequestException("Page index must not be less than zero");
        }

        int countVal = Utils.intFromRq(count);
        if (countVal < 1) {
            throw new BadRequestException("Page index must not be less than one");
        }

        var result = attachmentsRepository.findAll(PageRequest.of(pageVal, countVal));
        if (!result.hasContent()) {
            throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
        }

        return result.getContent();
    }
}