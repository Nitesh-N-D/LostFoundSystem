package com.college.lostfound.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 2048)
    private String description;
    private String category;
    private String location;
    private boolean found; // true = found on campus, false = lost (reported lost)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name="item_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ManyToOne
    @JoinColumn(name="reporter_id")
    private User reporter;
}
