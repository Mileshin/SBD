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
import ru.ifmo.andrey.db.wiki.cassandra.entity.Authors;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Spaces;
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
public class AuthorsController {
    @Autowired
    private AuthorsRepository authorsRepository;

    private CassandraConnector cassandraConnector;
    private AuthorsCRUD authorsLog;

    private AuthorCRUD authorRels;

    @PostConstruct
    private void initialize() {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect("159.65.199.183", 9042);
        authorsLog = new AuthorsCRUD(cassandraConnector.getSession());

        authorRels = new AuthorCRUD();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authors/generate")
    public void genAuthors() {
        IProduceFactory factory = new GenProduceFactory();
        List<Author> list = factory.produce(Author.class, 500000);
        Iterator<Author> iter = list.iterator();
        for (char a = 'a'; a < 'z'; a++) {
            for (char b = 'a'; b < 'z'; b++) {
                for (char c = 'a'; c < 'z'; c++) {
                    for (char d = 'a'; d < 'z'; d++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(a).append(b).append(c).append(d);
                        if (iter.hasNext()) {
                            Author n = iter.next();
                            n.setLogin(sb.toString());
                            this.newAuthor(n, sb.toString());
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/authors/{name}/authored-pages")
    public List<String> authoredPages(@PathVariable String name,
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

        var ref = Neo4jSessionFactory.getAuthorsPagesCreated(name).iterator();
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
    @RequestMapping(method = RequestMethod.GET, value = "/authors/{name}/commented-pages")
    public List<String> commentedPages(@PathVariable String name,
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

        var ref = Neo4jSessionFactory.getAuthorsPagesCommented(name).iterator();
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
    @RequestMapping(method = RequestMethod.GET, value = "/authors/{name}/attachments")
    public List<String> uploads(@PathVariable String name,
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

        var ref = Neo4jSessionFactory.getAuthorsUploads(name).iterator();
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
    @RequestMapping(method = RequestMethod.GET, value = "/authors/{login}/log")
    public List<Authors> authorsLog(@PathVariable String login,
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

        var log = authorsLog.getAllByLogin(login);
        for (int i = 0; i < pageVal * countVal; ++i) {
            if (log.isExhausted()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            }
            log.one();
        }

        List<Authors> result = new ArrayList<>();
        for (int i = 0; i < countVal; ++i) {
            if (log.isExhausted() && result.isEmpty()) {
                throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
            } else if (log.isExhausted()) { break; }
            result.add(log.one());
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authors/{login}")
    public Author author(@PathVariable String login) {
        return authorsRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException("Author '" + login + "' does not exist"));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authors")
    public boolean newAuthor(@RequestBody Author newAuthor,
                             @RequestParam(name = "author") String author) {
        if (authorsRepository.findByLogin(newAuthor.getLogin()).isPresent()) {
            return false;
        } else {
            authorsLog.insertNow(author, "authors", "created " + newAuthor.getLogin());
            authorRels.createOrUpdate(new ru.ifmo.astoria.db.wiki.neo4j.entity.Author(newAuthor.getLogin()));
            authorsRepository.insert(newAuthor);
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/authors/{login}")
    public boolean deleteAuthor(@PathVariable String login,
                                @RequestParam(name = "author") String author) {
        authorsRepository.delete(authorsRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException("Author '" + login + "' does not exist")));
        authorsLog.insertNow(author, "authors", "deleted " + login);

        Long relId = authorRels.getIdByName(login);
        if (relId != null) {
            authorRels.delete(relId);
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/authors")
    public boolean modifyAuthor(@RequestBody Author modifiedAuthor,
                                @RequestParam(name = "author") String author) {
        Optional<Author> currentAuthor = authorsRepository.findByLogin(modifiedAuthor.getLogin());
        if (currentAuthor.isPresent()) {
            modifiedAuthor.setId(currentAuthor.get().getId());
            authorsLog.insertNow(author, "authors",
                    "updated " + modifiedAuthor.getName() + ": " + modifiedAuthor.toString());
            authorsRepository.save(modifiedAuthor);
        } else {
            return false;
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(method = RequestMethod.GET, value = "/authors")
    public List<Author> authors(@RequestParam(name = "page", defaultValue = "0") String page,
                                        @RequestParam(name = "count", defaultValue = "10") String count) {
        int pageVal = Utils.intFromRq(page);
        if (pageVal < 0) {
            throw new BadRequestException("Page index must not be less than zero");
        }

        int countVal = Utils.intFromRq(count);
        if (countVal < 1) {
            throw new BadRequestException("Page index must not be less than one");
        }

        var result = authorsRepository.findAll(PageRequest.of(pageVal, countVal));
        if (!result.hasContent()) {
            throw new NotFoundException("Nothing found on page " + page + " (page size: " + count + ")");
        }

        return result.getContent();
    }
}