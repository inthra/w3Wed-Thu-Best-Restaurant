import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class CuisineTest {
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
  public void Cuisine_instantiatesCorrectly_true() {
    Cuisine testCuisine = new Cuisine("BBQ");
    assertTrue(testCuisine instanceof Cuisine);
  }

  @Test
  public void Cuisine_instantiatesWithCuisineType_String() {
    Cuisine testCuisine = new Cuisine("BBQ");
    assertEquals("BBQ", testCuisine.getType());
  }

  @Test
  public void all_CuisineListEmptyAtFirst() {
    assertEquals(Cuisine.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreSame() {
    Cuisine firstCuisine = new Cuisine("BBQ");
    Cuisine secondCuisine = new Cuisine("BBQ");
    assertTrue(firstCuisine.equals(secondCuisine));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    assertTrue(Cuisine.all().get(0).equals(testCuisine));
  }

  @Test
  public void save_assignsIdToObject() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    Cuisine savedCuisine = Cuisine.all().get(0);
    assertEquals(testCuisine.getId(), savedCuisine.getId());
  }

  @Test
  public void find_findCuisineInDatabase_true() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    Cuisine savedCuisine = Cuisine.find(testCuisine.getId());
    assertTrue(testCuisine.equals(savedCuisine));
  }

  @Test
  public void getRestaurants_retrievesAllRestaurantsFromDatabase_restaurantsList() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    Restaurant firstRestaurant = new Restaurant("Pit BBQ", "Tasty smoke", testCuisine.getId());
    firstRestaurant.save();
    Restaurant secondRestaurant = new Restaurant("Sticky Fingers", "Saucy sauce", testCuisine.getId());
    secondRestaurant.save();
    Restaurant[] restaurants = new Restaurant[] { firstRestaurant, secondRestaurant };
    assertTrue(testCuisine.getRestaurants().containsAll(Arrays.asList(restaurants)));
  }
}
