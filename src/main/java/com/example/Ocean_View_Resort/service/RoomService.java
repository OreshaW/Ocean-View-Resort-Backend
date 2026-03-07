package com.example.Ocean_View_Resort.service;

import com.example.Ocean_View_Resort.entity.Room;
import com.example.Ocean_View_Resort.exception.ResourceNotFoundException;
import com.example.Ocean_View_Resort.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RoomService {

    private static final Pattern ROOM_ID_PATTERN = Pattern.compile("^ROOM-(\\d+)$");
    private static final String UPLOAD_DIR = "uploads/rooms/";

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room saveRoom(Room room, MultipartFile imageFile, List<MultipartFile> galleryFiles) {
        room.setId(generateNextRoomId());

        if (imageFile != null && !imageFile.isEmpty()) {
            room.setImage(saveFile(imageFile));
        }

        if (galleryFiles != null) {
            List<String> galleryUrls = new ArrayList<>();
            for (MultipartFile f : galleryFiles) {
                if (!f.isEmpty())
                    galleryUrls.add(saveFile(f));
            }
            room.setImages(galleryUrls);
        }

        return roomRepository.save(room);
    }

    public Room updateRoom(Room room, MultipartFile imageFile, String existingImage,
            List<String> existingImages, List<MultipartFile> galleryFiles) {
        // Handle main image
        if (imageFile != null && !imageFile.isEmpty()) {
            room.setImage(saveFile(imageFile));
        } else {
            room.setImage(existingImage);
        }

        // Handle gallery
        List<String> gallery = new ArrayList<>();
        if (existingImages != null)
            gallery.addAll(existingImages);
        if (galleryFiles != null) {
            for (MultipartFile f : galleryFiles) {
                if (!f.isEmpty())
                    gallery.add(saveFile(f));
            }
        }
        room.setImages(gallery);

        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    public void deleteRoom(String id) {
        roomRepository.deleteById(id);
    }

    private String saveFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/" + UPLOAD_DIR + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    private String generateNextRoomId() {
        String latestId = roomRepository.findLatestRoomId().orElse(null);
        int nextNumber = 1;

        if (latestId != null) {
            Matcher matcher = ROOM_ID_PATTERN.matcher(latestId);
            if (matcher.matches()) {
                nextNumber = Integer.parseInt(matcher.group(1)) + 1;
            }
        }

        return String.format("ROOM-%03d", nextNumber);
    }
}