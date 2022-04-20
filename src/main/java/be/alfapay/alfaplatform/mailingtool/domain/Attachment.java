package be.alfapay.alfaplatform.mailingtool.domain;

import javax.persistence.*;

@Entity
@Table(name = "ap_attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "name")
    private String name;

    @Column(name = "mimeType")
    private String mimeType;

    public Attachment() {

    }

    public Attachment(byte[] data, String name, String mimeType) {
        this.data = data;
        this.name = name;
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
