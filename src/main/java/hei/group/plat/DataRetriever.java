package hei.group.plat;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    DBConnexion dbConnexion = new DBConnexion();
    Connection con;

    Dish findDishById(Integer id) throws SQLException {

        String sql = " Select d.id,d.name,d.dish_type,i.name as Name_ingredient from dish d left join ingredient i on d.id = i.id_dish where d.id = ?";
        Dish dish = null;
        try (Connection conn = dbConnexion.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (dish == null) {
                    dish = new Dish(
                            rs.getInt("id"),
                            rs.getString("name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type")),
                            new ArrayList<>()
                    );
                }

                if (rs.getString("Name_ingredient") != null) {
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

    List<Ingredient> findByIngredient(int page, int size) throws SQLException {
        int offset = (page - 1) * size;
        String sql = "select i.id,i.name,i.price,i.category,i.id_dish from ingredient i limit ? offset ? ";
        List<Ingredient> Listingredients = new ArrayList<>();
        try (Connection conn = dbConnexion.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, size);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        null

                );
                Listingredients.add(ingredient);
            }
            return Listingredients;
        }
    }

    List<Ingredient> CreateIngredient(List<Ingredient> ingredients) throws SQLException {
        String sql = "insert into ingredient(id,name,price,category,id_dish) values(?,?,?,?,?)";
        Connection conn = dbConnexion.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Ingredient ingredient : ingredients) {
                stmt.setInt(1, ingredient.getId());
                stmt.setString(2, ingredient.getName());
                stmt.setDouble(3, ingredient.getPrice());
                stmt.setObject(4, ingredient.getCategory().toString(),java.sql.Types.OTHER);
                stmt.setInt(5, ingredient.getDish().getId());
                stmt.executeUpdate();
            }
            conn.commit();
            System.out.println("Ingredients inserted");
        }catch (SQLException e) {
            conn.rollback();
            System.out.println(e.getMessage());
        }finally {
            conn.setAutoCommit(true);
            conn.close();
        }
        return ingredients;
    }
}
