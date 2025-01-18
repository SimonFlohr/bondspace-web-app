package com.bondspace.service;

import com.bondspace.domain.model.Memory;

import java.util.List;

public interface MemoryService {

    // Create a new memory
    Memory createMemory(String type, String name, String description, List<String> tags, String textContent, String mediaContent);

    // Retrieve a memory by its ID
    Memory getMemoryById(int memoryId);

    // Retrieve all memories from a space
    List<Memory> getAllMemoriesFromASpace(int spaceId);

    // Add tags to a memory
    boolean addTagsToMemory(int memoryId, List<String> tags);

    // Remove tag from a memory
    boolean removeTagFromMemory(int memoryId, String tag);

    // Add media content to a memory
    boolean addMediaContentToMemory(int memoryId, String mediaContent);

    // Replace media content for a memory
    boolean replaceMediaContentInMemory(int memoryId, String newMediaContent);

    // Remove media content from a memory
    boolean removeMediaContentFromMemory(int memoryId, String mediaContent);

    // Update the details of an existing memory
    boolean updateMemory(String type, String name, String description, String textContent);

    // Delete an existing memory
    boolean deleteMemory(int memoryId);

    // Filter a space's memories by tag
    List<Memory> filterMemoriesByTag(int spaceId, String tag);

    // Filter a space's memories by name
    List<Memory> filterMemoriesByName(int spaceId, String name);

}
