package rprs.business.refer.referbusiness;

public class BusinessModel {

    String id,name,mobile,status;

    public BusinessModel(String id, String name, String mobile, String status) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
