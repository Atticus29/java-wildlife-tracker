import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.util.Date;
import java.sql.Timestamp;

public class SightingTest {
  private Sighting testSighting;
  private Animal testAnimal;

  @Before
  public void setUp(){
    testAnimal = new Animal("Deer");
    testAnimal.save();
    testSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void sighting_instantiatesCorrectly_true() {
    assertEquals(true, testSighting instanceof Sighting);
  }

  @Test
  public void equals_returnsTrueIfLocationAndDescriptionAreSame_true() {

    Sighting testSighting1 = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    Sighting anotherSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertTrue(testSighting1.equals(anotherSighting));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Sighting() {
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
  }

  @Test
  public void all_returnsAllInstancesOfSighting_true() {
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
    assertEquals(true, Sighting.all().get(1).equals(secondTestSighting));
  }

  @Test
  public void find_returnsSightingWithSameId_secondSighting() {
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(Sighting.find(secondTestSighting.getId()), secondTestSighting);
  }

  @Test
  public void findAnimal_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.findAnimal(999) == null);
  }

  @Test
  public void save_StoresAMeaningfulTimeStamp_true(){
    assertTrue(testSighting.getTimeSighted() instanceof Timestamp);
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDate(), testSighting.getTimeSighted().getDate());
  }

}
