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
import org.springframework.http.HttpStatus;
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

            // Save memory to get its ID
            memory = memoryRepository.save(memory);

            // Update space's memoryIds array
            Integer[] currentMemories = space.getMemoryIds();
            Integer[] newMemories;

            if (currentMemories == null) {
                newMemories = new Integer[]{memory.getId()};
            } else {
                newMemories = Arrays.copyOf(currentMemories, currentMemories.length + 1);
                newMemories[newMemories.length - 1] = memory.getId();
            }

            space.setMemoryIds(newMemories);
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

    @DeleteMapping("/{memoryId}")
    @Transactional
    public ResponseEntity<?> deleteMemory(@PathVariable int memoryId) {
        try {
            Integer userId = sessionUtil.getLoggedInUserId();
            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User not authenticated"));
            }

            Memory memory = memoryRepository.findById(memoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Memory not found"));

            if (memory.getUploadedBy().getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Only the owner can delete a memory"));
            }

            // Get the space and update its memory array first
            Space space = memory.getSpace();
            Set<Memory> updatedMemories = space.getMemories();
            updatedMemories.remove(memory);
            space.setMemories(updatedMemories);

            // Update memoryIds array
            Integer[] currentIds = space.getMemoryIds();
            if (currentIds != null) {
                Integer[] newIds = Arrays.stream(currentIds)
                        .filter(id -> id != memoryId)
                        .toArray(Integer[]::new);
                space.setMemoryIds(newIds);
            }

            spaceRepository.save(space);
            memoryRepository.delete(memory);

            return ResponseEntity.ok(Map.of("message", "Memory deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to delete memory: " + e.getMessage()));
        }
    }

    @PutMapping("/{memoryId}")
    @Transactional
    public ResponseEntity<?> updateMemory(
            @PathVariable int memoryId,
            @RequestBody Map<String, Object> request) {
        try {
            Integer userId = sessionUtil.getLoggedInUserId();
            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User not authenticated"));
            }

            // Find the memory
            Memory memory = memoryRepository.findById(memoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Memory not found"));

            // Check if the user is the owner
            if (memory.getUploadedBy().getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Only the owner can edit a memory"));
            }

            // Update the memory fields
            memory.setName((String) request.get("name"));
            memory.setTextContent((String) request.get("textContent"));

            // Update tags
            @SuppressWarnings("unchecked")
            List<String> tags = (List<String>) request.get("tags");
            if (tags != null && !tags.isEmpty()) {
                memory.setTags(tags);
            }

            // Save the updated memory
            memoryRepository.save(memory);

            return ResponseEntity.ok(Map.of("message", "Memory updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to update memory: " + e.getMessage()));
        }
    }

    @GetMapping("/{memoryId}")
    public ResponseEntity<?> getMemory(@PathVariable int memoryId) {
        try {
            Memory memory = memoryRepository.findById(memoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Memory not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("id", memory.getId());
            response.put("name", memory.getName());
            response.put("textContent", memory.getTextContent());
            response.put("tags", memory.getTags());
            response.put("type", memory.getType());
            response.put("createdAt", memory.getCreatedAt());
            response.put("updatedAt", memory.getUpdatedAt());

            Map<String, String> uploadedBy = new HashMap<>();
            uploadedBy.put("firstName", memory.getUploadedBy().getFirstName());
            response.put("uploadedBy", uploadedBy);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch memory: " + e.getMessage()));
        }
    }
}