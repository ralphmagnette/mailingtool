package be.alfapay.alfaplatform.mailingtool.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ap_mailing")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "sender")
    private String from;

    @Column(name = "receiver")
    private String to;

    @Column(name = "subject")
    private String subject;

    @Column(name = "plainText")
    private String plainText;

    @Column(name = "htmlText")
    private String htmlText;

    @Column(name = "sendDate")
    private LocalDateTime sendDate;

    @Column(name = "opened")
    private boolean isOpened;

    @Column(name = "clicked")
    private boolean isClicked;

    @Column(name = "bounced")
    private boolean isBounced;

    @Column(name = "csvId")
    private Long csvId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "csvId", updatable = false, insertable = false)
    private CSVData csvData;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "attachmentId")
    private List<Attachment> attachments = new ArrayList<>();

    public Mail() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isBounced() {
        return isBounced;
    }

    public void setBounced(boolean bounced) {
        isBounced = bounced;
    }

    public Long getCsvId() {
        return csvId;
    }

    public void setCsvId(Long csvId) {
        this.csvId = csvId;
    }

    public CSVData getCsvData() {
        return csvData;
    }

    public void setCsvData(CSVData csvData) {
        this.csvData = csvData;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
