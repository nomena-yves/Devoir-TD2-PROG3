package hei.group.plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataRedriever {
   DBConnexion dbConnexion = new DBConnexion();
   Connection con;

Dish findDishById(Integer id)throws SQLException {

   String sql=" Select d.id,d.name,d.dish_type,i.name as Name_ingredient from dish d left join ingredient i on d.id = i.id_dish where d.id = ?";
    Dish dish=null;
   try (Connection conn =dbConnexion.getConnection() ){

       PreparedStatement stmt =conn.prepareStatement(sql);
           stmt.setInt(1,id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
           if(dish==null){
               dish = new Dish(
                       rs.getInt("id"),
                       rs.getString("name"),
                       DishTypeEnum.valueOf(rs.getString("dish_type")),
                        new ArrayList<>()
               );
           }

           if (rs.getString("Name_ingredient")!=null) {
            Ingredient ingredient = new Ingredient(
                    null,
                    rs.getString("Name_ingredient"),
                   null,
                    null,
                    null
            );
            dish.getIngredients().add(ingredient);
           }
        }

   }
   return dish;
}

}
