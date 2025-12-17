package hei.group.plat;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBConnexion {
    final  String URL = "jdbc:postgresql://localhost:5432/database mini_dish_db";
    final  String USER = "mini_dish_manager";
    final  String PASSWORD = "harena";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        DBConnexion db = new DBConnexion();
        try (Connection conn = db.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion réussie à la base de données !");
            } else {
                System.out.println("Échec de la connexion !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}
