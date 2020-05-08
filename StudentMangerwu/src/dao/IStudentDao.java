package dao;

import domain.Admin;
import domain.Clazz;
import domain.Page;
import domain.Student;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/25 - 17:58
 */
public interface IStudentDao {
    public boolean addStudent(Student student);
    public List<Student> getStudentList(Student student, Page page);
    public int getStudentListTotal(Student student);
    public Student getStudent(int id);
    public boolean editStudent(Student student);
    public boolean editStudentPhoto(Student student);
    public boolean deleteStudent(String id);
    public Student studentLogin(String sn, String password);
    public boolean editPassword(Student student,String newPassword);
}
