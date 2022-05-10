package be.alfapay.alfaplatform.mailingtool.resources;

import be.alfapay.alfaplatform.mailingtool.domain.Attachment;
import be.alfapay.alfaplatform.mailingtool.domain.MailSendTo;

import java.util.ArrayList;
import java.util.List;

public class MailSendToDTO {
    private String mailingId;
    private Integer articleId;
    private String subject;
    private String email;
    private String firstName;
    private String lastName;
    private int amount;
    private List<Attachment> attachments = new ArrayList<>();
    private String error;

    public MailSendToDTO() {

    }

    public String getMailingId() {
        return mailingId;
    }

    public void setMailingId(String mailingId) {
        this.mailingId = mailingId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
