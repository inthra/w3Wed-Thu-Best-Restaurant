import org.sql2o.*;
import java.util.Arrays;

public class Cuisine {
  private int id;
  private String cuisine_type;

  public Cuisine(String cuisine_type) {
    this.cuisine_type = cuisine_type;
  }

  public String getType() {
    return cuisine_type;
  }
}
