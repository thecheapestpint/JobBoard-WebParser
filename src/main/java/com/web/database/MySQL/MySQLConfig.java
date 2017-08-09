package com.web.database.MySQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConfig {

    private static boolean debug = false;

    private static String devURL = "jdbc:mysql://localhost:3306/";
    private static String devUsername = "root";
    private static String devPassword = "";

    private static String URL = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
    private static String password = "VWxMhFo3X0";

    public static Connection getConnectionURL(String db){
        try {
            return (debug) ? DriverManager.getConnection(devURL + db, devUsername, devPassword) : DriverManager.getConnection(URL + db, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}