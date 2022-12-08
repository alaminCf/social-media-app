package com.softaloy.lossbook.Model;

public class Users {

    private String name, email, password, profassion, profileImage, coverImage;
    private String userId;
    private int followerCount;

    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profassion = profassion;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
    }

    public Users() {
    }

    public Users(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfassion() {
        return profassion;
    }

    public void setProfassion(String profassion) {
        this.profassion = profassion;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
