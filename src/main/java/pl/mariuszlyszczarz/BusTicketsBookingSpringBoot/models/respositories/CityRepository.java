package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models.CityModel;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<CityModel, Integer> {
    List<CityModel> findAll();
}
