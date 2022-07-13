package com.airline.core.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aircraft")
public class AircraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "airline_id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    private AirlineEntity airline;

    private BigDecimal price;

    private Double maxDistance;
}
