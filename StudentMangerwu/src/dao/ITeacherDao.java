package dao;

import domain.Admin;
import domain.Page;
import domain.Student;
import domain.Teacher;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/29 - 8:42
 */
public interface ITeacherDao {
    public boolean addTeacher(Teacher teacher);
    public List<Teacher> getTeacherList(Teacher teacher, Page page);
    public int getTeacherListTotal(Teacher teacher);
    public boolean editTeacher(Teacher teacher);
    public boolean deleteTeacher(String id);
    public Teacher getTeacher(int id);
    public boolean editTeacherPhoto(Teacher teacher);
    public Teacher teacherLogin(String sn, String password);
    public boolean editPassword(Teacher teacher, String newPassword);
}
