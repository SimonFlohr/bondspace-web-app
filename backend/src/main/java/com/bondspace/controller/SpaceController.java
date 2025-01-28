package com.bondspace.controller;

import com.bondspace.domain.dto.CreateSpaceRequestDTO;
import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;
import com.bondspace.repository.SpaceRepository;
import com.bondspace.repository.UserRepository;
import com.bondspace.repository.UserSpaceRepository;
import com.bondspace.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createSpace(@RequestBody CreateSpaceRequestDTO request) {
        // Get the current user's ID from the session
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            // Create and save the new space
            Space space = new Space(request.getSpaceName(), request.getSpaceDescription());
            space = spaceRepository.save(space);

            // Create and save the UserSpace relationship
            UserSpace userSpace = new UserSpace(
                    SpaceUserRole.OWNER,
                    userRepository.findById(userId).orElseThrow(),
                    space
            );
            userSpaceRepository.save(userSpace);

            return ResponseEntity.ok(Map.of("message", "Space created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to create space: " + e.getMessage()));
        }
    }

    @GetMapping("/user-spaces")
    public ResponseEntity<?> getUserSpaces() {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            List<UserSpace> userSpaces = userSpaceRepository.findAllByUserId(userId);

            // Create a simplified response object with only the data we need
            List<Map<String, Object>> spacesResponse = userSpaces.stream()
                    .map(userSpace -> {
                        Space space = userSpace.getSpace();
                        Map<String, Object> spaceData = new HashMap<>();
                        spaceData.put("id", space.getId());
                        spaceData.put("spaceName", space.getSpaceName());
                        spaceData.put("spaceDescription", space.getSpaceDescription());
                        return spaceData;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(spacesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch spaces: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}")
    public ResponseEntity<?> getSpaceDetails(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            Space space = spaceRepository.findByIdSimple(spaceId);
            if (space == null) {
                return ResponseEntity.notFound().build();
            }

            // Create a simplified response object
            Map<String, Object> spaceDetails = new HashMap<>();
            spaceDetails.put("id", space.getId());
            spaceDetails.put("spaceName", space.getSpaceName());
            spaceDetails.put("spaceDescription", space.getSpaceDescription());
            spaceDetails.put("createdAt", space.getCreatedAt());

            return ResponseEntity.ok(spaceDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch space details: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}/members")
    public ResponseEntity<?> getSpaceMembers(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            List<UserSpace> userSpaces = userSpaceRepository.findAllBySpaceId(spaceId);
            List<Map<String, Object>> members = userSpaces.stream()
                    .map(userSpace -> {
                        Map<String, Object> member = new HashMap<>();
                        member.put("firstName", userSpace.getUser().getFirstName());
                        member.put("lastName", userSpace.getUser().getLastName());
                        member.put("role", userSpace.getUserRole().toString());
                        return member;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch members: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}/memories")
    public ResponseEntity<?> getSpaceMemories(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            Space space = spaceRepository.findById(spaceId).orElseThrow();
            return ResponseEntity.ok(space.getMemories());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch memories: " + e.getMessage()));
        }
    }
}