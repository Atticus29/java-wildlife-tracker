import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class AnimalTest {
  private Animal testAnimal;

  @Before
  public void setUp(){
    testAnimal = new Animal("Deer");
    testAnimal.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void animal_instantiatesCorrectly_false() {
    // Animal testAnimal = new Animal("Deer");
    assertEquals(true, testAnimal instanceof Animal);
  }

  @Test
  public void animal_instantiatesWithTypeNotEndangered_true(){
    assertEquals("not endangered", testAnimal.getType());
  }

  @Test
  public void getName_animalInstantiatesWithName_Deer() {
    // Animal testAnimal = new Animal("Deer");
    assertEquals("Deer", testAnimal.getName());
  }

  @Test
  public void equals_returnsTrueIfNameIsTheSame_false() {
    Animal firstAnimal = new Animal("Deer");
    Animal anotherAnimal = new Animal("Deer");
    assertTrue(firstAnimal.equals(anotherAnimal));
  }

  @Test
  public void save_assignsIdToObjectAndSavesObjectToDatabase() {
    // Animal testAnimal = new Animal("Deer");
    // testAnimal.save();
    Animal savedAnimal = Animal.allAnimal().get(0);
    assertEquals(testAnimal.getId(), savedAnimal.getId());
  }

  @Test
  public void allAnimal_returnsAllInstancesOfAnimal_false() {
    // Animal firstAnimal = new Animal("Deer");
    // firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear");
    secondAnimal.save();
    assertEquals(true, Animal.allAnimal().get(0).equals(testAnimal));
    assertEquals(true, Animal.allAnimal().get(1).equals(secondAnimal));
  }

  @Test
  public void find_returnsAnimalWithSameId_secondAnimal() {
    // Animal firstAnimal = new Animal("Deer");
    // firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear");
    secondAnimal.save();
    assertEquals(Animal.findAnimal(secondAnimal.getId()), secondAnimal);
  }

  @Test
  public void delete_deletesAnimalFromDatabase_0() {
    // Animal testAnimal = new Animal("Deer");
    // testAnimal.save();
    testAnimal.delete();
    assertEquals(0, Animal.allAnimal().size());
  }

  public void updateName_updatesAnimalNameInDatabase_String() {
    // Animal testAnimal = new Animal("Deer");
    // testAnimal.save();
    testAnimal.updateName("Buck");
    assertEquals("Buck", testAnimal.getName());
  }

  @Test
  public void find_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.findAnimal(999) == null);
  }

  @Test
  public void getSightings_returnsAListOfSightings_true(){
    // Animal testAnimal = new Animal("Deer");
    // testAnimal.save();
    // Animal secondAnimal = new Animal("Black Bear");
    // secondAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    testSighting.save();
    secondTestSighting.save();
    List<Sighting> allSightings = testAnimal.getSightings();
    assertEquals(2, allSightings.size());
    assertEquals(secondTestSighting,allSightings.get(1));
  }

}
