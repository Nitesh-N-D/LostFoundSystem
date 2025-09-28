package com.college.lostfound.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String details;

    @ManyToOne
    private User claimer;

    @ManyToOne
    private Item item;

    private String status; // PENDING, APPROVED, REJECTED
}
