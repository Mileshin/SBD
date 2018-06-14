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
import ru.ifmo.andrey.db.wiki.cassandra.entity.Spaces;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AttachmentCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.AuthorCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.PageCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.CRUD.SpaceCRUD;
import ru.ifmo.astoria.db.wiki.neo4j.Neo4jSessionFactory;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class SpacesController {
    @Autowired
    private SpacesRepository spacesRepository;

    private CassandraConnector cassandraConnector;
    private SpacesCRUD spacesLog;
    private AuthorsCRUD authorsLog;

    private PageCRUD pagesRels;
    private AuthorCRUD authorRels;
    private SpaceCRUD spacesRels;
    private AttachmentCRUD attachmentRels;

    @PostConstruct
    private void initialize() {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect("159.65.199.183", 9042);
        spacesLog = new SpacesCRUD(cassandraConnector.getSession());
        authorsLog = new AuthorsCRUD(cassandraConnector.getSession());

        pagesRels = new PageCRUD();
        authorRels = new AuthorCRUD();
        spacesRels = new SpaceCRUD();
        attachmentRels = new AttachmentCRUD();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/spaces/generate")
    public void genSpaces() {
        IProduceFactory factory = new GenProduceFactory();
        List<Space> list = factory.produce(Space.class, 500);
        Iterator<Space> iter = list.iterator();
        for (char a = 'a'; a < 'z'; a++) {
            for (char b = 'a'; b < 'z'; b++) {
                StringBuilder sp = new StringBuilder();
                sp.append(a).append(b);
                if (iter.hasNext()) {
                    Space n = iter.next();
                    n.setName(sp.toString());
                    n.setCreator("aabb");
                    this.newSpace(n);
                } else {
                    return;
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/spaces/{name}")
    public Space space(@PathVariable String name) {
        return spacesRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Space '" + name + "' does not exist"));
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/spaces/{name}/pages")
    public List<String> pagesInSpace(@PathVariable String name,
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

        var ref = Neo4jSessionFactory.getPageInSpace(name).iterator();
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
    @RequestMapping(method = RequestMethod.GET, value = "/spaces/{name}/log")
    public List<Spaces> spacesLog(@PathVariable String name,
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

        var log = spacesLog.getAllByName(name);
        for (int i = 0; i < pageVal * countVal; ++i) {
            if (log.isExhausted()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            }
            log.one();
        }

        List<Spaces> result = new ArrayList<>();
        for (int i = 0; i < countVal; ++i) {
            if (log.isExhausted() && result.isEmpty()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            } else if (log.isExhausted()) { break; }
            result.add(log.one());
        }

        return result;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/spaces/{name}/diff")
    public List<Spaces> pageDiff(@PathVariable String name,
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

        var log = spacesLog.getAllBetweenTime(name, fromVal, toVal);
        if (log.isExhausted()) {
            throw new NotFoundException("Nothing found");
        }

        return log.all();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/spaces")
    public boolean newSpace(@RequestBody Space newSpace) {
        if (spacesRepository.findByName(newSpace.getName()).isPresent()) {
            return false;
        } else {
            authorsLog.insertNow(newSpace.getCreator(), "spaces", "created " + newSpace.getName());
            spacesLog.insertNow(newSpace.getName(), newSpace.toString(), newSpace.getCreator());
            spacesRels.createOrUpdate(new ru.ifmo.astoria.db.wiki.neo4j.entity.Space(newSpace.getName()));
            spacesRepository.insert(newSpace);
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/spaces/{name}")
    public boolean deleteSpace(@PathVariable String name,
                               @RequestParam(name = "author") String author) {
        spacesRepository.delete(spacesRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Space '" + name + "' does not exist")));
        spacesLog.insertNow(name, "<deleted>", author);
        authorsLog.insertNow(author, "spaces", "deleted " + name);

        Long relId = spacesRels.getIdByName(name);
        if (relId != null) {
            spacesRels.delete(relId);
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/spaces")
    public boolean modifySpace(@RequestBody Space modifiedSpace,
                               @RequestParam(name = "author") String author) {
        Optional<Space> currentSpace = spacesRepository.findByName(modifiedSpace.getName());
        if (currentSpace.isPresent()) {
            modifiedSpace.setId(currentSpace.get().getId());
            authorsLog.insertNow(author, "spaces", "updated " + modifiedSpace.getName());
            spacesLog.insertNow(modifiedSpace.getName(), modifiedSpace.toString(), author);
            spacesRepository.save(modifiedSpace);
        } else {
            return false;
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/spaces")
    public List<Space> spaces(@RequestParam(name = "page", defaultValue = "0") String page,
                              @RequestParam(name = "count", defaultValue = "10") String count) {
        int pageVal = Utils.intFromRq(page);
        if (pageVal < 0) {
            throw new BadRequestException("Page index must not be less than zero");
        }

        int countVal = Utils.intFromRq(count);
        if (countVal < 1) {
            throw new BadRequestException("Page index must not be less than one");
        }

        var result = spacesRepository.findAll(PageRequest.of(pageVal, countVal));
        if (!result.hasContent()) {
            throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
        }

        return result.getContent();
    }
}
