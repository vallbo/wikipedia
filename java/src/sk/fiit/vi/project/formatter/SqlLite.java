package sk.fiit.vi.project.formatter;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vallbo_work on 07/12/14.
 */
public class SqlLite {

    public static void writeResults(Collection<Cluster> clusters) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "DELETE from CLUSTERS where 1;";
            stmt.executeUpdate(sql);
            c.commit();
            System.out.println("Database empty");
        for(Iterator<Cluster> i = clusters.iterator(); i.hasNext(); ) {
            Cluster cluster = i.next();
            if(cluster.getLabel().contains("Other Topics")) {
                continue;
            }
            for(Iterator<Document> j = cluster.getDocuments().iterator(); j.hasNext(); ) {
                Document item = j.next();
                String selectStatement = "INSERT INTO CLUSTERS (ID, CLUSTER_NAME, IDD,NAME) VALUES (?, ?, ?, ?);";
                PreparedStatement prepStmt = c.prepareStatement(selectStatement);
                prepStmt.setInt(1, cluster.getId());
                prepStmt.setString(2, cluster.getLabel());
                prepStmt.setInt(3, Integer.parseInt(item.getStringId()));
                prepStmt.setString(4, item.getTitle());
                prepStmt.executeUpdate();
            }
        }
            stmt.close();
            c.commit();
            c.close();
            System.out.println("Records created successfully");
        } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
    }

    public static void createDb() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS CLUSTERS; CREATE TABLE CLUSTERS " +
                    "(ID INT NOT NULL," +
                    " CLUSTER_NAME           TEXT    NOT NULL, " +
                    " IDD           INT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void printResults() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:data/test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT IDD, NAME, GROUP_CONCAT(CLUSTER_NAME, ', ') AS CLUSTER_NAMES FROM CLUSTERS GROUP BY IDD;");
            while ( rs.next() ) {
                String  clusterNames = rs.getString("cluster_names");
                int idd  = rs.getInt("idd");
                String  name = rs.getString("name");
                System.out.println( name + " ("+idd+"): "+ clusterNames);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

}
