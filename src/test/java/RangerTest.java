import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class RangerTest {
  private Ranger testRanger;

  @Before
  public void setUp(){
    testRanger = new Ranger("Bill", 321, "mark.aaron.fisher@gmail.com");
    testRanger.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void ranger_instantiatesCorrectly_true(){
    assertTrue(testRanger instanceof Ranger);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void ranger_throwsExceptionIfNameTooLong(){
    Ranger longRanger = new Ranger("antidisestablishmentarianismusson", 321, "mark.aaron.fisher@gmail.com");
  }

  @Test (expected = UnsupportedOperationException.class)
  public void ranger_throwsExceptionIfEmailNotValid(){
    Ranger longRanger = new Ranger("Bill", 321,  "@mark.aaron.fisher.gmail.com");
  }

  @Test
  public void save_assignsIdToObjectAndSavesObjectToDatabase() {
    Ranger savedRanger = Ranger.allRangers().get(0);
    assertEquals(testRanger.getId(), savedRanger.getId());
  }

  @Test
  public void update_changesNameBadgeNumberEmail_true(){
    testRanger.update("Fran", 100, "mark.fisher3@pcc.edu");
    assertEquals("Fran",Ranger.findRanger(testRanger.getId()).getName());
    assertEquals(100,Ranger.findRanger(testRanger.getId()).getBadge_number());
    assertEquals("mark.fisher3@pcc.edu",Ranger.findRanger(testRanger.getId()).getEmail());
  }

  @Test
  public void getters_getWhatTheyNeedToGet_true(){
    assertEquals("Bill", testRanger.getName());
    assertEquals(321, testRanger.getBadge_number());
    assertEquals("mark.aaron.fisher@gmail.com", testRanger.getEmail());
    assertTrue(testRanger.getId()>0);
  }

  @Test
  public void equals_returnsTrueIfNameIsTheSame_true() {
    Ranger retrievedRanger = Ranger.findRanger(testRanger.getId());
    assertTrue(testRanger.equals(retrievedRanger));
  }

  @Test
  public void delete_deletesRangerFromDatabase_0() {
    testRanger.delete();
    assertEquals(0, Ranger.allRangers().size());
  }

  @Test
  public void allRangers_returnsAllInstancesOfRanger_true() {
    Ranger secondTestRanger = new Ranger("Stacey", 411, "fishema@ohsu.edu");
    secondTestRanger.save();
    assertEquals(true, Ranger.allRangers().get(0).equals(testRanger));
    assertEquals(true, Ranger.allRangers().get(1).equals(secondTestRanger));
  }

  @Test
  public void findRanger_returnsNullWhenNoRangerFound_null() {
    assertTrue(Ranger.findRanger(999) == null);
  }

  @Test
  public void findByBadge_returnsASingleRanger_true(){
    Ranger restoredRanger = Ranger.findByBadge(321);
    assertEquals("Bill", restoredRanger.getName());
  }

  @Test
  public void findByBadge_returnsNullWhenItDoesNotFindMatch_true(){
    Ranger restoredRanger = Ranger.findByBadge(32);
    assertEquals(null, restoredRanger);
  }

  @Test (expected = UnsupportedOperationException.class)
  public void ranger_throwsExceptionIfBadgeAlreadyExists_true(){
    Ranger testRangerBadge = new Ranger("Perry", 321, "mark.fisher3@pcc.edu");
  }

  @Test
  public void getSightings_returnsListOfSightings_true(){
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getId(), testAnimal.getType(), "45.472428, -121.946466", "Ranger Avery", 321);
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), testAnimal.getType(), "45.472428, -121.946466", "Ranger Reese", 321);
    testSighting.save();
    secondTestSighting.save();
    List<Sighting> allSightings = testRanger.getSightings();
    assertEquals(2, allSightings.size());
    assertEquals(secondTestSighting,allSightings.get(1));
  }
}
