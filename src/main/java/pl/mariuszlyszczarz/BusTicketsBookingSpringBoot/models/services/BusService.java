package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.CityModel;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.PassengerModel;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.respositories.CityRepository;

import java.util.*;

@Service
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class BusService {
    private PassengerModel[][] seats;
    private List<PassengerModel> listOfPassengers;

    public BusService() {
    }

    public void clearSeats() {
        for (int i = 0; i < seats.length; i++) {
            for (int i1 = 0; i1 < seats[i].length; i1++) {
                   seats[i][i1] = null;
            }
        }
    }

    public void arrangementOfPassengers(PassengerModel[][] seats) {
        for (int i = 0; i < seats.length; i++) {
            for (int i1 = 0; i1 < seats[i].length; i1++) {
                if (seats[i][i1] != null) {
                    System.out.print(seats[i][i1].getNumberPassenger() + " ");
                } else {
                    System.out.print("w ");
                }
            }
            System.out.println("");
        }
    }

    public void awardingPlace(PassengerModel passenger, Map<String, Integer> grantedNumberForTheCity) {
        int numberOfCityStart = grantedNumberForTheCity.get(passenger.getFromCity());
        int numberOfCityEnd = grantedNumberForTheCity.get(passenger.getToCity());
        for (int i = 0; i < seats[0].length; i++) {
            if (isSeatsIsFreeFromCityToCity(numberOfCityStart, numberOfCityEnd, i)) {
                seats[numberOfCityStart][i] = passenger;
                passenger.setNumberOfSeat(i + 1);
                break;
            }
        }
        if (passenger.getNumberOfSeat() != 0) {
            for (int i = numberOfCityStart + 1; i < numberOfCityEnd; i++) {
                seats[i]
                        [passenger.getNumberOfSeat() - 1] = passenger;
            }
        } else {
            System.out.println("No free seats");
        }
    }

    public int numberOfFreePlaces(String fromCity, String toCity, Map<String, Integer> grantedNumberForTheCity) {
        int counter = 0;
        for (int i = 0; i < seats[0].length; i++) {
            if (isSeatsIsFreeFromCityToCity(grantedNumberForTheCity.get(fromCity), grantedNumberForTheCity.get(toCity), i)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean isSeatsIsFreeFromCityToCity(int numberOfCityStart, int numberOfCityEnd, int numberOfSeat) {
        boolean isFree = true;
        for (int i = numberOfCityStart; i <= numberOfCityEnd; i++) {
            if (seats[i][numberOfSeat] != null) {
                isFree = false;
            }
        }
        return isFree;
    }

}
