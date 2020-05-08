package domain;

/**
 * @author wu
 * @date 2020/5/2 - 14:01
 */
public class SelectedCourse {
    private int id;
    private int studentId;
    private int courseId;

    @Override
    public String toString() {
        return "SelectedCourse{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
