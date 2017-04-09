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
      String newRangerName = request.queryParams("ranger-name");
      int newRangerBadge = Integer.parseInt(request.queryParams("ranger-badge"));
      String newRangerEmail = request.queryParams("ranger-email");
      Ranger newRanger = new Ranger(newRangerName, newRangerBadge, newRangerEmail);
      newRanger.save();
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

    post("/endangered_sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      int animalIdSelected = Integer.parseInt(request.queryParams("endangeredAnimalSelected"));
      String latLong = request.queryParams("latLong");
      int rangerBadge = Integer.parseInt(request.queryParams("badge-num"));
      try{
        Sighting sighting = new Sighting(animalIdSelected, latLong, rangerName, rangerBadge);
        sighting.save();
        model.put("sighting", sighting);
      } catch(RuntimeException e){
        System.out.println(e.getClass().getName());
        response.redirect("/error");
        return null;
      }
      model.put("animals", EndangeredAnimal.allEndangered());
      String animal = EndangeredAnimal.findEndangered(animalIdSelected).getName();
      model.put("animal", animal);
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      int animalIdSelected = Integer.parseInt(request.queryParams("animalSelected"));
      String latLong = request.queryParams("latLong");
      int rangerBadge = Integer.parseInt(request.queryParams("badge-num"));
      try{
        Sighting sighting = new Sighting(animalIdSelected, latLong, rangerName, rangerBadge);
        sighting.save();
        model.put("sighting", sighting);
      } catch(RuntimeException e){
        System.out.println(e.getClass().getName());
        response.redirect("/error");
        return null;
      }
      model.put("animals", Animal.allAnimal());
      String animal = Animal.findAnimal(animalIdSelected).getName();
      model.put("animal", animal);
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

    // get("/:animaltype/:id", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   String animalType = request.params(":animaltype");
    //   if (animalType.equals("animal")){
    //     Animal animal = Animal.findAnimal(Integer.parseInt(request.params(":id")));
    //     model.put("animal", animal);
    //     model.put("type", "animal");
    //     // model.put("template", "templates/animal.vtl");
    //   } else if (animalType.equals("endangered_animal")){
    //     EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
    //     model.put("endangeredAnimal", endangeredAnimal);
    //     model.put("type", "endangeredAnimal");
    //   } else{
    //     response.redirect("/error");
    //     return null;
    //   }
    //   model.put("template", "templates/endangered_animal.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/error.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // get("/endangered_animal/:id/remove/:sightingID", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   int sightingID = Integer.parseInt(request.params(":sightingID"));
    //   Sighting sighting = Sighting.find(sightingID);
    //   sighting.delete();
    //   EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
    //   model.put("endangeredAnimal", endangeredAnimal);
    //   model.put("template", "templates/endangered_animal.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    // get("/animal/:id/remove/:sightingID", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   int sightingID = Integer.parseInt(request.params(":sightingID"));
    //   Sighting sighting = Sighting.find(sightingID);
    //   sighting.delete();
      // Animal animal = Animal.findAnimal(Integer.parseInt(request.params(":id")));
      // model.put("animal", animal);
      // model.put("template", "templates/animal.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    get("/:animal_type/:id/remove/:sightingID", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String animalType = request.params(":animal_type");
      int sightingID = Integer.parseInt(request.params(":sightingID"));
      Sighting sighting = Sighting.find(sightingID);
      sighting.delete();
      if(animalType.equals("endangered_animal")){
        EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
        model.put("endangeredAnimal", endangeredAnimal);
        model.put("template", "templates/endangered_animal.vtl");
      } else{
        Animal animal = Animal.findAnimal(Integer.parseInt(request.params(":id")));
        model.put("animal", animal);
        model.put("template", "templates/animal.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // get("/endangered_animal/:id/remove", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
    //   endangeredAnimal.delete();
    //   model.put("animals", Animal.allAnimal());
    //   model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
    //   model.put("sightings", Sighting.all());
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    get("/endangered_animal/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
      model.put("endangeredAnimal", endangeredAnimal);
      model.put("endangeredStatus", true);
      model.put("template", "templates/animal-form.vtl");
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

    // get("/animal/:id/remove", (request, response) -> {
    //   Map<String, Object> model = new HashMap<String, Object>();
    //   Animal animal = Animal.findAnimal(Integer.parseInt(request.params("id")));
    //   animal.delete();
    //   model.put("animals", Animal.allAnimal());
    //   model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
    //   model.put("sightings", Sighting.all());
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    get("/:animal_type/:id/remove", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.findEndangered(Integer.parseInt(request.params("id")));
      endangeredAnimal.delete();
      model.put("animals", Animal.allAnimal());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("sightings", Sighting.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/:animalType/:id/edit/:sightingId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String animalType = request.params(":animaltype");
      model.put("animalType", animalType);
      boolean editStatus = true;
      model.put("editStatus", editStatus);
      int sightingID = Integer.parseInt(request.params(":sightingId"));
      Sighting currentSighting = Sighting.find(sightingID);
      model.put("sighting", currentSighting);
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
      currentSighting.update(newLocation, newRangerName, newBadgeNumber);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      System.out.println("this happened!");
      model.put("template", "templates/ranger-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/ranger/:rangerId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int rangerId = Integer.parseInt(request.params(":rangerId"));
      Ranger currentRanger = Ranger.findRanger(rangerId);
      List<Sighting> currentRangerSightings = currentRanger.getSightings();
      model.put("sightings", currentRangerSightings);
      model.put("ranger", currentRanger);
      model.put("animals", Animal.allAnimal());
      model.put("endangeredAnimals", EndangeredAnimal.allEndangered());
      model.put("template", "templates/ranger.vtl");
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

    get("/ranger/:rangerId/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int rangerId = Integer.parseInt(request.params(":rangerId"));
      Ranger currentRanger = Ranger.findRanger(rangerId);
      List<Sighting> currentSightings = currentRanger.getSightings();
      model.put("sightings", currentSightings);
      model.put ("ranger", currentRanger);
      model.put("template", "/templates/ranger.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
