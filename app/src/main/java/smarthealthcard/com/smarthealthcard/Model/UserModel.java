package smarthealthcard.com.smarthealthcard.Model;

public class UserModel {
    public String name;
    public String mobile;
    public String email;
    public String photoUrl;

    public UserModel() {
    }

    public UserModel(String name, String mobile, String email, String photoUrl) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.photoUrl = photoUrl;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
