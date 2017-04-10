import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    post("/", (request, response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      String mode = request.queryParams("idIfEdit");
      String newRangerName = request.queryParams("ranger-name");
      int newRangerBadge = Integer.parseInt(request.queryParams("ranger-badge"));
      String newRangerEmail = request.queryParams("ranger-email");
      if(mode.equals("new")){
        try{
          Ranger newRanger = new Ranger(newRangerName, newRangerBadge, newRangerEmail);
          newRanger.save();
        } catch(RuntimeException e){
          System.out.println(e.getClass().getName());
          response.redirect("/error");
          return null;
        }

      } else{
        int currentId = Integer.parseInt(mode);
        Ranger currentRanger = Ranger.findRanger(currentId);
        currentRanger.update(newRangerName, newRangerBadge, newRangerEmail);
      }
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", Animal.allAnimal());
      model.put("rangers", Ranger.allRangers());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("sightings", Sighting.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int animalIdSelected = Integer.parseInt(request.queryParams("animalSelected"));
      String newAnimalType = request.queryParams("animal-type");
      String newLocation = request.queryParams("latLong");
      String rangerName = request.queryParams("rangerName");
      int newRangerBadge = Integer.parseInt(request.queryParams("badge-num"));
      try{
        Sighting sighting = new Sighting(animalIdSelected, newAnimalType, newLocation, rangerName, newRangerBadge);
        sighting.save();
        model.put("sighting", sighting);
      } catch(RuntimeException e){
        System.out.println(e.getClass().getName());
        response.redirect("/error");
        return null;
      }
      Animal animal = Animal.findAnimal(animalIdSelected);
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(animalIdSelected);
      if(animal != null){
        model.put("animal", animal.getName());
      } else{
        model.put("animal", endangeredAnimal.getName());
      }
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", Animal.allAnimal());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("template", "templates/animal-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      boolean endangered = request.queryParamsValues("endangered")!=null;
      boolean shouldUpdate= request.queryParams("update")!=null;
      if (endangered) {
        String name = request.queryParams("name");
        String health = request.queryParams("health");
        String age = request.queryParams("age");
        if(shouldUpdate){
          int endangeredAnimalId = Integer.parseInt(request.queryParams("endangeredAnimalId"));
          EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(endangeredAnimalId);
          endangeredAnimal.updateName(name);
          endangeredAnimal.updateHealth(health);
          endangeredAnimal.updateAge(age);
        }else{
          try{
            EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
            endangeredAnimal.save();
          } catch(RuntimeException e){
            System.out.println(e.getClass().getName());
            response.redirect("/error");
            return null;
          }
        }
        model.put("animals", Animal.allAnimal());
        model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      } else {
        String name = request.queryParams("name");
        if(shouldUpdate){
          int animalId = Integer.parseInt(request.queryParams("animalId"));
          Animal animal = Animal.findAnimal(animalId);
          animal.updateName(name);
        } else{
          try{
            Animal animal = new Animal(name);
            animal.save();
          } catch(RuntimeException e){
            System.out.println(e.getClass().getName());
            response.redirect("/error");
            return null;
          }
        }
        model.put("animals", Animal.allAnimal());
        model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      }
      response.redirect("/");
      return null;
    });

    get("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      int currentId = Integer.parseInt(request.params(":id"));
      Animal animal = Animal.findAnimal(currentId);
      System.out.println(animal == null);
      if(animal != null){
        model.put("animal", animal);
        model.put("type", "animal");
      }
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(currentId);
      if(endangeredAnimal != null){
        model.put("endangeredAnimal", endangeredAnimal);
        model.put("type", "endangeredAnimal");
      }
      model.put("template", "templates/animal.vtl");
      System.out.println(model.keySet());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/error.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/:id/remove/:sightingID", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int animalId = Integer.parseInt(request.params(":id"));
      int sightingID = Integer.parseInt(request.params(":sightingID"));
      Sighting sighting = Sighting.find(sightingID);
      sighting.delete();
      Animal animal = Animal.findAnimal(animalId);
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(animalId);
      if(animal != null){
        model.put("animal", animal);
      }else{
        model.put("endangeredAnimal", endangeredAnimal);
      }
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Animal animal = Animal.findAnimal(Integer.parseInt(request.params("id")));
      model.put("animal", animal);
      model.put("endangeredStatus", false);
      model.put("template", "templates/animal-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/:id/edit/:sightingId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int animalId = Integer.parseInt(request.params(":id"));
      int sightingId = Integer.parseInt(request.params(":sightingId"));
      Sighting currentSighting = Sighting.find(sightingId);
      model.put("sighting", currentSighting);
      model.put("animals", Animal.allAnimal());
      model.put("rangers", Ranger.allRangers());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("editStatus", "true");
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/:id/remove", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int currentId = Integer.parseInt(request.params(":id"));
      Animal animal = Animal.findAnimal(currentId);
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(currentId);
      if(animal != null){
        animal.delete();
      }else{
        endangeredAnimal.delete();
      }
      model.put("animals", Animal.allAnimal());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("sightings", Sighting.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int sightingID = Integer.parseInt(request.queryParams("sighting-id-from-form"));
      Sighting currentSighting = Sighting.find(sightingID);
      String newLocation = request.queryParams("latLong");
      String newRangerName = request.queryParams("rangerName");
      int newBadgeNumber = Integer.parseInt(request.queryParams("badge-num"));
      String newType = request.queryParams("animal-type");
      try{
        currentSighting.update(newType, newLocation, newRangerName, newBadgeNumber);
      } catch(RuntimeException e){
        System.out.println(e.getClass().getName());
        response.redirect("/error");
        return null;
      }
      model.put("animals", Animal.allAnimal());
      model.put("rangers", Ranger.allRangers());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      System.out.println("this happened!");
      model.put("template", "templates/ranger-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/:rangerId/remove", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int rangerId = Integer.parseInt(request.params(":rangerId"));
      Ranger currentRanger = Ranger.findRanger(rangerId);
      currentRanger.delete();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/:rangerId/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int rangerId = Integer.parseInt(request.params(":rangerId"));
      Ranger currentRanger = Ranger.findRanger(rangerId);
      model.put ("ranger", currentRanger);
      model.put("template", "/templates/ranger-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/:rangerId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      System.out.println("Got here");
      int rangerId = Integer.parseInt(request.params(":rangerId"));
      System.out.println("Ranger id is " + rangerId);
      Ranger currentRanger = Ranger.findRanger(rangerId);
      model.put ("ranger", currentRanger);
      List<Sighting> sightings = currentRanger.getSightings();
      model.put("sightings", sightings);
      model.put("template", "/templates/ranger.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
