package com.example.Ocean_View_Resort.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private String id;

    @NotBlank(message = "Room name is required")
    private String name;

    @NotBlank(message = "Room type is required")
    private String type;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @Column(length = 2000)
    @NotBlank(message = "Description is required")
    private String description;

    private String image;

    // Room Amenities
    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    // Room Gallery Images
    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private List<String> images;

    @NotNull(message = "Max guests required")
    @Positive(message = "Max guests must be greater than 0")
    private Integer maxGuests;

    private String size;

    private Boolean available;

    // Relationship with bookings
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;
}
