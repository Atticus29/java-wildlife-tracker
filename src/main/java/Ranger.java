import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;
import java.util.regex.*;

public class Ranger implements DatabaseManagement {
  private int badge_number;
  private String name;
  public static final int MAX_NAME_LENGTH = 30;
  private String email;
  private int id;


  public Ranger(String name, int badgeNumber, String email) {
    if(name.length() > MAX_NAME_LENGTH){
      throw new UnsupportedOperationException("Name is too long for the database.");
    } else{
      this.name = name;
    }
    // make it throw an error if someone already has that badge number
    this.badge_number = badgeNumber;
    String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    if(!Pattern.matches(emailPattern, email)){
      throw new UnsupportedOperationException ("Not a valid email address");
    } else{
      this.email = email;
    }
  }

  public int getId(){
    return this.id;
  }

  public int getBadge_number(){
    return this.badge_number;
  }

  public String getName(){
    return this.name;
  }

  public String getEmail(){
    return this.email;
  }

  @Override
  public boolean equals(Object otherRanger) {
    if(!(otherRanger instanceof Ranger)) {
      return false;
    } else {
      Ranger newRanger = (Ranger) otherRanger;
      return this.getName().equals(newRanger.getName()) &&
      this.getBadge_number() == newRanger.getBadge_number() &&
      this.getId() == newRanger.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      // make it throw an error if someone already has that name?
      String sql = "INSERT INTO Rangers (badge_number, name, email) VALUES (:badge_number, :name, :email);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("badge_number", this.badge_number)
        .addParameter("name", this.name)
        .addParameter("email", this.email)
        .throwOnMappingFailure(false)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Ranger> allRangers() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM rangers;";
      return con.createQuery(sql)
        // .throwOnMappingFailure(false)
        .executeAndFetch(Ranger.class);
    }
  }

  public static Ranger findRanger(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM rangers WHERE id=:id;";
      Ranger Ranger = con.createQuery(sql)
        .throwOnMappingFailure(false)
        .addParameter("id", id)
        .executeAndFetchFirst(Ranger.class);
      return Ranger;
    }
  }

  public void updateName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE Rangers SET name=:name WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("name", name)
        .executeUpdate();
    }
  }

  public void updateBadgeNumber(int badgeNum) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE Rangers SET badge_number=:badge_number WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("badge_number", badgeNum)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM rangers WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Sighting> getSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE Ranger_id=:id;";
        List<Sighting> sightings = con.createQuery(sql)
          .addParameter("id", this.id)
          .throwOnMappingFailure(false)
          .executeAndFetch(Sighting.class);
      return sightings;
    }
  }

  public static Ranger findByBadge(int badge){
    String sqlQuery = "SELECT * FROM rangers WHERE badge_number=:badge_number;";
    try(Connection con=DB.sql2o.open()){
      Ranger result = con.createQuery(sqlQuery)
        .addParameter("badge_number", badge)
        .executeAndFetchFirst(Ranger.class);
      return result;
    }
  }


}
