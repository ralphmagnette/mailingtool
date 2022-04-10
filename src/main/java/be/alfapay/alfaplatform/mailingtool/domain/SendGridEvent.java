package be.alfapay.alfaplatform.mailingtool.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SendGridEvent {
    @JsonProperty("email")
    private String email;
    @JsonProperty("event")
    private String eventType;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("sg_content_type")
    private String sgContentType;
    @JsonProperty("sg_event_id")
    private String sgEventId;
    @JsonProperty("sg_machine_open")
    private String sgMachineOpen;
    @JsonProperty("url")
    private String url;
    @JsonProperty("sg_message_id")
    private String sgMessageId;
    @JsonProperty("timestamp")
    private LocalDateTime timeStamp;
    @JsonProperty("useragent")
    private String userAgent;

    public SendGridEvent() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSgContentType() {
        return sgContentType;
    }

    public void setSgContentType(String sgContentType) {
        this.sgContentType = sgContentType;
    }

    public String getSgEventId() {
        return sgEventId;
    }

    public void setSgEventId(String sgEventId) {
        this.sgEventId = sgEventId;
    }

    public String getSgMachineOpen() {
        return sgMachineOpen;
    }

    public void setSgMachineOpen(String sgMachineOpen) {
        this.sgMachineOpen = sgMachineOpen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSgMessageId() {
        return sgMessageId;
    }

    public void setSgMessageId(String sgMessageId) {
        this.sgMessageId = sgMessageId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "email: " + email + "\n" +
                "event_type: " + eventType + "\n" +
                "ip: " + ip + "\n" +
                "sg_content_type: " + sgContentType + "\n" +
                "sg_ event_id: " + sgEventId + "\n" +
                "sg_machine_open: " + sgMachineOpen + "\n" +
                "url: " + url + "\n" +
                "sg_message_id: " + sgMessageId + "\n" +
                "timestamp: " + timeStamp + "\n" +
                "useragent: " + userAgent;
    }
}
