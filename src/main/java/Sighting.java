import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.Date;

public class Sighting implements DatabaseManagement{
  private int animal_id;
  private String location;
  private String ranger_name;
  private int id;
  private Timestamp time_sighted;

  public Sighting(int animal_id, String location, String ranger_name) {
    this.animal_id = animal_id;
    this.location = location;
    this.ranger_name = ranger_name;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public Timestamp getTime_sighted(){
    return this.time_sighted;
  }

  public String getTime_sightedAsString(){
    return this.time_sighted.toGMTString();
  }

  public int getAnimalId() {
    return animal_id;
  }

  public String getLocation() {
    return location;
  }

  public String getRangerName() {
    return ranger_name;
  }

  @Override
  public boolean equals(Object otherSighting) {
    if(!(otherSighting instanceof Sighting)) {
      return false;
    } else {
      Sighting newSighting = (Sighting) otherSighting;
      return this.getAnimalId() == (newSighting.getAnimalId()) && this.getLocation().equals(newSighting.getLocation()) && this.getRangerName().equals(newSighting.getRangerName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO sightings (time_sighted, animal_id, location, ranger_name) VALUES (now(), :animal_id, :location, :ranger_name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("animal_id", this.animal_id)
        .addParameter("location", this.location)
        .addParameter("ranger_name", this.ranger_name)
        // .addParameter("ranger_id", )
        .throwOnMappingFailure(false)
        .executeUpdate()
        .getKey();
      String timeSql = "SELECT time_sighted FROM sightings WHERE id=:id;";
        Timestamp sightTime = con.createQuery(timeSql)
          .addParameter("id", this.id)
          .throwOnMappingFailure(false)
          .executeAndFetchFirst(Timestamp.class);
        this.time_sighted = sightTime;
    }
  }

  public static List<Sighting> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings;";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Sighting.class);
    }
  }

  public static Sighting find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE id=:id;";
      Sighting sighting = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Sighting.class);
      return sighting;
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM sightings WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

}
