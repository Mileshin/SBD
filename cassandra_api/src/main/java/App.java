import CRUD.AuthorsCRUD;
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
        CassandraDDL.createTableAuthors(test.getSession(), "authors");


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse("2018-04-03 21:25:10");
        Date date1 = format.parse("2018-04-03 10:05:10");

        SpacesCRUD spacesCRUD = new SpacesCRUD(test.getSession());
        spacesCRUD.insertSpaces(new Spaces("fff","trtt",date,"json","parent"));
        System.out.println(spacesCRUD.findSpaces("fff",date).getDescription());
        spacesCRUD.updateSpaces(new Spaces("fff","tRtt",date1,"json","parent"));
        System.out.println(spacesCRUD.findSpaces("fff",date1).getDescription());
      //  spacesCRUD.deleteSpaces("fff",1);
       // System.out.println(spacesCRUD.findSpaces("fff",1).getDescription());

        System.out.println(spacesCRUD.getAll());
        System.out.println(spacesCRUD.getAllByName("fff"));
        List<Spaces> tt = spacesCRUD.getLastRowsByName("fff",2);
        tt = spacesCRUD.getAllBetweenTime("fff",date1,date);
        spacesCRUD.insertNow("test","des","diff", "parent");


        PagesCRUD pagesCRUD = new PagesCRUD(test.getSession());
        pagesCRUD.insertPages(new Pages("ttt","content",date,"json"));
        System.out.println(pagesCRUD.findPages("ttt",date).getContent());
        pagesCRUD.insertPages(new Pages("ttt","CONTENT",date1,"json"));
        System.out.println(pagesCRUD.findPages("ttt",date1).getContent());
       // pagesCRUD.insertNow("test","content","test_json");

        AuthorsCRUD authorsCRUD = new AuthorsCRUD(test.getSession());
        authorsCRUD.insertNow("user_login","spaces","diff_json");

        test.close();
    }

}
