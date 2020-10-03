package smarthealthcard.com.smarthealthcard.Model;

public class UploadModel {
    String userId;
    String title;
    String description;
    String docUrl;
    String date;

    public UploadModel() {
    }

    public UploadModel(String userId, String title, String description, String docUrl, String date) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.docUrl = docUrl;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
