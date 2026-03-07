package com.example.Ocean_View_Resort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Ocean_View_Resort.entity.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    @Query(value = "SELECT id FROM rooms WHERE id LIKE 'ROOM-%' ORDER BY CAST(SUBSTRING(id, 6) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLatestRoomId();
}
