package controlers.datastax;

import com.datastax.driver.core.Session;

/**
 * Created by Andrey on 06.04.2018.
 */
public  class CassandraDDL {

    public static void createKeyspace(Session session, String keyspaceName,
                               String replicationStrategy, int replicationFactor) {
        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(keyspaceName).append(" WITH replication = {")
                        .append("'class':'").append(replicationStrategy)
                        .append("','replication_factor':").append(replicationFactor)
                        .append("};");

        String query = sb.toString();
        session.execute(query);
    }

    public static void useKeyspace(Session session, String keyspaceName){
        session.execute("USE " + keyspaceName);
    }

    // Create Tables
    public static void createTablePages(Session session, String NameTable) {
        String query = "CREATE TABLE IF NOT EXISTS " +
                NameTable +
                "( name text,\n" +
                "  content text,\n" +
                "  modified timestamp,\n" +
                "  diffs_json text,\n" +
                "  revision int,\n" +
                "  PRIMARY KEY(name, revision)\n" +
                ") WITH CLUSTERING ORDER BY (revision ASC)";
        session.execute(query);
    }

    public static void createTableSpaces(Session session, String NameTable) {
        String query = "CREATE TABLE IF NOT EXISTS " +
                NameTable +
                "(name text,\n" +
                "  description text,\n" +
                "  modified timestamp,\n" +
                "  diffs_json text,\n" +
                "  parent text,\n" +
                "  revision int,\n" +
                "  PRIMARY KEY(name, revision)\n" +
                ") WITH CLUSTERING ORDER BY (revision ASC);";
        session.execute(query);
        session.execute("create index IF NOT EXISTS " + NameTable +"_modified on " + NameTable + "(modified);");
    }

   /* public static void createTableAuthors(Session session, String NameTable) {
        String query = "CREATE TABLE IF NOT EXISTS " +
                NameTable +
                "(login text, " +
                "event_time timestamp, " +
                "name text, " +
                "status text, " +
                "primary key (login,event_time));";
        session.execute(query);
    }

    public static void createTableAttachments (Session session, String NameTable) {
        String query = "CREATE TABLE IF NOT EXISTS " +
                NameTable +
                "(filename text, " +
                "event_time timestamp, " +
                "diff_json text, " +
                "primary key (filename,event_time));";
        session.execute(query);
    }*/

    public static void dropTable(Session session, String NameTable){
        String query = "DROP TABLE IF EXISTS " + NameTable;
        session.execute(query);
    }

    public static void dropKeyspace(Session session, String NameKeyspace){
        String query = "DROP KEYSPACE  IF EXISTS " + NameKeyspace;
        session.execute(query);
    }
}
