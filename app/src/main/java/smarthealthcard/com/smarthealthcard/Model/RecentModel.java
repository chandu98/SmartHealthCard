package smarthealthcard.com.smarthealthcard.Model;

public class RecentModel {
    String userId;
    String name;
    String mobile;
    String email;
    String remarks;
    String date;

    public RecentModel() {
    }

    public RecentModel(String userId, String name, String mobile, String email, String remarks, String date) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.remarks = remarks;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
