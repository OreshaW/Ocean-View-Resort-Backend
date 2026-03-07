package com.example.Ocean_View_Resort.controller;

import com.example.Ocean_View_Resort.entity.ContactMessage;
import com.example.Ocean_View_Resort.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ContactMessage message) {
        ContactMessage saved = contactService.saveMessage(message);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ContactMessage>> getMessages() {
        return ResponseEntity.ok(contactService.getAllMessages());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        contactService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }
}