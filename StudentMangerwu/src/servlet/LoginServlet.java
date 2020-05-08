package servlet;

import domain.Admin;
import domain.Student;
import domain.Teacher;
import service.impl.AdminServiceImpl;
import service.impl.StudentServiceImpl;
import service.impl.TeacherServiceImpl;
import utils.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author wu
 * @date 2020/4/19 - 16:49
 */
@WebServlet("/loginservlet")
public class LoginServlet extends HttpServlet {
    private AdminServiceImpl adminservice = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if("logout".equals(method)){
            logout(request, response);
            return;
        }
        String vcode = request.getParameter("vcode");
        String name = request.getParameter("account");
        String password = request.getParameter("password");
        int type = Integer.parseInt(request.getParameter("type"));
        String loginCpacha = request.getSession().getAttribute("loginCapcha").toString();
        request.getSession().removeAttribute("loginCapcha");
        if (StringUtil.isEmpty(vcode)) {
            response.getWriter().write("vcodeError");
            return;
        }
        if (!vcode.toUpperCase().equals(loginCpacha.toUpperCase())) {
            response.getWriter().write("vcodeError");
            return;
        }
        //验证码验证通过，对比用户名密码是否正确
        String loginStatus = "loginFaild";
        switch (type) {
            case 1: {
                AdminServiceImpl admins = new AdminServiceImpl();
                Admin admin = admins.loginAdmin(name, password);
                if (admin == null){
                    response.getWriter().write("loginError");
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("user", admin);
                session.setAttribute("userType", type);
                loginStatus = "loginSuccess";
//                response.getWriter().write("admin");
                break;
            }
            case 2:{
                StudentServiceImpl studentDao = new StudentServiceImpl();
                Student student = studentDao.studentLogin(name, password);
                if(student == null){
                    response.getWriter().write("loginError");
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("user", student);
                session.setAttribute("userType", type);
                loginStatus = "loginSuccess";
                break;
            }
            case 3:{
                TeacherServiceImpl teahcerDao = new TeacherServiceImpl();
                Teacher teacher = teahcerDao.teacherLogin(name, password);
                if(teacher == null){
                    response.getWriter().write("loginError");
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("user", teacher);
                session.setAttribute("userType", type);
                loginStatus = "loginSuccess";
                break;
            }
            default: break;
        }
        response.getWriter().write(loginStatus);
    }

    private void logout(HttpServletRequest request,HttpServletResponse response) throws IOException{
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("userType");
        response.sendRedirect("index.jsp");
    }
}

