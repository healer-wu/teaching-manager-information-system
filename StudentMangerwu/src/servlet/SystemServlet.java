package servlet;

import domain.Admin;
import domain.Student;
import domain.Teacher;
import service.impl.AdminServiceImpl;
import service.impl.StudentServiceImpl;
import service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wu
 * @date 2020/4/20 - 20:58
 */
@WebServlet("/systemservlet")
public class SystemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("toPersonalView")){
            personalView(req,resp);
            return;
        }else if("editPassword".equals(method)){
            editPassword(req,resp);
            return;
        }
        req.getRequestDispatcher("system.jsp").forward(req,resp);
    }

    private void editPassword(HttpServletRequest req, HttpServletResponse resp) {
        AdminServiceImpl adminService = new AdminServiceImpl();
        String password = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");
        resp.setCharacterEncoding("UTF-8");
        int userType = (Integer)req.getSession().getAttribute("userType");
        if(userType==1){
            //用户为管理员
            Admin user = (Admin)req.getSession().getAttribute("user");
            if(!(user.getPassword()).equals(password)){
                try {
                    resp.getWriter().write("原密码错误！！！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if(adminService.editPassword(user,newPassword)){
                try {
                    resp.getWriter().write("success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(userType==2){
            //用户为学生
            StudentServiceImpl studentService = new StudentServiceImpl();
            Student user = (Student)req.getSession().getAttribute("user");
            if(!(user.getPassword()).equals(password)){
                try {
                    resp.getWriter().write("原密码错误！！！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if(studentService.editPassword(user,newPassword)){
                try {
                    resp.getWriter().write("success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(userType==3){
            //用户为老师
            TeacherServiceImpl teacherService = new TeacherServiceImpl();
            Teacher user = (Teacher)req.getSession().getAttribute("user");
            if(!(user.getPassword()).equals(password)){
                try {
                    resp.getWriter().write("原密码错误！！！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if(teacherService.editPassword(user,newPassword)){
                try {
                    resp.getWriter().write("success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        Object user = req.getSession().getAttribute("user");
    }

    private void personalView(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("person/personalView.jsp").forward(req,resp);
        } catch (ServletException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
