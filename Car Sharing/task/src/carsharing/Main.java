package carsharing;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database = new Database(args);
        Menu.start();
        database.close();
    }
}