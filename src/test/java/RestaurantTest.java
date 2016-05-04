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
  public void getName_instantiatesWithName_String() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    assertEquals("Pit BBQ", testRestaurant.getName());
  }

  @Test
  public void getDescription_instantiatesWithDescription_String() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    assertEquals("Tasty smoke", testRestaurant.getDescription());
  }

  @Test
  public void all_RestaurantListIsEmptyFirst() {
    assertEquals(Restaurant.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreSame() {
    Restaurant firstRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    Restaurant secondRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    assertTrue(firstRestaurant.equals(secondRestaurant));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    testRestaurant.save();
    assertTrue(Restaurant.all().get(0).equals(testRestaurant));
  }

  @Test
  public void save_assignsIdToObject() {
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke");
    testRestaurant.save();
    Restaurant savedRestaurant = Restaurant.all().get(0);
    assertEquals(testRestaurant.getId(), savedRestaurant.getId());
  }
}
