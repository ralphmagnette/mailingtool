package be.alfapay.alfaplatform.mailingtool.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ap_mailing")
public class Mailing {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "article_id", nullable = false)
    private Integer articleId;

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

    @Column(name = "dropped")
    private int dropped;

    public Mailing() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
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

    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
        this.dropped = dropped;
    }
}
