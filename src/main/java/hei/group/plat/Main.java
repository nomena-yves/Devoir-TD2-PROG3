package hei.group.plat;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
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