package hei.group.plat;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBConnexion db = new DBConnexion();
        DataRedriever dr = new DataRedriever();
        try (Connection conn = db.getConnection()) {
            System.out.println("Connexion réussie !");
            System.out.println(dr.findDishById(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}