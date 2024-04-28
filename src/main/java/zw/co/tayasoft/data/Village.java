package zw.co.tayasoft.data;

import jakarta.persistence.Entity;

@Entity
public class Village extends AbstractEntity {

    private String name;
    private String headman;
    private String headmanMobile1;
    private String headmanMobile2;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHeadman() {
        return headman;
    }
    public void setHeadman(String headman) {
        this.headman = headman;
    }
    public String getHeadmanMobile1() {
        return headmanMobile1;
    }
    public void setHeadmanMobile1(String headmanMobile1) {
        this.headmanMobile1 = headmanMobile1;
    }
    public String getHeadmanMobile2() {
        return headmanMobile2;
    }
    public void setHeadmanMobile2(String headmanMobile2) {
        this.headmanMobile2 = headmanMobile2;
    }

}
