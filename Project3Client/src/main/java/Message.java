import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;

    String senderUsername;
    String recipientUsername;
    String userMessage;
    ArrayList<String> clientNames;
    MessageType type;

    //class to assign public or private message
    //public = true; private = false
    Boolean publicMessage;


    //default constructor for just name checking
    public Message(String senderUsername, MessageType type){
        this.senderUsername = senderUsername;
        this.userMessage = "";
        this.publicMessage = publicMessage;
        this.type = type;
    }

    //constructor for server messages
    public Message(String userMessage, boolean publicMessage){
        this.userMessage = userMessage;
        this.publicMessage = publicMessage;
    }

    //constructor
    public Message(String senderUsername, String userMessage, boolean publicMessage, MessageType type){
        this.senderUsername = senderUsername;
        this.userMessage = userMessage;
        this.publicMessage = publicMessage;
        this.type = type;
    }

    //constructor for private message
    public Message(String senderUsername, String recipientUsername, String userMessage){
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.userMessage = userMessage;
        this.publicMessage = false;
    }
    // Getters and setters
    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getMessageContent() {
        return userMessage;
    }

    public void setMessageContent(String userMessage) {
        this.userMessage = userMessage;
    }

    public Boolean getpublicMessage() {
        return publicMessage;
    }

    public void setpublicMessage(Boolean publicMessage) {
        this.publicMessage = publicMessage;
    }
}
