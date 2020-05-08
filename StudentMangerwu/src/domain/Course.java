package domain;

/**
 * @author wu
 * @date 2020/5/1 - 11:16
 */
public class Course {
    private int id;
    private String name;
    private int teacherId;
    private String courseDate;
    private int selectedNum = 0;//已选人数
    private int maxNum = 50;//课程最大选课人数
    private String info;

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", courseDate='" + courseDate + '\'' +
                ", selectedNum=" + selectedNum +
                ", maxNum=" + maxNum +
                ", info='" + info + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public int getSelectedNum() {
        return selectedNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public String getInfo() {
        return info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public void setSelectedNum(int selectedNum) {
        this.selectedNum = selectedNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
