package be.alfapay.alfaplatform.mailingtool.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import java.util.UUID;

public class SendGridEvent {
    @JsonProperty("email")
    private String email;
    @JsonProperty("mailing_id")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID mailingId;
    @JsonProperty("event")
    private String eventType;
    @JsonProperty("sg_message_id")
    private String sgMessageId;
    @JsonProperty("url")
    private String url;

    public SendGridEvent() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getMailingId() {
        return mailingId;
    }

    public void setMailingId(UUID mailingId) {
        this.mailingId = mailingId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSgMessageId() {
        return sgMessageId;
    }

    public void setSgMessageId(String sgMessageId) {
        this.sgMessageId = sgMessageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}