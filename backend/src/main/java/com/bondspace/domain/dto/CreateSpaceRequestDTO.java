package com.bondspace.domain.dto;

public class CreateSpaceRequestDTO {
    private String spaceName;
    private String spaceDescription;

    // GETTERS AND SETTERS
    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }
}