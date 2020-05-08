package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Page;
import domain.Student;
import domain.Teacher;
import org.apache.commons.beanutils.BeanUtils;
import service.impl.StudentServiceImpl;
import service.impl.TeacherServiceImpl;
import utils.SNgenerateUtils;
import utils.TeacherSNgenerateUtils;

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
 * @date 2020/4/28 - 18:53
 */
@WebServlet("/teacherservlet")
public class TeacherServlet extends HttpServlet {
    private TeacherServiceImpl teacherService=new TeacherServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toTeacherListView".equals(method)){
            teacherList(req,resp);
        }else if("addTeacher".equals(method)){
            addTeacher(req,resp);
        }else if("teacherList".equals(method)){
            getTeacherList(req,resp);
        }else if("editTeacher".equals(method)){
            editTeacher(req,resp);
        }else if("deleteTeacher".equals(method)){
            deleteTeacher(req,resp);
        }
    }

    private void editTeacher(HttpServletRequest req, HttpServletResponse resp) {
        Teacher teacher = new Teacher();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(teacher,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean b = teacherService.editTeacher(teacher);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTeacherList(HttpServletRequest req, HttpServletResponse resp) {
        TeacherServiceImpl stu = new TeacherServiceImpl();
        HashMap<String, Object> map = new HashMap<>();
        String name = req.getParameter("teacherName");
        String cl=req.getParameter("clazzid")==null? "0":req.getParameter("clazzid");
        String str1=req.getParameter("page")==null? "1":req.getParameter("page");
        String str2=req.getParameter("rows")==null? "999":req.getParameter("rows");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        int cl1 = Integer.parseInt(cl);
        Teacher teacher = new Teacher();
        Page page = new Page(currentPage,pageSize);
        teacher.setName(name);
        teacher.setClass_id(cl1);
        //返回总条数total
        int teacherListTotal = stu.getTeacherListTotal(teacher);
        map.put("total",teacherListTotal);
        List<Teacher> teacherList = stu.getTeacherList(teacher, page);
        int userType = (Integer)req.getSession().getAttribute("userType");
        if(userType==3){
            Teacher user = (Teacher)req.getSession().getAttribute("user");
            List<Teacher> teachers = new ArrayList<>();
            for (Teacher t:teacherList) {
                if(t.getSn().equals(user.getSn())){
                    teachers.add(t);
                }
            }
            map.put("rows",teachers);
            map.put("total",teachers.size());
            ObjectMapper objectMapper = new ObjectMapper();
            resp.setContentType("application/json;charset=utf-8");
            try {
                String from = req.getParameter("from");
                if("combox".equals(from)){
                    objectMapper.writeValue(resp.getWriter(),teachers);
                }else{
                    objectMapper.writeValue(resp.getWriter(),map);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        map.put("rows",teacherList);
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                objectMapper.writeValue(resp.getWriter(),teacherList);
            }else{
                objectMapper.writeValue(resp.getWriter(),map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) {
        String[] parameterValues = req.getParameterValues("ids[]");
        String st="";
        for(String str:parameterValues){
            st+=str+",";
        }
        String substring = st.substring(0, st.length() - 1);
        boolean b = teacherService.deleteTeacher(substring);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addTeacher(HttpServletRequest req, HttpServletResponse resp) {
        Teacher teacher = new Teacher();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(teacher,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int class_id = Integer.parseInt(parameterMap.get("class_id")[0]);
        String sn = TeacherSNgenerateUtils.getSN(class_id);
        teacher.setSn(sn);
        boolean b = teacherService.addTeacher(teacher);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void teacherList(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("teacher/teacherList.jsp").forward(req,resp);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
