import CRUD.PagesCRUD;
import CRUD.SpacesCRUD;
import controlers.datastax.CassandraConnector;
import controlers.datastax.CassandraDDL;
import entity.Pages;
import entity.Spaces;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrey on 02.04.2018.
 */
public class App {
    public static void main(String[] args) throws ParseException {
        // connect
        CassandraConnector test = new CassandraConnector();
        test.connect("localhost",9042);

        // choose keyspace
        CassandraDDL.createKeyspace(test.getSession(),"testkeyspace","SimpleStrategy",3);
        CassandraDDL.useKeyspace(test.getSession(),"testkeyspace");
        CassandraDDL.createTableSpaces(test.getSession(),"spaces");
        CassandraDDL.createTablePages(test.getSession(),"pages");


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse("2018-04-03 20:25:10");
        Date date1 = format.parse("2018-04-03 20:05:10");

        SpacesCRUD spacesCRUD = new SpacesCRUD(test.getSession());
        spacesCRUD.insertSpaces(new Spaces(6,"fff","trtt",date,"json","parent"));
        System.out.println(spacesCRUD.findSpaces("fff",1).getDescription());
        spacesCRUD.updateSpaces(new Spaces(7,"fff","tRtt",date,"json","parent"));
        System.out.println(spacesCRUD.findSpaces("fff",1).getDescription());
      //  spacesCRUD.deleteSpaces("fff",1);
       // System.out.println(spacesCRUD.findSpaces("fff",1).getDescription());

        System.out.println(spacesCRUD.getAll());
        System.out.println(spacesCRUD.getAllByName("fff"));
        List<Spaces> tt = spacesCRUD.getAllBetweenRevision("fff",2,4);
        tt = spacesCRUD.getLastRevisionsByName("fff",2);
       // tt = spacesCRUD.getAllBetweenTime("fff",date1,date);


        PagesCRUD pagesCRUD = new PagesCRUD(test.getSession());
        pagesCRUD.insertPages(new Pages(1,"ttt","content",date,"json"));
        System.out.println(pagesCRUD.findPages("ttt",1).getContent());
        pagesCRUD.insertPages(new Pages(1,"ttt","CONTENT",date,"json"));
        System.out.println(pagesCRUD.findPages("ttt",1).getContent());
        pagesCRUD.deletePages("fff",1);




       /* // create tables
        test.createTablePages("pages");
        test.createTableSpaces("spaces");
        test.createTableAuthors("authors");
        test.createTableAttachments("attachments");

        // insert
        test.insertIntoPages("pages","test_name","test");
        test.insertIntoSpaces("spaces","test_name","test");
        test.insertIntoAuthors("authors","test_login","test_name","test_status");
        test.insertIntoAttachments("Attachments","test_filename","test");

        // select
        /*List<Row> rows = test.retriveValues("pages");
        if(rows != null) {
            for(Row row : rows) {
                System.out.println(row);
            }
        } else {
            System.out.println("empty");
        }*/

       // System.out.println(test.getRow("pages","name","test_name", "2018-04-03 20:15:09"));


        // drop tables
        /*test.dropTable("pages");
        test.dropTable("spaces");
        test.dropTable("authors");
        test.dropTable("attachments");*/

        test.close();
    }

}
