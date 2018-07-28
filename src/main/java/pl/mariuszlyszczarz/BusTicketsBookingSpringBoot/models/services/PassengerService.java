package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.PassengerModel;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.respositories.PassengerRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    final
    PassengerRespository passengerRespository;
    final
    BusService busService;
    final
    CityService cityService;

    @Autowired
    public PassengerService(PassengerRespository passengerRespository, BusService busService, CityService cityService) {
        this.passengerRespository = passengerRespository;
        this.busService = busService;
        this.cityService = cityService;
    }

    public void giveNumberOfPassenger(PassengerModel passengerModel, List<PassengerModel> listOfPassengers) {
        busService.setListOfPassengers(listOfPassengers);
        int counter = 1;
        List<Integer> numbersBusy = listOfPassengers.stream()
                .mapToInt(s -> s.getNumberPassenger())
                .boxed()
                .collect(Collectors.toList());

        while (numbersBusy.contains(counter)) {
            counter++;
        }

        passengerModel.setNumberPassenger(counter);
    }

    public void addSomeUser() {
        busService.setListOfPassengers(passengerRespository.findAll());
        for (PassengerModel passenger : busService.getListOfPassengers()) {
            PassengerModel copyPassenger = new PassengerModel(passenger);
            busService.awardingPlace(passenger, cityService.getGrantedNumberForTheCity());
            if (passenger.equals(copyPassenger)) {
                continue;
            } else {
                passengerRespository.delete(passenger.getId());
                passengerRespository.save(passenger);
            }
        }
    }
}
