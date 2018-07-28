package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.PassengerModel;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.respositories.CityRepository;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services.CityService;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.respositories.PassengerRespository;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services.BusService;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services.PassengerService;

@Controller
public class MainController {

    final
    PassengerRespository passengerRespository;
    final
    BusService busService;
    final
    CityRepository cityRepository;
    final
    CityService cityService;
    final
    PassengerService passengerService;

    @Autowired
    public MainController(PassengerRespository passengerRespository, BusService busService, CityRepository cityRepository, CityService cityService, PassengerService passengerService) {
        this.passengerRespository = passengerRespository;
        this.busService = busService;
        this.cityRepository = cityRepository;
        this.cityService = cityService;
        this.passengerService = passengerService;
    }


    @GetMapping("/")
    public String adminPanel() {
        cityService.setCities(cityRepository.findAll());
        busService.setSeats(new PassengerModel[5][24]);
        cityService.setGrantedNumberForTheCity(cityService.awardingNumberToTheCity(cityService.getCities()));
        passengerService.addSomeUser();
        busService.arrangementOfPassengers(busService.getSeats());
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String indexGet(Model model) {
        cityService.setCities(cityRepository.findAll());
        model.addAttribute("cities", cityService.getCities());
        return "dashboard";
    }

    @PostMapping("/index")
    public String indexPost(@RequestParam("fromCity") String fromCity,
                            @RequestParam("toCity") String toCity, Model model) {
        if (!cityService.isUserChoseCorrectly(fromCity, toCity)) {
            model.addAttribute("warning", "Nie oszukuj wybierz poprawnie miasta");
            model.addAttribute("cities", cityService.getCities());
            return "dashboard";
        }
        model.addAttribute("cities", cityService.getCities());
        model.addAttribute("freeSeats", busService.numberOfFreePlaces(fromCity, toCity, cityService.getGrantedNumberForTheCity()));

        return "dashboard";
    }

    @GetMapping("/addPassenger")
    public String addUserGet(Model model) {
        model.addAttribute("passengerModel", new PassengerModel());
        model.addAttribute("cities", cityService.getCities());
        return "addPassenger";
    }

    @PostMapping("/addPassenger")
    public String addUserPost(@ModelAttribute("passengerModel") PassengerModel passengerModel, Model model) {
        if (!cityService.isUserChoseCorrectly(passengerModel.getFromCity(), passengerModel.getToCity())) {
            model.addAttribute("warning", "Nie oszukuj wybierz poprawnie miasta");
            model.addAttribute("cities", cityService.getCities());
            return "addPassenger";
        }

        if (busService.numberOfFreePlaces(passengerModel.getFromCity(), passengerModel.getToCity(), cityService.getGrantedNumberForTheCity()) < 1) {
            model.addAttribute("info", "false");
            model.addAttribute("cities", cityService.getCities());
            return "addPassenger";
        }
        model.addAttribute("info", "true");
        busService.awardingPlace(passengerModel, cityService.getGrantedNumberForTheCity());
        passengerService.giveNumberOfPassenger(passengerModel, passengerRespository.findAll());

        passengerRespository.save(passengerModel);
        busService.getListOfPassengers().add(passengerModel);

        model.addAttribute("cities", cityService.getCities());
        return "addPassenger";
    }

    @GetMapping("/listOfPassengers")
    public String listOfPassengersGet(Model model) {
        model.addAttribute("passengers", passengerRespository.findAll());
        return "listOfPassengers";
    }

    @PostMapping("/listOfPassengers")
    public String listOfPassengersPost() {

        return "/index";
    }

    @GetMapping("/delete/{number}")
    public String deleteGet(@PathVariable("number") int numberPassenger) {
        passengerRespository.delete(numberPassenger);
        return "redirect:/listOfPassengers";
    }

    @GetMapping("/status")
    public String statusGet(Model model) {
        busService.clearSeats();
        passengerService.addSomeUser();

        model.addAttribute("seats", busService.getSeats());
        return "status";
    }

    @PostMapping("/status")
    public String statusPost() {

        return "status";
    }

}
