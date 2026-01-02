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
        conn.setAutoCommit(false);
        try {
            for (Ingredient ingredient : ingredients) {
                String sqlSelect= "Select id from ingredient where name = ?";
                PreparedStatement stmt1 = conn.prepareStatement(sqlSelect);
                stmt1.setString(1, ingredient.getName());
                ResultSet rs = stmt1.executeQuery();

                if (rs.next()) {
                 int resutl= rs.getInt("id");
                 String Update="Update ingredient Set name=?,price=?,category=?,id_dish=? where id=?";
                 PreparedStatement statement2 = conn.prepareStatement(Update);
                 statement2.setString(1, ingredient.getName());
                 statement2.setDouble(2, ingredient.getPrice());
                 statement2.setString(3, ingredient.getCategory().toString());
                 statement2.setInt(4,ingredient.getDish().getId());
                  statement2.executeQuery();

                } else {
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    stmt.setInt(1, ingredient.getId());
                    stmt.setString(2, ingredient.getName());
                    stmt.setDouble(3, ingredient.getPrice());
                    stmt.setObject(4, ingredient.getCategory().toString(), java.sql.Types.OTHER);
                    stmt.setInt(5, ingredient.getDish().getId());
                    stmt.executeUpdate();

                    conn.commit();
                    System.out.println("Ingredients inserted");
                }
            }
        }catch (SQLException e) {
            conn.rollback();
            System.out.println(e.getMessage());
        }finally {
            conn.setAutoCommit(true);
            conn.close();
        }
        return ingredients;
    }

public Dish saveDish(Dish dish) throws SQLException {
    Connection conn = dbConnexion.getConnection();
    conn.setAutoCommit(false);

    try {
        String sqlSelect = "Select id from dish where name = ?";
        PreparedStatement statementSelect = conn.prepareStatement(sqlSelect);
        statementSelect.setString(1, dish.getName());
        ResultSet rs = statementSelect.executeQuery();
        if (rs.next()) {
            dish.setId(rs.getInt("id"));
            String sqlUpdate = "Update dish set name=?,dish_type=? WHERE id = ?";
            PreparedStatement statement3 = conn.prepareStatement(sqlUpdate);
            statement3.setString(1, dish.getName());
            statement3.setObject(2, dish.getDishType().toString(), java.sql.Types.OTHER);
            statement3.setInt(3, dish.getId());
            statement3.executeUpdate();
            System.out.println("Dish updated");
        } else {
            String sqlInsert = "Insert into dish(id,name,dish_type) values(?,?,?)";
            PreparedStatement statement4 = conn.prepareStatement(sqlInsert);
            statement4.setInt(1, dish.getId());
            statement4.setString(2, dish.getName());
            statement4.setObject(3, dish.getDishType().toString(), java.sql.Types.OTHER);
            statement4.executeUpdate();
            System.out.println("Dish inserted");
        }
        conn.commit();
    }catch (SQLException e) {
        conn.rollback();
        System.out.println(e.getMessage());
    }
    return dish;
}
}
