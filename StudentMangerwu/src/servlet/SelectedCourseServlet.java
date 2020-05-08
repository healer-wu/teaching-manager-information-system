package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.impl.CourseDaoImpl;
import dao.impl.SelectedCourseDao;
import domain.Page;
import domain.SelectedCourse;
import domain.Student;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @date 2020/5/1 - 17:04
 */
@WebServlet("/selectedcourseservlet")
public class SelectedCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toSelectedCourseListView".equals(method)){
            try {
                req.getRequestDispatcher("course/selectedCourseList.jsp").forward(req, resp);
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if("AddSelectedCourse".equals(method)){
            addSelectedCourse(req,resp);
        }else if("SelectedCourseList".equals(method)){
            getSelectedCourseList(req,resp);
        }else if("DeleteSelectedCourse".equals(method)){
            deleteSelectedCourse(req,resp);
        }
    }

    private void deleteSelectedCourse(HttpServletRequest req, HttpServletResponse resp) {
        SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
        CourseDaoImpl courseDao = new CourseDaoImpl();
        String[] ids = req.getParameterValues("ids[]");
        String st="";
        for(String str:ids){
            st+=str+",";
        }
        String substring = st.substring(0, st.length() - 1);
        List<Integer> selectedCourseId = selectedCourseDao.getSelectedCourseId(substring);
        boolean b = selectedCourseDao.deleteSelectedCourse(substring);
        if(b){
            try {
//                String s="";
//                for (int a:selectedCourseId) {
//                    s+=a+",";
//                }
//                String substring1 = s.substring(0, s.length() - 1);
                courseDao.updateCourseSelectedNum(selectedCourseId);
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getSelectedCourseList(HttpServletRequest req, HttpServletResponse resp) {
        SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
        HashMap<String, Object> map = new HashMap<>();
        String studentId = req.getParameter("studentId")==null? "0":req.getParameter("studentId");
        String cl=req.getParameter("courseId")==null? "0":req.getParameter("courseId");
        String str1=req.getParameter("page")==null? "1":req.getParameter("page");
        String str2=req.getParameter("rows")==null? "999":req.getParameter("rows");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        int cl1 = Integer.parseInt(cl);
        int i = Integer.parseInt(studentId);
        SelectedCourse selectedCourse = new SelectedCourse();
        Page page = new Page(currentPage,pageSize);
        selectedCourse.setCourseId(cl1);
        selectedCourse.setStudentId(i);
        //返回总条数total
        int selectedCourseListTotal = selectedCourseDao.getSelectedCourseListTotal(selectedCourse);
        map.put("total",selectedCourseListTotal);
        List<SelectedCourse> selectedCourseList = selectedCourseDao.getSelectedCourseList(selectedCourse, page);
        //获取当前用户类型
        int userType = (Integer)req.getSession().getAttribute("userType");
        if(userType==2){
            List<SelectedCourse> selectedCourses = new ArrayList<>();
            Student user = (Student)req.getSession().getAttribute("user");
            for (SelectedCourse st:selectedCourseList) {
                if(st.getStudentId()==user.getId()){
                    selectedCourses.add(st);
                }
            }
            int size = selectedCourses.size();
            map.put("total",size);
            map.put("rows",selectedCourses);
            ObjectMapper objectMapper = new ObjectMapper();
            resp.setContentType("application/json;charset=utf-8");
            try {
                objectMapper.writeValue(resp.getWriter(),map);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        map.put("rows",selectedCourseList);
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                objectMapper.writeValue(resp.getWriter(),selectedCourseList);
            }else{
                objectMapper.writeValue(resp.getWriter(),map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSelectedCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
        CourseDaoImpl courseDao = new CourseDaoImpl();
        String courseId = parameterMap.get("courseId")[0];
        if(courseDao.isFull(Integer.parseInt(courseId))){
            resp.getWriter().write("courseFull");
            return;
        }
        SelectedCourse selectedCourse = new SelectedCourse();
        try {
            BeanUtils.populate(selectedCourse,parameterMap);
            if(selectedCourseDao.isSeleected(selectedCourse)){
                resp.getWriter().write("isSelected");
                return;
            }
            boolean b = selectedCourseDao.addSelectedCourse(selectedCourse);
            if(b){
                courseDao.updateCourseSelectedNum(Integer.parseInt(courseId));
                resp.getWriter().write("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
