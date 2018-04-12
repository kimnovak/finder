package ftn.tim2.finder.model;

import java.util.Date;

public class Comment {

    private String content;
    private Date dateCreated;
    private User user;

    public Comment() {

    }

    public Comment(String content, Date dateCreated, User user) {
        this.content = content;
        this.dateCreated = dateCreated;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
