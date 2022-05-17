package be.alfapay.alfaplatform.mailingtool.domain;

import javax.persistence.*;

@Entity
@Table(name = "ap_mailing")
public class Mailing {
    @Id
    private String id;

    @Column(name = "article_id")
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
    private Long date;

    @Column(name = "send_date")
    private Long sendDate;

    @Column(name = "open")
    private int open;

    @Column(name = "click")
    private int click;

    @Column(name = "dropped")
    private int dropped;

    @Column(name = "status", columnDefinition = "enum('CREATED','PROCESSING','FINISHED','CANCELLED')")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Mailing() { }

    public Mailing(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
