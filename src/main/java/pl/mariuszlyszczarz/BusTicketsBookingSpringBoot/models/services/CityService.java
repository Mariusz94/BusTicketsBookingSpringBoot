package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.services;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.CityModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CityService {
    @Getter @Setter
    private List<CityModel> cities;
    @Getter @Setter
    private Map<String, Integer> grantedNumberForTheCity;

    public CityService() {
        grantedNumberForTheCity = new HashMap<>();;
    }

    public boolean isUserChoseCorrectly(String fromCity, String toCity){
        int a = grantedNumberForTheCity.get(fromCity);
        int b = grantedNumberForTheCity.get(toCity);
        return a <= b;
    }

    public Map<String, Integer> awardingNumberToTheCity(List<CityModel> cities ) {
        Map<String, Integer> numberOfCity= new HashMap<>();
        for (int i = 0; i < cities.size(); i++) {
            numberOfCity.put(cities.get(i).getName(), i);
        }
        return numberOfCity;
    }
}
