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
}
