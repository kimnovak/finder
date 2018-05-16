package ftn.tim2.finder.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserProfile {

    private String description;
    private String image;
    private Date registrationDate;
    private double rate;
    private List<User> followers;
    private List<User> following;
    private String city;
    private String country;
    private Map<String, Comment> comments;

    public UserProfile() {
    }

    public UserProfile(String description, String image, Date registrationDate, double rate, String city, String country) {
        this.description = description;
        this.image = image;
        this.registrationDate = registrationDate;
        this.rate = rate;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.city = city;
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
    }
}
