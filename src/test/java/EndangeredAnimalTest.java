import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class EndangeredAnimalTest {
  private EndangeredAnimal testEndangeredAnimal;

  @Before
  public void SetUp(){
    testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    testEndangeredAnimal.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void endangeredAnimal_instantiatesCorrectly_true() {
    assertEquals(true, testEndangeredAnimal instanceof EndangeredAnimal);
  }

  @Test
  public void getHealth_returnsHealthAttribute_true() {
    assertEquals("Healthy", testEndangeredAnimal.getHealth());
  }

  @Test
  public void save_assignsIdAndSavesObjectToDatabase() {
    EndangeredAnimal savedEndangeredAnimal = EndangeredAnimal.allEndangered().get(0);
    assertEquals(testEndangeredAnimal.getId(), savedEndangeredAnimal.getId());
  }

  @Test
  public void allEndangered_returnsAllInstancesOfEndangeredAnimal_true() {
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Badger", "Okay", "Adult");
    secondEndangeredAnimal.save();
    assertEquals(true, EndangeredAnimal.allEndangered().get(0).equals(testEndangeredAnimal));
    assertEquals(true, EndangeredAnimal.allEndangered().get(1).equals(secondEndangeredAnimal));
  }

  @Test
  public void findEndangered_returnsAnimalWithSameId_secondAnimal() {
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Badger", "Okay", "Adult");
    secondEndangeredAnimal.save();
    assertEquals(EndangeredAnimal.findEndangered(secondEndangeredAnimal.getId()), secondEndangeredAnimal);
  }

  @Test
  public void update_updatesHealthAttribute_true() {
    testEndangeredAnimal.updateHealth("ill");
    assertEquals("ill", EndangeredAnimal.findEndangered(testEndangeredAnimal.getId()).getHealth());
  }

  @Test
  public void update_updatesAgeAttribute_true() {
    testEndangeredAnimal.updateAge("Adult");
    assertEquals("Adult", EndangeredAnimal.findEndangered(testEndangeredAnimal.getId()).getAge());
  }

  @Test
  public void endangeredAnimal_setsTypeToEndangeredInConstructor_true(){
    assertEquals("endangered", testEndangeredAnimal.getType());
  }

  @Test
  public void getSightings_returnsAListOfSightings_true(){
    Sighting testSighting = new Sighting(testEndangeredAnimal.getId(), "45.472428, -121.946466", "Ranger Avery", 466);
    Sighting secondTestSighting = new Sighting (testEndangeredAnimal.getId(), "45.472428, -121.946466", "Ranger Reese", 899);
    testSighting.save();
    secondTestSighting.save();
    List<Sighting> allSightings = testEndangeredAnimal.getSightings();
    assertEquals(2, allSightings.size());
    assertEquals(secondTestSighting,allSightings.get(1));
  }

}
