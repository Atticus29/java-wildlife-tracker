import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Animal {
  public String name;
  public int id;
  public String type;

  public Animal(String name) {
    if(name.equals("") || name == null){
      throw new UnsupportedOperationException("Name is empty!");
    }else{
      this.name = name;
    }
    this.type = "not endangered";
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public String getType(){
    return this.type;
  }

  @Override
  public boolean equals(Object otherAnimal) {
    if(!(otherAnimal instanceof Animal)) {
      return false;
    } else {
      Animal newAnimal = (Animal) otherAnimal;
      return this.getName().equals(newAnimal.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO animals (type, name) VALUES (:type, :name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("type", this.type)
        .addParameter("name", this.name)
        .throwOnMappingFailure(false)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Animal> allAnimal() {
    try(Connection con = DB.sql2o.open()) {
      // String sql = "SELECT * FROM animals;";
      String sql = "SELECT * FROM animals WHERE type='not endangered';";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Animal.class);
    }
  }

  public static Animal findAnimal(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM animals WHERE id=:id AND type='not endangered';";
      Animal animal = con.createQuery(sql)
        .throwOnMappingFailure(false)
        .addParameter("id", id)
        .executeAndFetchFirst(Animal.class);
      return animal;
    }
  }

  public void updateName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET name=:name WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("name", name)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM animals WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Sighting> getSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE animal_id=:id;";
        List<Sighting> sightings = con.createQuery(sql)
          .addParameter("id", this.id)
          .throwOnMappingFailure(false)
          .executeAndFetch(Sighting.class);
      return sightings;
    }
  }

}
