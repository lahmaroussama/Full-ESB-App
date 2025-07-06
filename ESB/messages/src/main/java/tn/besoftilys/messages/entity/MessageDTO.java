package tn.besoftilys.messages.entity;

import java.util.Date;

public class MessageDTO {
    private String contentType;
    private String messageDestination;
    private int messageSize;
    private String body;
    private String transformedMessage;
    private Date comingDate;

    public MessageDTO(String contentType, String messageDestination, int messageSize, String body, Date comingDate) {
        this.contentType = contentType;
        this.messageDestination = messageDestination;
        this.messageSize = messageSize;
        this.body = body;
        this.comingDate = comingDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMessageDestination() {
        return messageDestination;
    }

    public void setMessageDestination(String messageDestination) {
        this.messageDestination = messageDestination;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getComingDate() {
        return comingDate;
    }

    public void setComingDate(Date comingDate) {
        this.comingDate = comingDate;
    }

    public String getTransformedMessage() {
        return transformedMessage;
    }

    public void setTransformedMessage(String transformedMessage) {
        this.transformedMessage = transformedMessage;
    }
}
