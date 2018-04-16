package ftn.tim2.finder.model;

public class Message {

    private String message;
    private User sender;
    private User receiver;
    private long createdAt;

    public Message() {
    }

    public Message(String message, User sender, User receiver, long createdAt) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
