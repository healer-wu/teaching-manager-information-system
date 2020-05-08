package service.impl;

import dao.impl.TeacherDaoImpl;
import domain.Page;
import domain.Teacher;
import service.ITeacherService;
import java.util.List;

/**
 * @author wu
 * @date 2020/4/29 - 8:47
 */
public class TeacherServiceImpl implements ITeacherService {
    private TeacherDaoImpl teacherDao=new TeacherDaoImpl();
    @Override
    public boolean addTeacher(Teacher teacher) {
        return teacherDao.addTeacher(teacher);
    }

    @Override
    public List<Teacher> getTeacherList(Teacher teacher, Page page) {
        return teacherDao.getTeacherList(teacher,page);
    }

    @Override
    public int getTeacherListTotal(Teacher teacher) {
        return teacherDao.getTeacherListTotal(teacher);
    }

    @Override
    public boolean editTeacher(Teacher teacher) {
        return teacherDao.editTeacher(teacher);
    }

    @Override
    public boolean deleteTeacher(String id) {
        return teacherDao.deleteTeacher(id);
    }

    @Override
    public Teacher getTeacher(int id) {
        return teacherDao.getTeacher(id);
    }

    @Override
    public boolean editTeacherPhoto(Teacher teacher) {
        return teacherDao.editTeacherPhoto(teacher);
    }

    @Override
    public Teacher teacherLogin(String sn, String password) {
        return teacherDao.teacherLogin(sn,password);
    }

    @Override
    public boolean editPassword(Teacher teacher, String newPassword) {
        return teacherDao.editPassword(teacher,newPassword);
    }
}
