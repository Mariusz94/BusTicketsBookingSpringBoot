package pl.mariuszlyszczarz.BusTicketsBookingSpringBoot.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
public class CityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
}
