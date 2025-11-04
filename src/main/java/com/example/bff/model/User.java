package com.example.bff.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;
    private String name;
    private String email;

    // Additional dummy fields
    private String address;
    private String phoneNumber;
    private String bio;
    private List<String> hobbies;
    private Map<String, String> socialLinks;
    private String profilePictureBase64;
    private String notes;
    private List<String> tags;
    private String preferencesJson;

    public User() {}

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = "123 Dummy Street, Faketown, Imaginaryland";
        this.phoneNumber = "+1234567890";
        this.bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        this.hobbies = List.of("reading", "gaming", "coding", "traveling", "photography");
        this.socialLinks = Map.of(
                "facebook", "https://facebook.com/dummyuser",
                "twitter", "https://twitter.com/dummyuser",
                "linkedin", "https://linkedin.com/in/dummyuser"
        );
        this.profilePictureBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAUA..."; // truncated base64 string
        this.notes = "This is a long note field meant to simulate a larger payload. ".repeat(50);
        this.tags = List.of("admin", "tester", "beta-user", "premium", "inactive");
        this.preferencesJson = "{\"theme\":\"dark\",\"notifications\":true,\"language\":\"en\",\"timezone\":\"Asia/Kolkata\"}";
    }

    // Getters and setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }

    public Map<String, String> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(Map<String, String> socialLinks) { this.socialLinks = socialLinks; }

    public String getProfilePictureBase64() { return profilePictureBase64; }
    public void setProfilePictureBase64(String profilePictureBase64) { this.profilePictureBase64 = profilePictureBase64; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getPreferencesJson() { return preferencesJson; }
    public void setPreferencesJson(String preferencesJson) { this.preferencesJson = preferencesJson; }
}
