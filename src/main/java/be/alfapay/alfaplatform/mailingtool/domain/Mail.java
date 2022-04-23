package be.alfapay.alfaplatform.mailingtool.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ap_mailing")
public class Mail {
    @Column(name = "article_id")
    private Integer articleId;

    @Id
    @Column(name = "mailing_id")
    private UUID mailingId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mailing_id")
    private MailSendTo sendTo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "csv")
    private String csv;

    @Column(name = "template")
    private String template;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @Column(name = "open")
    private int open;

    @Column(name = "click")
    private int click;

    @Column(name = "bounced")
    private int bounced;

    public Mail() {

    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public UUID getMailingId() {
        return mailingId;
    }

    public void setMailingId(UUID mailingId) {
        this.mailingId = mailingId;
    }

    public MailSendTo getSendTo() {
        return sendTo;
    }

    public void setSendTo(MailSendTo sendTo) {
        this.sendTo = sendTo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getBounced() {
        return bounced;
    }

    public void setBounced(int bounced) {
        this.bounced = bounced;
    }
}
