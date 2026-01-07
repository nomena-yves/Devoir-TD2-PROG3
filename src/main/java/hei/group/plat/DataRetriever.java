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

        String sql = " Select d.id,d.name,d.dish_type,i.name as Name_ingredient,d.price from dish d left join ingredient i on d.id = i.id_dish where d.id = ?";
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
                            new ArrayList<>(),
                            rs.getDouble("price")
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
            String sqlUpdate = "Update dish set name=?,dish_type=?,price=? WHERE id = ?";
            PreparedStatement statement3 = conn.prepareStatement(sqlUpdate);
            statement3.setString(1, dish.getName());
            statement3.setObject(2, dish.getDishType().toString(), java.sql.Types.OTHER);
            statement3.setInt(3, dish.getId());
           statement3.setObject(4,dish.getPrice());
            statement3.executeUpdate();
            System.out.println("Dish updated");
        } else {
            int ingredientId= rs.getInt("id");
            String sqlInsert = "Insert into dish(id,name,dish_type,price=?) values(?,?,?)";
            PreparedStatement statement4 = conn.prepareStatement(sqlInsert);
            statement4.setInt(1, dish.getId());
            statement4.setString(2, dish.getName());
            statement4.setObject(3, dish.getDishType().toString(), java.sql.Types.OTHER);
            statement4.setDouble(4, dish.getPrice());
            for (Ingredient ingredient : dish.getIngredients()) {
               String sqlIngredient="Select name as ingredient_name from ingredient where id_dish = ?";
               PreparedStatement statement5 = conn.prepareStatement(sqlIngredient);
               statement5.setInt(1,ingredientId);
               ResultSet resultSet = statement5.executeQuery();
               if (resultSet.next()) {
                   String UpdateIngredient="Update ingredient Set name=?,price=?,category=?,id_dish=? where id=?";
                   PreparedStatement statement7 = conn.prepareStatement(UpdateIngredient);
                   statement7.setString(1, ingredient.getName());
                   statement7.setDouble(2, ingredient.getPrice());
                   statement7.setObject(3, ingredient.getCategory().toString(), java.sql.Types.OTHER);
                   statement7.setInt(4, ingredient.getDish().getId());
                   statement7.executeUpdate();

               }else {
                   String insertIngredient="insert into ingredient(id,name,price,category,id_dish) values(?,?,?,?,?)";
                   PreparedStatement statement6 = conn.prepareStatement(insertIngredient);
                   statement6.setInt(1, ingredient.getId());
                   statement6.setString(2, ingredient.getName());
                   statement6.setDouble(3, ingredient.getPrice());
                   statement6.setObject(4, ingredient.getCategory().toString(),java.sql.Types.OTHER);
                   statement6.setInt(5, ingredient.getDish().getId());
                   statement6.executeUpdate();
               }
            }
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
    List<Dish> findDishByIngredientName(String IngredientName) throws SQLException {
       Connection conn = dbConnexion.getConnection();
        String sqlfindIdDish = "Select id_dish from ingredient where name = ?";
        List<Dish> dishes = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement(sqlfindIdDish);
            stmt.setString(1, IngredientName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int dishId = rs.getInt("id_dish");
                String sqlFindDish = "Select id,name,dish_type,price from dish where id = ?";
                PreparedStatement statementFindDish = conn.prepareStatement(sqlFindDish);
                statementFindDish.setInt(1, dishId);
                ResultSet rsFindDish = statementFindDish.executeQuery();
                rsFindDish.next();
                Dish dish = new Dish(
                        rsFindDish.getInt("id"),
                        rsFindDish.getString("name"),
                        DishTypeEnum.valueOf(rsFindDish.getString("dish_type")),
                                new ArrayList<>(),
                        rsFindDish.getDouble("price")
                );
                dishes.add(dish);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dishes;
    }



}
