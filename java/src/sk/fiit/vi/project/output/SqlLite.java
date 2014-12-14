package sk.fiit.vi.project.output;

import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by VB on 07/12/14.
 */
public class SqlLite {

    private static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:data/clusters.db");
        c.setAutoCommit(false);
        System.out.println("Opened database CLUSTERS successfully");
        return c;
    }

    public static void writeResults(Collection<Cluster> clusters) {
        createDb();
        try {
            Connection dbCnt = getDbConnection();
            for (Iterator<Cluster> i = clusters.iterator(); i.hasNext(); ) {
                Cluster cluster = i.next();
                for (Iterator<Document> j = cluster.getDocuments().iterator(); j.hasNext(); ) {
                    Document item = j.next();
                    String selectStatement = "INSERT INTO CLUSTERS (ID, CLUSTER_NAME, IDD,NAME) VALUES (?, ?, ?, ?);";
                    PreparedStatement prepStmt = dbCnt.prepareStatement(selectStatement);
                    prepStmt.setInt(1, cluster.getId());
                    prepStmt.setString(2, cluster.getLabel());
                    prepStmt.setInt(3, Integer.parseInt(item.getStringId()));
                    prepStmt.setString(4, item.getTitle());
                    prepStmt.executeUpdate();
                }
            }
            dbCnt.commit();
            dbCnt.close();
            System.out.println("Records to CLUSTERS inserted successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void createDb() {
        try {
            Connection dbCnt = getDbConnection();
            Statement stmt = null;
            stmt = dbCnt.createStatement();
            String sql = "DROP TABLE IF EXISTS CLUSTERS; CREATE TABLE CLUSTERS " +
                    "(ID INT NOT NULL," +
                    " CLUSTER_NAME           TEXT    NOT NULL, " +
                    " IDD           INT    NOT NULL, " +
                    " NAME            TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            dbCnt.commit();
            dbCnt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table CLUSTERS created successfully");
    }

    public static void printResults() {
        Statement stmt = null;
        try {
            Connection c = getDbConnection();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT IDD, NAME, GROUP_CONCAT(CLUSTER_NAME, ', ') AS CLUSTER_NAMES FROM CLUSTERS GROUP BY IDD ORDER BY NAME;");
            while (rs.next()) {
                String clusterNames = rs.getString("cluster_names");
                int idd = rs.getInt("idd");
                String name = rs.getString("name");
                System.out.println(name + " (" + idd + "): " + clusterNames);
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
