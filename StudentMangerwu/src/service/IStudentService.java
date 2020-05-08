package service;

import domain.Page;
import domain.Student;
import domain.Teacher;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/25 - 18:15
 */
public interface IStudentService {
    public boolean addStudent(Student student);
    public int getStudentListTotal(Student student);
    public List<Student> getStudentList(Student student, Page page);
    public Student getStudent(int id);
    public boolean editStudent(Student student);
    public boolean editStudentPhoto(Student student);
    public boolean deleteStudent(String id);
    public Student studentLogin(String sn, String password);
    public boolean editPassword(Student student, String newPassword);
}
