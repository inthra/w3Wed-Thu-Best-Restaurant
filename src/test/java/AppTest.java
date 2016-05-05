import org.sql2o.*; // for DB support
import org.junit.*; // for @Before and @After
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

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
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Find the best restaurants!");
    assertThat(pageSource()).contains("View Cuisine List");
    assertThat(pageSource()).contains("Add a New Cuisine");
  }

  @Test
  public void cuisineIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Add a New Cuisine"));
    fill("#cuisine_type").with("BBQ");
    submit(".btn");
    assertThat(pageSource()).contains("Your cuisine has been saved.");
  }

  @Test
  public void cuisineIsDisplayedTest() {
    goTo("http://localhost:4567/cuisines/new");
    fill("#cuisine_type").with("BBQ");
    submit(".btn");
    click("a", withText("View cuisines"));
    assertThat(pageSource()).contains("BBQ");
  }

  @Test
  public void cuisineShowPageDisplaysCuisineType() {
    goTo("http://localhost:4567/cuisines/new");
    fill("#cuisine_type").with("BBQ");
    submit(".btn");
    click("a", withText("View cuisines"));
    click("a", withText("BBQ"));
    assertThat(pageSource()).contains("BBQ");
  }

  @Test
  public void cuisineRestaurantsFormIsDisplayed() {
    goTo("http://localhost:4567/cuisines/new");
    fill("#cuisine_type").with("BBQ");
    submit(".btn");
    click("a", withText("View cuisines"));
    click("a", withText("BBQ"));
    click("a", withText("Add a new restaurant"));
    assertThat(pageSource()).contains("Add a restaurant to BBQ");
  }

  @Test
  public void restaurantsIsAddedAndDisplayed() {
    goTo("http://localhost:4567/cuisines/new");
    fill("#cuisine_type").with("BBQ");
    submit(".btn");
    click("a", withText("View cuisines"));
    click("a", withText("BBQ"));
    click("a", withText("Add a new restaurant"));
    fill("#restaurant_name").with("Pit BBQ");
    fill("#restaurant_description").with("Tasty smoke");
    submit(".btn");
    click("a", withText("View cuisines"));
    click("a", withText("BBQ"));
    assertThat(pageSource()).contains("Pit BBQ");
    assertThat(pageSource()).contains("Tasty smoke");
  }

  @Test
  public void cuisineUpdate() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", testCuisine.getId());
    goTo(cuisinePath);
    fill("#cuisine_type").with("Texas BBQ");
    submit("#update-cuisine");
    assertThat(pageSource()).contains("Texas BBQ");
  }

  @Test
  public void cuisineDelete() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", testCuisine.getId());
    goTo(cuisinePath);
    submit("#delete-cuisine");
    assertEquals(0, Cuisine.all().size());
    assertThat(pageSource()).contains("Your cuisine has been deleted.");
  }

  @Test
  public void restaurantUpdate() {
    Cuisine testCuisine = new Cuisine("BBQ");
    testCuisine.save();
    Restaurant testRestaurant = new Restaurant("Pit BBQ", "Tasty smoke", testCuisine.getId());
    testRestaurant.save();
    String restaurantPath = String.format("http://localhost:4567/cuisines/%d/restaurants/%d", testCuisine.getId(), testRestaurant.getId());
    goTo(restaurantPath);
    fill("#restaurant_name").with("Pit BBQ 2");
    fill("#restaurant_description").with("Real smokey");
    submit("#update-restaurant");
    assertThat(pageSource()).contains("Pit BBQ 2");
  }
}
