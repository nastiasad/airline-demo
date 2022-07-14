package com.airline.core.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    @ManyToOne(optional = false)
    private AirlineEntity airline;

    private BigDecimal price;

    private Double maxDistance;

    @CreationTimestamp
    private LocalDate creationDate;
}
