package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Database {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:.\\src\\carsharing\\db\\";
    public static Connection conn;
    private static PreparedStatement statement;

    public Database(String[] args) {
        try {
            if (args.length > 0 && args[0].equals("-databaseFileName")) {
                DB_URL += args[1];
            } else {
                DB_URL += "anything";
            }
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);

            try {
                String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                        "(ID   INTEGER NOT NULL AUTO_INCREMENT," +
                        " NAME VARCHAR(255) NOT NULL UNIQUE," +
                        "PRIMARY KEY  (ID));";
                statement = conn.prepareStatement(sql);
                statement.execute();

                sql = "CREATE TABLE IF NOT EXISTS CAR " +
                        "(ID   INTEGER NOT NULL AUTO_INCREMENT," +
                        " NAME VARCHAR(255) NOT NULL UNIQUE," +
                        "COMPANY_ID INTEGER NOT NULL," +
                        "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)," +
                        "PRIMARY KEY  (ID));";
                statement = conn.prepareStatement(sql);
                statement.execute();

                sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                        "(ID   INTEGER NOT NULL AUTO_INCREMENT," +
                        " NAME VARCHAR(255) NOT NULL UNIQUE," +
                        "RENTED_CAR_ID INTEGER DEFAULT NULL," +
                        "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)," +
                        "PRIMARY KEY  (ID));";
                statement = conn.prepareStatement(sql);
                statement.execute();
            } catch (Exception se) {
                se.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        System.out.println("Closing connection");
        statement.close();
        conn.close();
    }
}