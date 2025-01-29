package com.bondspace.controller;

import com.bondspace.domain.model.Memory;
import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.User;
import com.bondspace.repository.MemoryRepository;
import com.bondspace.repository.SpaceRepository;
import com.bondspace.repository.UserRepository;
import com.bondspace.util.SessionUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createMemory(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = sessionUtil.getLoggedInUserId();
            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User not authenticated"));
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Space space = spaceRepository.findById(Integer.parseInt(request.get("spaceId").toString()))
                    .orElseThrow(() -> new IllegalArgumentException("Space not found"));

            Memory memory = new Memory(
                    "TEXT",
                    (String) request.get("name"),
                    user,
                    space
            );

            memory.setTextContent((String) request.get("textContent"));

            List<String> tags = (List<String>) request.get("tags");
            if (tags != null && !tags.isEmpty()) {
                memory.setTags(tags);
            }

            // Add memory to space's memories set
            space.getMemories().add(memory);

            // Save both entities
            memoryRepository.save(memory);
            spaceRepository.save(space);

            return ResponseEntity.ok(Map.of("message", "Memory created successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to create memory: " + e.getMessage()));
        }
    }

    @GetMapping("/space/{spaceId}/tags")
    public ResponseEntity<?> getSpaceTags(@PathVariable int spaceId) {
        try {
            Space space = spaceRepository.findById(spaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Space not found"));

            Set<String> uniqueTags = space.getMemories().stream()
                    .flatMap(memory -> memory.getTags().stream())
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(uniqueTags);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch tags: " + e.getMessage()));
        }
    }

    @GetMapping("/debug/{spaceId}")
    @Transactional
    public ResponseEntity<?> debugMemories(@PathVariable int spaceId) {
        try {
            Space space = spaceRepository.findById(spaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Space not found"));

            List<Memory> dbMemories = memoryRepository.findBySpaceId(spaceId);
            return ResponseEntity.ok(dbMemories);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Debug failed: " + e.getMessage()));
        }
    }
}