package com.example.Ocean_View_Resort.controller;

import com.example.Ocean_View_Resort.entity.Room;
import com.example.Ocean_View_Resort.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createRoom(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam("maxGuests") Integer maxGuests,
            @RequestParam("available") Boolean available,
            @RequestParam(value = "amenities", required = false) List<String> amenities,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "galleryFiles", required = false) List<MultipartFile> galleryFiles) {
        Room room = new Room();
        room.setName(name);
        room.setType(type);
        room.setPrice(price);
        room.setDescription(description);
        room.setSize(size);
        room.setMaxGuests(maxGuests);
        room.setAvailable(available);
        room.setAmenities(amenities);

        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Main image is required");
        }

        Room savedRoom = roomService.saveRoom(room, imageFile, galleryFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateRoom(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam("maxGuests") Integer maxGuests,
            @RequestParam("available") Boolean available,
            @RequestParam(value = "amenities", required = false) List<String> amenities,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "existingImage", required = false) String existingImage,
            @RequestParam(value = "existingImages", required = false) List<String> existingImages,
            @RequestParam(value = "galleryFiles", required = false) List<MultipartFile> galleryFiles) {

        Room room = roomService.getRoomById(id);
        room.setName(name);
        room.setType(type);
        room.setPrice(price);
        room.setDescription(description);
        room.setSize(size);
        room.setMaxGuests(maxGuests);
        room.setAvailable(available);
        room.setAmenities(amenities);

        // Call service to handle update while keeping existing images
        Room updatedRoom = roomService.updateRoom(room, imageFile, existingImage, existingImages, galleryFiles);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable String id) {
        return roomService.getRoomById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}