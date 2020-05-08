package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @date 2020/4/21 - 15:25
 */
@WebServlet("/studentservlet")
public class StudentServlet extends HttpServlet {
    private StudentServiceImpl stu=new StudentServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toStudentListView".equals(method)){
            studentList(req,resp);
        }else if("addStudent".equals(method)){
            addStudent(req,resp);
        }else if("studentList".equals(method)){
            getListStudent(req,resp);
        }else if("editStudent".equals(method)){
            editStudent(req,resp);
        }else if("deleteStudent".equals(method)){
            deleteStudent(req,resp);
        }

    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] parameterValues = req.getParameterValues("ids[]");
        String st="";
        for(String str:parameterValues){
            st+=str+",";
        }
        String substring = st.substring(0, st.length() - 1);
        boolean b = stu.deleteStudent(substring);
        if(b){
            resp.getWriter().write("success");
        }
    }

    private void editStudent(HttpServletRequest req, HttpServletResponse resp) {
        Student student = new Student();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(student,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean b = stu.editStudent(student);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getListStudent(HttpServletRequest req, HttpServletResponse resp) {
        StudentServiceImpl stu = new StudentServiceImpl();
        HashMap<String, Object> map = new HashMap<>();
        String name = req.getParameter("studentName");
        String cl=req.getParameter("clazzid")==null? "0":req.getParameter("clazzid");
        String str1=req.getParameter("page")==null? "1":req.getParameter("page");
        String str2=req.getParameter("rows")==null? "999":req.getParameter("rows");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        int cl1 = Integer.parseInt(cl);
        Student student = new Student();
        Page page = new Page(currentPage,pageSize);
        student.setName(name);
        student.setClass_id(cl1);
        //返回总条数total
        int studentListTotal = stu.getStudentListTotal(student);
        map.put("total",studentListTotal);
        List<Student> studentList = stu.getStudentList(student, page);
        //获取当前用户类型
        int userType = (Integer)req.getSession().getAttribute("userType");
        if(userType==2){
            Student user = (Student)req.getSession().getAttribute("user");
            for (Student st:studentList) {
                if(st.getId()==user.getId()){
                    studentList.clear();
                    studentList.add(st);
                    map.put("rows",studentList);
                    break;
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            resp.setContentType("application/json;charset=utf-8");
            try {
                String from = req.getParameter("from");
                if("combox".equals(from)){
                    objectMapper.writeValue(resp.getWriter(),studentList);
                }else{
                    objectMapper.writeValue(resp.getWriter(),map);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        map.put("rows",studentList);
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                objectMapper.writeValue(resp.getWriter(),studentList);
            }else{
                objectMapper.writeValue(resp.getWriter(),map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) {
        Student student = new Student();
        Map<String, String[]> parameterMap = req.getParameterMap();
        try {
            BeanUtils.populate(student,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int class_id = Integer.parseInt(parameterMap.get("class_id")[0]);
        String sn = SNgenerateUtils.getSN(class_id);
        student.setSn(sn);
        boolean b = stu.addStudent(student);
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void studentList(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("student/studentList.jsp").forward(req,resp);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
