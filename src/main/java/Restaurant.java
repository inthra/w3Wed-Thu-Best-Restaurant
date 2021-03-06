import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class Restaurant {
  private int id;
  private String restaurant_name;
  private String restaurant_description;
  private int cuisine_id;

  public Restaurant(String name, String description, int cuisine_id) {
    this.restaurant_name = name;
    this.restaurant_description = description;
    this.cuisine_id = cuisine_id;
  }

  public String getName() {
    return restaurant_name;
  }

  public String getDescription() {
    return restaurant_description;
  }

  public int getId() {
    return id;
  }

  public int getCuisineId() {
    return cuisine_id;
  }

  public static List<Restaurant> all() {
    String sql = "SELECT * FROM restaurants";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }

  @Override
  public boolean equals(Object otherRestaurant) {
    if (!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getName().equals(newRestaurant.getName()) && this.getDescription().equals(newRestaurant.getDescription());
    }
  }

  public void save(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants (restaurant_name, restaurant_description, cuisine_id) VALUES (:name, :description, :cuisine_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name" , this.restaurant_name)
        .addParameter("description", this.restaurant_description)
        .addParameter("cuisine_id", this.cuisine_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE id=:id";
      Restaurant restaurant = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
      return restaurant;
    }
  }

  public void update(String restaurant_name, String restaurant_description) {
    if (restaurant_name != null) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE restaurants SET restaurant_name = :restaurant_name WHERE id = :id";
        con.createQuery(sql)
          .addParameter("restaurant_name", restaurant_name)
          .addParameter("id", this.getId())
          .executeUpdate();
      }
    }
    if (restaurant_description != null) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE restaurants SET restaurant_description = :restaurant_description WHERE id = :id";
        con.createQuery(sql)
          .addParameter("restaurant_description", restaurant_description)
          .addParameter("id", this.getId())
          .executeUpdate();
      }
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM restaurants WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id", this.getId())
        .executeUpdate();
    }
  }
}
