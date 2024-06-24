package com.example.Maledetta_TreEst.post;

public class Post {
    private final String delay;
    private final String comment;
    private final String status;
    private final Author author;
    private final String datetime;
    private boolean followingAuthor;

    public Post(String delay, String comment, String status, Author author, boolean followingAuthor, String datetime){
        this.delay = delay;
        this.comment = comment;
        this.status = status;
        this.author = author;
        this.followingAuthor = followingAuthor;
        this.datetime = datetime;
    }

    public String getDelay() {
        return delay;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public String getDatetime() {
        return datetime.substring(0, 16);
    }

    public boolean getFollowingAuthor() {
        return followingAuthor;
    }

    public void setFollowingAuthor(boolean follow) { followingAuthor = follow; }

    public Author getAuthor() {
        return author;
    }

}
