package com.example.Ocean_View_Resort.service;

import com.example.Ocean_View_Resort.entity.ContactMessage;
import com.example.Ocean_View_Resort.repository.ContactRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public ContactMessage saveMessage(ContactMessage message) {
        return repository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return repository.findAll();
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}