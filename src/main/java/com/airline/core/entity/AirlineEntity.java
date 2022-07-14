package com.airline.core.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airline")
public class AirlineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name="base_location_id")
    private DestinationEntity baseLocation;

    @ManyToMany(mappedBy = "airlines")
    private Set<DestinationEntity> destinations = new HashSet<>();

    public void addToBalance(BigDecimal amountToAdd) {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        balance = balance.add(amountToAdd);
    }
}
