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
    private String preferences;

    public User() {}

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = "123 Dummy Street, Faketown, Imaginaryland, Planet Earth, Solar System, Milky Way Galaxy, Universe";
        this.phoneNumber = "+1234567890";
        this.bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(30) +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ".repeat(30);
        this.hobbies = List.of(
                "reading", "gaming", "coding", "traveling", "photography", "painting", "hiking", "cycling",
                "cooking", "blogging", "knitting", "woodworking", "birdwatching", "gardening", "fishing"
        );
        this.socialLinks = Map.ofEntries(
                Map.entry("facebook", "https://facebook.com/dummyuser"),
                Map.entry("twitter", "https://twitter.com/dummyuser"),
                Map.entry("linkedin", "https://linkedin.com/in/dummyuser"),
                Map.entry("instagram", "https://instagram.com/dummyuser"),
                Map.entry("github", "https://github.com/dummyuser"),
                Map.entry("youtube", "https://youtube.com/dummyuser"),
                Map.entry("tiktok", "https://tiktok.com/@dummyuser")
        );
        this.profilePictureBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAUA" + "A".repeat(1500); // Simulated large base64 string
        this.notes = ("This is a long note field meant to simulate a larger payload. ".repeat(150)) +
                "End of notes.";
        this.tags = List.of(
                "admin", "tester", "beta-user", "premium", "inactive", "early-access", "vip", "moderator",
                "contributor", "reviewer", "developer", "support", "subscriber", "guest", "legacy"
        );
        this.preferences = "{"
                + "\"theme\":\"dark\","
                + "\"notifications\":true,"
                + "\"language\":\"en\","
                + "\"timezone\":\"Asia/Kolkata\","
                + "\"dashboard\":{\"layout\":\"grid\",\"widgets\":[\"weather\",\"news\",\"stocks\",\"calendar\"]},"
                + "\"privacy\":{\"tracking\":false,\"adPersonalization\":true},"
                + "\"shortcuts\":[\"ctrl+s\",\"ctrl+z\",\"ctrl+shift+p\"],"
                + "\"bookmarks\":["
                + "{\"title\":\"Home\",\"url\":\"https://example.com/home\"},"
                + "{\"title\":\"Profile\",\"url\":\"https://example.com/profile\"},"
                + "{\"title\":\"Settings\",\"url\":\"https://example.com/settings\"}"
                + "]"
                + "}";
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

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
}