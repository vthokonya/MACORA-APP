package zw.co.tayasoft.data;

import jakarta.persistence.Entity;

@Entity
public class Ward extends AbstractEntity {

    private String name;
    private String councellor;
    private String mobileNumber1;
    private String mobileNumber2;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCouncellor() {
        return councellor;
    }
    public void setCouncellor(String councellor) {
        this.councellor = councellor;
    }
    public String getMobileNumber1() {
        return mobileNumber1;
    }
    public void setMobileNumber1(String mobileNumber1) {
        this.mobileNumber1 = mobileNumber1;
    }
    public String getMobileNumber2() {
        return mobileNumber2;
    }
    public void setMobileNumber2(String mobileNumber2) {
        this.mobileNumber2 = mobileNumber2;
    }

}
