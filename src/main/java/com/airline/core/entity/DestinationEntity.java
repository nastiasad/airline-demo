package com.airline.core.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "destination")
public class DestinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "airline_destination",
            joinColumns = @JoinColumn(name = "destination_id"),
            inverseJoinColumns = @JoinColumn(name = "airline_id"))
    Set<AirlineEntity> airlines = new HashSet<>();

    public void addAirline(AirlineEntity airlineEntity) {
        if (airlines == null) {
            airlines = new HashSet<>();
        }
        airlines.add(airlineEntity);
    }
}
