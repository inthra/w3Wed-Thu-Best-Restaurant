import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class Restaurant {
  private int id;
  private String restaurant_name;
  private String restaurant_description;
  private int cuisine_id;

  public Restaurant(String name, String description) {
    this.restaurant_name = name;
    this.restaurant_description = description;
  }

  public String getName() {
    return restaurant_name;
  }

  public String getDescription() {
    return restaurant_description;
  }

  public static List<Restaurant> all() {
    String sql = "SELECT * FROM restaurants";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }
}
