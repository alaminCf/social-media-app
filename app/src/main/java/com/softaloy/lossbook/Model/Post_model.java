package com.softaloy.lossbook.Model;

public class Post_model {

    private String postId, postImage, postedBy, postDescription;
    private long postedAt;
    private int postLove;
    private int commentCount;

    public Post_model(String postId, String postImage, String postedBy, String postDescription, long postedAt) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }

    public Post_model() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getPostLove() {
        return postLove;
    }

    public void setPostLove(int postLove) {
        this.postLove = postLove;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
