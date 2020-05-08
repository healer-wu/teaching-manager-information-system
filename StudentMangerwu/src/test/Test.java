package test;

import dao.impl.AdminDaoImpl;
import dao.impl.ClazzDaoImpl;
import dao.impl.CourseDaoImpl;
import dao.impl.SelectedCourseDao;
import domain.*;
import service.impl.ClazzServiceImpl;
import service.impl.StudentServiceImpl;
import servlet.SelectedCourseServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * @author wu
 * @date 2020/4/19 - 20:00
 */
public class Test {
    public static void main(String[] args) {
        SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
        List<Integer> selectedCourseId = selectedCourseDao.getSelectedCourseId("1,2,3,4");

        System.out.println("selectedCourseId = " + selectedCourseId);

    }
}
