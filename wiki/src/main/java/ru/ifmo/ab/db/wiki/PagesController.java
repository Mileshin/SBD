package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.factory.IProduceFactory;
import astoria.dummymaker.factory.impl.GenProduceFactory;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.andrey.db.wiki.cassandra.CRUD.AuthorsCRUD;
import ru.ifmo.andrey.db.wiki.cassandra.CRUD.PagesCRUD;
import ru.ifmo.andrey.db.wiki.cassandra.controllers.datastax.CassandraConnector;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Pages;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AttachmentCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AuthorCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.PageCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.SpaceCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.Neo4jSessionFactory;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PagesController {
    @Autowired
    private PagesRepository pagesRepository;

    private CassandraConnector cassandraConnector;
    private PagesCRUD pagesLog;
    private AuthorsCRUD authorsLog;

    private PageCRUD pagesRels;
    private AuthorCRUD authorRels;
    private SpaceCRUD spacesRels;
    private AttachmentCRUD attachmentRels;

    @PostConstruct
    private void initialize() {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect("159.65.199.183", 9042);
        pagesLog = new PagesCRUD(cassandraConnector.getSession());
        authorsLog = new AuthorsCRUD(cassandraConnector.getSession());

        pagesRels = new PageCRUD();
        authorRels = new AuthorCRUD();
        spacesRels = new SpaceCRUD();
        attachmentRels = new AttachmentCRUD();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pages/generate")
    public void genPages() {
        IProduceFactory factory = new GenProduceFactory();
        List<Page> list = factory.produce(Page.class, 1000000);
        Iterator<Page> iter = list.iterator();
        for (char a = 'a'; a < 'z'; a++) {
            for (char b = 'a'; b < 'z'; b++) {
                StringBuilder sp = new StringBuilder();
                sp.append(a).append(b);
                for (char c = 'a'; c < 'z'; c++) {
                    StringBuilder refp = new StringBuilder();
                    refp.append(a).append(b).append(c).append('a').append('f');
                    for (char d = 'a'; d < 'z'; d++) {
                        for (char z = 'f'; z < 'h'; z++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(a).append(b).append(c).append(d);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(a).append(b).append(c).append(d).append(z);
                            if (iter.hasNext()) {
                                Page n = iter.next();
                                n.setName(sb2.toString());
                                n.setCreator(sb.toString());
                                n.setAttachments(new ArrayList<>());
                                n.getAttachments().add(sb.toString());
                                n.setSpaces(new ArrayList<>());
                                n.getSpaces().add(sp.toString());
                                if (d != 'a') {
                                    n.setRefersTo(new ArrayList<>());
                                    n.getRefersTo().add(refp.toString());
                                }
                                this.newPage(n);
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pages/{name}")
    public Page page(@PathVariable String name) {
        return pagesRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Page '" + name + "' does not exist"));
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/pages/{name}/referred-by")
    public List<String> pagesReferredBy(@PathVariable String name,
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

        var ref = Neo4jSessionFactory.getLinkedPages(name).iterator();
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

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/pages/{name}/log")
    public List<Pages> pageLog(@PathVariable String name,
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

        var log = pagesLog.getAllByName(name);
        for (int i = 0; i < pageVal * countVal; ++i) {
            if (log.isExhausted()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            }
            log.one();
        }

        List<Pages> result = new ArrayList<>();
        for (int i = 0; i < countVal; ++i) {
            if (log.isExhausted() && result.isEmpty()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            } else if (log.isExhausted()) { break; }
            result.add(log.one());
        }

        return result;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/pages/{name}/diff")
    public List<Pages> pageDiff(@PathVariable String name,
                                @RequestParam(name = "fromTime", defaultValue = "") String fromTime,
                                @RequestParam(name = "toTime", defaultValue = "") String toTime) {
        if (fromTime.isEmpty()) {
            fromTime = LocalDateTime.now().minusDays(1).toString();
        }

        if (toTime.isEmpty()) {
            toTime = LocalDateTime.now().toString();
        }

        Date fromVal = Utils.dateFromRq(fromTime);
        Date toVal = Utils.dateFromRq(toTime);
        if (fromVal.after(toVal)) {
            throw new BadRequestException("'from' date must be earlier than 'to' date");
        }

        var log = pagesLog.getAllBetweenTime(name, fromVal, toVal);
        if (log.isExhausted()) {
            throw new NotFoundException("Nothing found");
        }

        return log.all();
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.POST, value = "/pages")
    public boolean newPage(@RequestBody Page newPage) {
        if (pagesRepository.findByName(newPage.getName()).isPresent()) {
            return false;
        } else {
            var newPageRel = new ru.ifmo.astoria.db.wiki.neo4j.entity.Page(newPage.getName());

            if (newPage.getAttachments() != null) {
                newPage.getAttachments().forEach(attachment -> {
                    Long attachmentId = attachmentRels.getIdByName(attachment);
                    if (attachmentId == null) {
                        throw new BadRequestException("Attachment '" + attachment + "' does not exist");
                    }

                    var attachmentRel = attachmentRels.find(attachmentId);
                    if (attachmentRel == null) {
                        throw new BadRequestException("Attachment '" + attachment + "' does not exist");
                    }

                    newPageRel.addAttachment(attachmentRel);
                });
            }

            if (newPage.getSpaces() != null) {
                newPage.getSpaces().forEach(space -> {
                    Long spaceId = spacesRels.getIdByName(space);

                    if (spaceId == null) {
                        throw new BadRequestException("Space '" + space + "' does not exist");
                    }

                    var spaceRel = spacesRels.find(spaceId);
                    if (spaceRel == null) {
                        throw new BadRequestException("Space '" + space + "' does not exist");
                    }

                    newPageRel.addSpace(spaceRel);
                });
            }
            if (newPage.getRefersTo() != null) {
                newPage.getRefersTo().forEach(refersTo -> {
                    Long pageId = pagesRels.getIdByName(refersTo);
                    if (pageId == null) {
                        throw new BadRequestException("Page '" + refersTo + "' does not exist");
                    }

                    var pageRel = pagesRels.find(pageId);
                    if (pageRel == null) {
                        throw new BadRequestException("Page '" + refersTo + "' does not exist");
                    }

                    newPageRel.addPage(pageRel);
                });
            }

            if (newPage.getCreator() != null) {
                Long creatorId = authorRels.getIdByName(newPage.getCreator());
                if (creatorId == null) {
                    throw new BadRequestException("Author '" + newPage.getCreator() + "' does not exist");
                }

                var creatorRel = authorRels.find(creatorId);
                if (creatorRel == null) {
                    throw new BadRequestException("Author '" + newPage.getCreator() + "' does not exist");
                }

                creatorRel.setPageCreated(newPageRel);
                authorRels.createOrUpdate(creatorRel);
            }

            if (newPage.getComments() != null) {
                newPage.getComments().forEach(comment -> {
                    if (comment.getAuthor() == null) {
                        throw new BadRequestException("Comment without author is invalid");
                    }

                    Long commenterId = authorRels.getIdByName(comment.getAuthor());
                    if (commenterId == null) {
                        throw new BadRequestException("Author '" + comment.getAuthor() + "' does not exist");
                    }

                    var commenterRel = authorRels.find(commenterId);
                    if (commenterRel == null) {
                        throw new BadRequestException("Author '" + comment.getAuthor() + "' does not exist");
                    }

                    commenterRel.setPageCommented(newPageRel);
                    authorRels.createOrUpdate(commenterRel);
                });
            }

            authorsLog.insertNow(newPage.getCreator(), "pages", "created " + newPage.getName());
            pagesLog.insertNow(newPage.getName(), newPage.toString(), newPage.getCreator());
            pagesRels.createOrUpdate(newPageRel);
            pagesRepository.insert(newPage);
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pages/{name}/comments")
    public boolean comment(@PathVariable String name, @RequestBody Comment comment) {
        Optional<Page> page = pagesRepository.findByName(name);
        if (page.isPresent()) {
            Long pageRelId = pagesRels.getIdByName(name);
            if (pageRelId == null) {
                throw new RuntimeException("Data between MongoDB and Neo4j are out of sync");
            }

            var pageRel = pagesRels.find(pageRelId);
            if (pageRel == null) {
                throw new RuntimeException("Data between MongoDB and Neo4j are out of sync");
            }

            if (comment.getAuthor() == null) {
                throw new BadRequestException("Comment without author is invalid");
            }

            Long authorRelId = authorRels.getIdByName(comment.getAuthor());
            if (authorRelId == null) {
                throw new RuntimeException("Data between MongoDB and Neo4j are out of sync");
            }

            var authorRel = authorRels.find(authorRelId);
            if (authorRel == null) {
                throw new RuntimeException("Data between MongoDB and Neo4j are out of sync");
            }

            if (page.get().getComments() == null) {
                page.get().setComments(new ArrayList<>());
            }

            page.get().getComments().add(comment);

            authorRel.setPageCommented(pageRel);
            authorRels.createOrUpdate(authorRel);

            authorsLog.insertNow(comment.getAuthor(), "pages", "commented " + name);
            pagesLog.insertNow(name, page.get().toString(), comment.getAuthor());
        } else {
            throw new NotFoundException("Page '" + name + "' does not exist");
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/pages/{name}/comments/{id}")
    public boolean deleteComment(@PathVariable String name, @PathVariable String id,
                                 @RequestParam(name = "author") String author) {
        int idVal = Utils.intFromRq(id);

        Optional<Page> page = pagesRepository.findByName(name);
        if (page.isPresent() && page.get().getComments() != null) {
            try {
                page.get().getComments().remove(idVal);

                // Relation is not deleted
                authorsLog.insertNow(author, "pages", "deleted comment " + id + " @ " + name);
                pagesLog.insertNow(name, page.get().toString(), author);
            } catch (IndexOutOfBoundsException ex) {
                throw new NotFoundException("Comment with id '" + id + "' does not exist");
            }
        } else {
            throw new NotFoundException("Page '" + name + "' does not exist");
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/pages/{name}")
    public boolean deletePage(@PathVariable String name,
                              @RequestParam(name = "author") String author) {
        pagesRepository.delete(pagesRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Page '" + name + "' does not exist")));
        pagesLog.insertNow(name, "<deleted>", author);
        authorsLog.insertNow(author, "pages", "deleted " + name);

        Long relId = pagesRels.getIdByName(name);
        if (relId != null) {
            pagesRels.delete(relId);
        }

        return true;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.PUT, value = "/pages")
    public boolean modifyPage(@RequestBody Page modifiedPage,
                              @RequestParam(name = "author") String author) {
        Optional<Page> currentPage = pagesRepository.findByName(modifiedPage.getName());
        if (currentPage.isPresent()) {
            modifiedPage.setId(currentPage.get().getId());

            Long modifiedPageRelId = pagesRels.getIdByName(modifiedPage.getName());
            if (modifiedPageRelId == null) {
                throw new BadRequestException("Page '" + modifiedPage.getName() + "' does not exist");
            }

            var modifiedPageRel = pagesRels.find(modifiedPageRelId);
            if (modifiedPageRel == null) {
                throw new BadRequestException("Page '" + modifiedPage.getName() + "' does not exist");
            }

            if (modifiedPage.getAttachments() != null) {
                modifiedPage.getAttachments().forEach(attachment -> {
                    Long attachmentId = attachmentRels.getIdByName(attachment);
                    if (attachmentId == null) {
                        throw new BadRequestException("Attachment '" + attachment + "' does not exist");
                    }

                    var attachmentRel = attachmentRels.find(attachmentId);
                    if (attachmentRel == null) {
                        throw new BadRequestException("Attachment '" + attachment + "' does not exist");
                    }

                    modifiedPageRel.addAttachment(attachmentRel);
                });
            }

            if (modifiedPage.getSpaces() != null) {
                modifiedPage.getSpaces().forEach(space -> {
                    Long spaceId = spacesRels.getIdByName(space);
                    if (spaceId == null) {
                        throw new BadRequestException("Space '" + space + "' does not exist");
                    }

                    var spaceRel = spacesRels.find(spaceId);
                    if (spaceRel == null) {
                        throw new BadRequestException("Space '" + space + "' does not exist");
                    }

                    modifiedPageRel.addSpace(spaceRel);
                });
            }

            if (modifiedPage.getRefersTo() != null) {
                modifiedPage.getRefersTo().forEach(refersTo -> {
                    Long pageId = pagesRels.getIdByName(refersTo);
                    if (pageId == null) {
                        throw new BadRequestException("Page '" + refersTo + "' does not exist");
                    }

                    var pageRel = pagesRels.find(pageId);
                    if (pageRel == null) {
                        throw new BadRequestException("Page '" + refersTo + "' does not exist");
                    }

                    modifiedPageRel.addPage(pageRel);
                });
            }

            if (modifiedPage.getComments() != null) {
                modifiedPage.getComments().forEach(comment -> {
                    if (comment.getAuthor() == null) {
                        throw new BadRequestException("Comment without author is invalid");
                    }

                    Long commenterId = authorRels.getIdByName(comment.getAuthor());
                    if (commenterId == null) {
                        throw new BadRequestException("Author '" + comment.getAuthor() + "' does not exist");
                    }

                    var commenterRel = authorRels.find(commenterId);
                    if (commenterRel == null) {
                        throw new BadRequestException("Author '" + comment.getAuthor() + "' does not exist");
                    }

                    commenterRel.setPageCommented(modifiedPageRel);
                    authorRels.createOrUpdate(commenterRel);
                });
            }

            authorsLog.insertNow(author, "pages", "updated " + modifiedPage.getName());
            pagesLog.insertNow(modifiedPage.getName(), modifiedPage.toString(), author);
            pagesRels.createOrUpdate(modifiedPageRel);
            pagesRepository.save(modifiedPage);
        } else {
            return false;
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/pages")
    public List<Page> pages(@RequestParam(name = "page", defaultValue = "0") String page,
                            @RequestParam(name = "count", defaultValue = "10") String count,
                            @RequestParam(name = "text", required = false) String text) {
        int pageVal = Utils.intFromRq(page);
        if (pageVal < 0) {
            throw new BadRequestException("Page index must not be less than zero");
        }

        int countVal = Utils.intFromRq(count);
        if (countVal < 1) {
            throw new BadRequestException("Page index must not be less than one");
        }

        org.springframework.data.domain.Page<Page> result;
        if (text != null) {
            result = pagesRepository.textSearch(text, PageRequest.of(pageVal, countVal));
        } else {
            result = pagesRepository.findAll(PageRequest.of(pageVal, countVal));
        }
        if (!result.hasContent()) {
            throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
        }

        return result.getContent();
    }
}
