package domain;

/**
 * @author wu
 * @date 2020/5/4 - 17:49
 * 签到考勤实体类
 */
public class Attendance {
    private int id;
    private int courseId;
    private int studentId;
    private String type;
    private String date;

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", studentId=" + studentId +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
