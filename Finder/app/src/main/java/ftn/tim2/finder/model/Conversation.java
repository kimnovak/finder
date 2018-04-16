package ftn.tim2.finder.model;

import java.util.List;

public class Conversation {

    private User sender;
    private User receiver;
    private List<Message> messages;

    public Conversation() {
    }

    public Conversation(User sender, User receiver, List<Message> messages) {
        this.sender = sender;
        this.receiver = receiver;
        this.messages = messages;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiever() {
        return receiver;
    }

    public void setReceiever(User receiever) {
        this.receiver = receiever;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
