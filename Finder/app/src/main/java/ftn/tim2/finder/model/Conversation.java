package ftn.tim2.finder.model;

import java.util.Map;

public class Conversation {

    private User participant;
    private Map<String, String> messages;
    private String lastMessage;

    public Conversation() {
    }

    public Conversation(User participant, String lastMessage) {
        this.participant = participant;
        this.lastMessage = lastMessage;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
