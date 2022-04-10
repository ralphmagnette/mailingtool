package be.alfapay.alfaplatform.mailingtool.domain;

public class Attachment {
    private byte[] data;
    private String name;
    private String mimeType;

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
