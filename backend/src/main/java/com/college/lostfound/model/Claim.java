package com.college.lostfound.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Claim {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne @JoinColumn(name="claimer_id")
    private User claimer;

    private String status; // PENDING, APPROVED, REJECTED
    private String note;
    private LocalDateTime createdAt;
}
