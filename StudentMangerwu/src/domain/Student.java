package domain;

import java.io.InputStream;

/**
 * @author wu
 * @date 2020/4/25 - 17:45
 */
public class Student {
    private int id;
    private String sn;//学号
    private String name;
    private String password;
    private int class_id;
    private String sex = "男";
    private String mobile;
    private String qq;
    private InputStream photo;//头像

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", class_id=" + class_id +
                ", sex='" + sex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", photo=" + photo +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getClass_id() {
        return class_id;
    }

    public String getSex() {
        return sex;
    }

    public String getMobile() {
        return mobile;
    }

    public String getQq() {
        return qq;
    }

    public InputStream getPhoto() {
        return photo;
    }
}
