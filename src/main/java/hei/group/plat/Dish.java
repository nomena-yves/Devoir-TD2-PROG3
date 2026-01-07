package hei.group.plat;

import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;
    private Double price;

    public Dish(int id, String name, DishTypeEnum dishType, List<Ingredient> ingredients, Double price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(ingredients, dish.ingredients)&& Objects.equals(price, dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, ingredients, price);
    }

    public Double getDishCost(){
        Double priceTotal = 0.0;
        if (ingredients == null) {
            priceTotal =null;
        }else {
     ingredients.stream().mapToDouble(Ingredient::getPrice)
             .sum();
        }
        return price;
    }

    public Double getGrossMargin(){
        Double priceTotalIngredient = 0.0;
        Double margin = 0.0;
     try{
         for (Ingredient ingredient : ingredients) {
             priceTotalIngredient=+ingredient.getPrice();
         }
        margin=getPrice() -priceTotalIngredient;
     }catch(Exception e) {
         System.out.println(e.getMessage());
         System.out.println(" votre prix n existe pas");
     }
     return margin;
    }
}
