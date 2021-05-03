package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class LoadDriver {
    public static Connection conn = null;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Scanner scanner = new Scanner(new File("dbtoken"));

            conn = DriverManager.getConnection(scanner.next(), scanner.next(), scanner.next());
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM a;";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1)); //1-based indexing
            }

        } catch (FileNotFoundException e) {
            System.err.println("Unable to retrieve dbtoken file");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
