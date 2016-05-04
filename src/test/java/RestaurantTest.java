import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class RestaurantTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/best_restaurants_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      con.createQuery(deleteCuisinesQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
    }
  }

  @Test
  public void Restaurant_instantiatesCorrectly_true() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    assertTrue(testRestaurant instanceof Restaurant);
  }

  @Test
  public void getName_instantiatesWithNameAndDescription_String() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    assertEquals("Pit BBQ", testRestaurant.getName());
  }
}
