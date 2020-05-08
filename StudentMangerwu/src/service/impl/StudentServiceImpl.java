package service.impl;

import dao.IStudentDao;
import dao.impl.StudentDaoImpl;
import domain.Page;
import domain.Student;
import service.IStudentService;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/25 - 18:16
 */
public class StudentServiceImpl implements IStudentService {
    private StudentDaoImpl stu=new StudentDaoImpl();
    @Override
    public boolean addStudent(Student student) {
        return stu.addStudent(student);
    }

    @Override
    public int getStudentListTotal(Student student) {
        return stu.getStudentListTotal(student);
    }

    @Override
    public List<Student> getStudentList(Student student, Page page) {
        return stu.getStudentList(student,page);
    }

    @Override
    public Student getStudent(int id) {
        return stu.getStudent(id);
    }

    @Override
    public boolean editStudent(Student student) {
        return stu.editStudent(student);
    }

    @Override
    public boolean editStudentPhoto(Student student) {
        return stu.editStudentPhoto(student);
    }

    @Override
    public boolean deleteStudent(String id) {
        return stu.deleteStudent(id);
    }

    @Override
    public Student studentLogin(String sn, String password) {
        return stu.studentLogin(sn,password);
    }

    @Override
    public boolean editPassword(Student student, String newPassword) {
        return stu.editPassword(student,newPassword);
    }
}
