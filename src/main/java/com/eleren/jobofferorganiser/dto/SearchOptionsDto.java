package com.eleren.jobofferorganiser.dto;

public class SearchOptionsDto {

    private String title;
    private String location;
    private String programmingLanguage;
    private String experienceLevel;

    public SearchOptionsDto() {
    }

    public SearchOptionsDto(String title, String location, String programmingLanguage, String experienceLevel) {
        this.title = title;
        this.location = location;
        this.programmingLanguage = programmingLanguage;
        this.experienceLevel = experienceLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
}
