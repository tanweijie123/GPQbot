package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class SQLConn {

    static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;

        try {
            Scanner scanner = new Scanner(new File("dbtoken"));
            conn = DriverManager.getConnection(scanner.next(), scanner.next(), scanner.next());

        } catch (FileNotFoundException e) {
            System.err.println("Unable to retrieve dbtoken file");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return conn;
    }
}
