import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class Cuisine {
  private int id;
  private String cuisine_type;

  public Cuisine(String cuisine_type) {
    this.cuisine_type = cuisine_type;
  }

  public String getType() {
    return cuisine_type;
  }

  public static List<Cuisine> all() {
    String sql = "SELECT id, cuisine_type FROM cuisines";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Cuisine.class);
    }
  }

  @Override
  public boolean equals (Object otherCuisine) {
    if (!(otherCuisine instanceof Cuisine)) {
      return false;
    } else {
      Cuisine newCuisine = (Cuisine) otherCuisine;
      return this.getType().equals(newCuisine.getType());
    }
  }
}
