package ru.ifmo.astoria.db.wiki.neo4j;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import ru.ifmo.astoria.db.wiki.neo4j.entity.*;

import java.util.HashMap;
import java.util.Map;

public class Neo4jSessionFactory {
    private static final Configuration configuration = new Configuration.Builder()
            .uri("bolt://localhost")
            .credentials("neo4j", "12345")
            .connectionLivenessCheckTimeout(1000)
            .build();

    private static final SessionFactory sessionFactory = new SessionFactory(configuration,
            "ru.ifmo.astoria.db.wiki.neo4j.entity");

/*    public static void main(String[] args){
        AuthorCRUD authorCRUD = new AuthorCRUD();
        PageCRUD pageCRUD = new PageCRUD();
        AttachmentCRUD attachmentCRUD = new AttachmentCRUD();
        Random rand = new Random();
        int pageGen, attGen, authorGen, spaceGen, crossedLinks;
        for (Author a: authors) {
            Page page = null;
            Attachment attachment;
            for (int i = 0; i < pages.size() / authors.size(); i++) {
                pageGen = rand.nextInt(30);
                attGen = rand.nextInt(5);
                authorGen = rand.nextInt(10);
                spaceGen = rand.nextInt(10);
                crossedLinks = rand.nextInt(30);
                page = pages.get(pageGen);
                attachment = attachments.get(attGen);
                a.setAttachment(attachment);
                //a.setOwner(page);
                //a.removePageAttachment(attachment);
            }
            authorCRUD.createOrUpdate(a);
            //pageCRUD.createOrUpdate(page);
        }

        Long id = authorCRUD.getIdByName("BARRY");
        Author author = authorCRUD.find(id);
        Long idA = attachmentCRUD.getIdByName("Composite.mkv");
        Attachment attachment = attachmentCRUD.find(idA);
        author.removePageAttachment(attachment);
        authorCRUD.createOrUpdate(author);
        Iterable<Page> iterable = getPageOwners("MILAN");
        SpaceCRUD spaceCRUD = new SpaceCRUD();
        Long id1 = spaceCRUD.getIdByName("Silverlight");
        spaceCRUD.delete(id);
        getInstance().endNeo4jSession();
    } */

    public static Iterable<Page> getLinkedPages(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Page.class,
                    "MATCH (n:Page)-[:LINKED]->(m) WHERE m.name = {param} RETURN n", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Page> getPageInSpace(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Page.class,
                    "MATCH (n:Page)-[:INCLUDED_IN]->(m:Space) m.name={param} RETURN n", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Page> getAuthorsPagesCreated(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Page.class,
                    "MATCH (n:Author)-[:CREATED]->(m:Page) WHERE n.login={param} RETURN m", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Page> getAuthorsPagesCommented(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Page.class,
                    "MATCH (n:Author)-[:COMMENTED]->(m:Page) WHERE n.login={param} RETURN m", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Attachment> getAuthorsUploads(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Attachment.class,
                    "MATCH (n:Author)-[:UPLOADED]->(m:Attachment) WHERE n.login={param} RETURN m", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Page> getPagesIncludesAttachment(String name){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("param", name);
            return sessionFactory.openSession().query(Page.class,
                    "MATCH (n:Page)-[:INCLUDES]->(m:Attachment) WHERE m.name={param} RETURN n", map);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

    public static Neo4jSessionFactory getInstance() {
        return factory;
    }

    private Neo4jSessionFactory() {}

    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }

    public void endNeo4jSession(){
        sessionFactory.close();
    }
}
