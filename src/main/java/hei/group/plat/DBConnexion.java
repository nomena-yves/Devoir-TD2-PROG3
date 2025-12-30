package hei.group.plat;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnexion {
    final  String URL = "jdbc:postgresql://localhost:5432/mini_dish_db";
    final  String USER = "mini_dish_manager";
    final  String PASSWORD = "harena";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
