package domain;

/**
 * @author wu
 * @date 2020/5/6 - 19:44
 */
public class Score {
    private int id;
    private int studentId;
    private int courseId;
    private double score;
    private String remark;

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", score=" + score +
                ", remark='" + remark + '\'' +
                '}';
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

    public void setScore(double score) {
        this.score = score;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public double getScore() {
        return score;
    }

    public String getRemark() {
        return remark;
    }
}
