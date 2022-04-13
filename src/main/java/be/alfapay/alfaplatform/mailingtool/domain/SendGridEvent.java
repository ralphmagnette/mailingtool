package be.alfapay.alfaplatform.mailingtool.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SendGridEvent {
    @JsonProperty("email")
    private String email;
    @JsonProperty("timestamp")
    private LocalDateTime timeStamp;
    @JsonProperty("event")
    private String eventType;
    @JsonProperty("smtp-id")
    private String smtpId;
    @JsonProperty("useragent")
    private String userAgent;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("sg_event_id")
    private String sgEventId;
    @JsonProperty("sg_message_id")
    private String sgMessageId;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("status")
    private String status;
    @JsonProperty("response")
    private String response;
    @JsonProperty("tls")
    private boolean tls;
    @JsonProperty("url")
    private String url;
    @JsonProperty("url_offset")
    private String url_offset;
    @JsonProperty("attempt")
    private int attempt;
    @JsonProperty("category")
    private String category;
    @JsonProperty("type")
    private String type;
    @JsonProperty("sg_machine_open")
    private String sgMachineOpen;

    public SendGridEvent() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSmtpId() {
        return smtpId;
    }

    public void setSmtpId(String smtpId) {
        this.smtpId = smtpId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSgEventId() {
        return sgEventId;
    }

    public void setSgEventId(String sgEventId) {
        this.sgEventId = sgEventId;
    }

    public String getSgMessageId() {
        return sgMessageId;
    }

    public void setSgMessageId(String sgMessageId) {
        this.sgMessageId = sgMessageId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl_offset() {
        return url_offset;
    }

    public void setUrl_offset(String url_offset) {
        this.url_offset = url_offset;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSgMachineOpen() {
        return sgMachineOpen;
    }

    public void setSgMachineOpen(String sgMachineOpen) {
        this.sgMachineOpen = sgMachineOpen;
    }

    @Override
    public String toString() {
        return "email: " + email + "\n" +
                "timestamp: " + timeStamp + "\n" +
                "event_type: " + eventType + "\n" +
                "smtp-id: " + smtpId + "\n" +
                "useragent: " + userAgent + "\n" +
                "ip: " + ip + "\n" +
                "sg_ event_id: " + sgEventId + "\n" +
                "sg_message_id: " + sgMessageId + "\n" +
                "reason: " + reason + "\n" +
                "status: " + status + "\n" +
                "response: " + response + "\n" +
                "tls: " + tls + "\n" +
                "url: " + url + "\n" +
                "url_offset: " + url_offset + "\n" +
                "attempt: " + attempt + "\n" +
                "category: " + category + "\n" +
                "type: " + type + "\n" +
                "sg_machine_open: " + sgMachineOpen;
    }
}