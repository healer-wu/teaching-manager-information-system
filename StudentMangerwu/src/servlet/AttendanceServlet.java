package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.impl.AttendanceDao;
import dao.impl.CourseDaoImpl;
import dao.impl.SelectedCourseDao;
import domain.Attendance;
import domain.Course;
import domain.Page;
import domain.Student;
import org.apache.commons.beanutils.BeanUtils;
import utils.DataFormatUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author wu
 * @date 2020/5/4 - 19:51
 */
@WebServlet("/attendanceservlet")
public class AttendanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("toAttendanceListView".equals(method)) {
            req.getRequestDispatcher("attendance/attendanceList.jsp").forward(req, resp);
        } else if ("AttendanceList".equals(method)) {
            getAttendanceList(req, resp);
        } else if ("getStudentSelectedCourseList".equals(method)) {
            getStudentSelectedCourseList(req, resp);
        } else if ("AddAttendance".equals(method)) {
            addAttendance(req, resp);
        } else if ("DeleteAttendance".equals(method)) {
            deleteAttendance(req, resp);
        }

    }

    private void deleteAttendance(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        int i = Integer.parseInt(id);
        AttendanceDao attendanceDao = new AttendanceDao();
        boolean b = attendanceDao.deleteAttendance(i);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(!b){
            try {
                resp.getWriter().write("not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAttendance(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        Attendance attendance = new Attendance();
        try {
            BeanUtils.populate(attendance, parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        attendance.setDate(DataFormatUtils.getDateFormat(new Date(), "yyyy-MM-dd"));
        AttendanceDao attendanceDao = new AttendanceDao();
        resp.setCharacterEncoding("UTF-8");
        if (attendanceDao.isAttendanced(attendance.getStudentId(), attendance.getCourseId(), attendance.getType(), attendance.getDate())) {
            try {
                resp.getWriter().write("已签到");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            boolean b = attendanceDao.addAttendance(attendance);
            if (b)
                resp.getWriter().write("success");
            else
                resp.getWriter().write("系统内部错误，请联系管理员！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getStudentSelectedCourseList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String studentId = req.getParameter("studentId") == null ? "0" : req.getParameter("studentId");
        SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
        ArrayList<Course> courses = new ArrayList<>();
        List<Integer> selectedCourseId = selectedCourseDao.getSelectedCourseIdByStudentId(studentId);
        Page page = new Page(1, 999);
        for (Integer i : selectedCourseId) {
            Course course = new Course();
            course.setId(i);
            CourseDaoImpl courseDao = new CourseDaoImpl();
            List<Course> courseList = courseDao.getCourseList(course, page);
            courses.add(courseList.get(0));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getWriter(), courses);
    }

    private void getAttendanceList(HttpServletRequest req, HttpServletResponse resp) {
        String str1 = req.getParameter("page") == null ? "1" : req.getParameter("page");
        String str2 = req.getParameter("rows") == null ? "999" : req.getParameter("rows");
        int studentId = req.getParameter("studentId") == null ? 0 : Integer.parseInt(req.getParameter("studentId"));
        int courseId = req.getParameter("courseId") == null ? 0 : Integer.parseInt(req.getParameter("courseId"));
        String type = req.getParameter("type")==""?null:req.getParameter("type");
        String date = req.getParameter("date")==""?null:req.getParameter("date");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        Attendance attendance = new Attendance();
       attendance.setDate(date);
       attendance.setCourseId(courseId);
       attendance.setStudentId(studentId);
       attendance.setType(type);
        //获取当前登录用户类型
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        if(userType == 2){
            //如果是学生，只能查看自己的信息
            Student currentUser = (Student)req.getSession().getAttribute("user");
            studentId = currentUser.getId();
            attendance.setStudentId(studentId);
        }
        AttendanceDao attendanceDao = new AttendanceDao();
        List<Attendance> attendanceList = attendanceDao.getAttendanceList(attendance, new Page(currentPage, pageSize));
        int total = attendanceDao.getAttendanceListTotal(attendance);
        resp.setContentType("application/json;charset=utf-8");
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("total", total);
        ret.put("rows", attendanceList);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(resp.getWriter(), ret);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
