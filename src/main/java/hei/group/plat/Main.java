package hei.group.plat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hei.group.plat.CategoryEnum.ANIMAL;
import static hei.group.plat.DishTypeEnum.MAIN;

public class Main {
    public static void main(String[] args) {
        DBConnexion db = new DBConnexion();
        DataRetriever dr = new DataRetriever();
        List<Ingredient> ingredients = new ArrayList<>();
        Dish steakBoeuf=new Dish(8,"steak",MAIN,ingredients);
        Ingredient viande_Hacher= new Ingredient(6,"viande_hache",200.00,ANIMAL,steakBoeuf);
        ingredients.add(viande_Hacher);
        try (Connection conn = db.getConnection()) {
            System.out.println("Connexion réussie !");
            //System.out.println(dr.findDishById(1));
            //System.out.println(dr.findByIngredient(2,3));
          //dr.CreateIngredient(ingredients);
            dr.saveDish(steakBoeuf);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}