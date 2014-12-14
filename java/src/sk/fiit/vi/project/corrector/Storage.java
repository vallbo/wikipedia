package sk.fiit.vi.project.corrector;

import sk.fiit.vi.project.model.Page;
import sk.fiit.vi.project.model.Record;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by VB on 07/12/14.
 */
public class Storage {

    private static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:data/corrector.db");
        c.setAutoCommit(false);
        System.out.println("Opened database CORRECTOR successfully");
        return c;
    }

    public static void writeData(List<Page> pages) {
        createDb();
        try {
            Connection c = getDbConnection();
            int j = 0;
            for (Iterator<Page> i = pages.iterator(); i.hasNext(); ) {
                Page page = i.next();
                String selectStatement = "INSERT INTO PAGES (ID, TITLE, TYPE) VALUES (?, ?, ?);";
                PreparedStatement prepStmt = c.prepareStatement(selectStatement);
                prepStmt.setInt(1, j);
                prepStmt.setString(2, page.getName());
                prepStmt.setString(3, page.getInfoBox().getType());
                prepStmt.executeUpdate();
                j++;
            }
            c.commit();
            c.close();
            System.out.println("Records inserted successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void createDb() {
        Statement stmt = null;
        try {
            Connection c = getDbConnection();

            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS PAGES; CREATE TABLE PAGES " +
                    "(ID INT NOT NULL," +
                    " TITLE TEXT NOT NULL, " +
                    " TYPE TEXT NOT NULL);";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table RECORDS created successfully");
    }

    public static void correctRecords() {
        Statement stmt = null;
        try {
            Connection c = getDbConnection();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, TYPE FROM (SELECT ID, TYPE, COUNT(*) AS NUM FROM PAGES GROUP BY TYPE) WHERE NUM=1;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                stmt = c.createStatement();
                String sql = "UPDATE PAGES SET TYPE = '"+type.replaceAll("^([a-zA-Z0-9\\._-]+) .*$", "$1").trim()+"' WHERE ID="+id+";";
                stmt.executeUpdate(sql);
                c.commit();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records was succesfully corrected");
    }

    public static void printResults() {
        Statement stmt = null;
        try {
            Connection c = getDbConnection();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, TYPE, COUNT(*) AS NUM FROM PAGES GROUP BY TYPE ORDER BY NUM DESC;");
            while (rs.next()) {
                String types = rs.getString("type");
                int count = rs.getInt("num");
                int id = rs.getInt("id");
                System.out.println(id+":"+types + " (" + count + ")");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static List<Record> getRecords() {
        Statement stmt = null;
        List<Record> records = new ArrayList<Record>();
        try {
            Connection c = getDbConnection();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, TYPE, TITLE FROM PAGES GROUP BY TYPE ORDER BY TYPE;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                records.add(new Record(id, type, type));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return records;
    }

    public static void printRecords() {
        Statement stmt = null;
        try {
            Connection c = getDbConnection();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, TYPE, TITLE FROM PAGES ORDER BY TITLE;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                System.out.println(id+":"+title + ":" + type);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
