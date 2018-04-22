package in.theindianlifter.indianlifter.model;

import java.util.Date;

/**
 * Created by rajatdhamija on 22/04/18.
 */

public class ChatMessage {
    private String message;
    private int messageType;
    private Date date;
    private boolean showDate = true;

    public ChatMessage(String message, int messageType, Date date, boolean showDate) {
        this.message = message;
        this.messageType = messageType;
        this.date = date;
        this.showDate = showDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }
}
