package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.impl.CourseDaoImpl;
import domain.Course;
import domain.Page;
import domain.Student;
import org.apache.commons.beanutils.BeanUtils;
import service.impl.StudentServiceImpl;
import utils.SNgenerateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @date 2020/5/1 - 10:57
 */
@WebServlet("/courseservlet")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toCourseListView".equals(method)){
            req.getRequestDispatcher("course/courseList.jsp").forward(req,resp);
        }else if("courseList".equals(method)){
            getCourseList(req,resp);
        }else if("addCourse".equals(method)){
            addCourseList(req,resp);
        }else if("deleteCourse".equals(method)){
            deleteCourse(req,resp);
        }else if("editCourse".equals(method)){
            editCourse(req,resp);
        }

    }

    private void editCourse(HttpServletRequest req, HttpServletResponse resp) {
        CourseDaoImpl courseDao = new CourseDaoImpl();
        Course course = new Course();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(course,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean b = courseDao.editCourse(course);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) {
        CourseDaoImpl courseDao = new CourseDaoImpl();
        String[] parameterValues = req.getParameterValues("ids[]");
        String st="";
        for(String str:parameterValues){
            st+=str+",";
        }
        String substring = st.substring(0, st.length() - 1);
        boolean b = courseDao.deleteCourse(substring);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCourseList(HttpServletRequest req, HttpServletResponse resp) {
        CourseDaoImpl courseDao = new CourseDaoImpl();
        Course course = new Course();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(course,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean b = courseDao.addCourse(course);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getCourseList(HttpServletRequest req, HttpServletResponse resp) {
        HashMap<String, Object> map = new HashMap<>();
        CourseDaoImpl courseDao = new CourseDaoImpl();
        String name = req.getParameter("name");
        String cl=req.getParameter("teacherid")==null? "0":req.getParameter("teacherid");
        String str1=req.getParameter("page")==null? "1":req.getParameter("page");
        String str2=req.getParameter("rows")==null? "999":req.getParameter("rows");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        int cl1 = Integer.parseInt(cl);
        Course course = new Course();
        Page page = new Page(currentPage,pageSize);
        course.setName(name);
        course.setTeacherId(cl1);
        //返回总条数total
        int courseListTotal = courseDao.getCourseListTotal(course);
        map.put("total",courseListTotal);
        List<Course> courseList = courseDao.getCourseList(course, page);
        //获取当前用户类型
//        int userType = (Integer)req.getSession().getAttribute("userType");
//        if(userType==2){
//            Student user = (Student)req.getSession().getAttribute("user");
//            for (Student st:studentList) {
//                if(st.getId()==user.getId()){
//                    studentList.clear();
//                    studentList.add(st);
//                    map.put("rows",studentList);
//                    break;
//                }
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            resp.setContentType("application/json;charset=utf-8");
//            try {
//                objectMapper.writeValue(resp.getWriter(),map);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
        map.put("rows",courseList);
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                objectMapper.writeValue(resp.getWriter(),courseList);
            }else{
                objectMapper.writeValue(resp.getWriter(),map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
