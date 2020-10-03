package smarthealthcard.com.smarthealthcard.Model;

public class HistoryModel {
    String userId;
    String age;
    String weight;
    String bloodGroup;
    String disease;
    String allergy;
    String date;

    public HistoryModel() {
    }

    public HistoryModel(String userId, String age, String weight, String bloodGroup, String disease, String allergy, String date) {
        this.userId = userId;
        this.age = age;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
        this.disease = disease;
        this.allergy = allergy;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
