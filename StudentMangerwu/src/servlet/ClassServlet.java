package servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Clazz;
import domain.Page;
import service.impl.ClazzServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author wu
 * @date 2020/4/21 - 17:34
 */
@WebServlet("/classservlet")
public class ClassServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toClazzListView".equals(method)){
            classList(req,resp);
        }else if("getClazzList".equals(method)){
            getClazzList(req,resp);
        }else if("addClazz".equals(method)){
            addClazz(req,resp);
        }else if("deleteClazz".equals(method)){
            deleteClazz(req,resp);
        }else if("editClazz".equals(method)){
            editClazz(req,resp);
        }
    }

    private void editClazz(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String info = req.getParameter("info");
        int i=Integer.parseInt(id);
        Clazz clazz = new Clazz();
        clazz.setInfo(info);
        clazz.setName(name);
        clazz.setId(i);
        ClazzServiceImpl clazzService = new ClazzServiceImpl();
        if(clazzService.editClazz(clazz)){
            resp.getWriter().write("success");
        }
    }

    private void deleteClazz(HttpServletRequest req, HttpServletResponse resp) {
        String parameter = req.getParameter("clazzid");
        ClazzServiceImpl clazzService = new ClazzServiceImpl();
        boolean b = clazzService.deleteClazz(Integer.parseInt(parameter));
        if(b){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addClazz(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String info = req.getParameter("info");
        Clazz clazz = new Clazz();
        clazz.setName(name);
        clazz.setInfo(info);
        ClazzServiceImpl clazzService = new ClazzServiceImpl();
        boolean b = clazzService.addClazz(clazz);
        if(b){
            resp.getWriter().write("success");
        }
        else
            resp.getWriter().write("failed");
    }

    private void getClazzList(HttpServletRequest req, HttpServletResponse resp) {
        ClazzServiceImpl clazzService = new ClazzServiceImpl();
        String name = req.getParameter("clazzName");
        String str1=req.getParameter("page")==null? "1":req.getParameter("page");
        String str2=req.getParameter("rows")==null? "999":req.getParameter("rows");
        int currentPage = Integer.parseInt(str1);
        int pageSize = Integer.parseInt(str2);
        Clazz clazz = new Clazz();
        Page page = new Page(currentPage,pageSize);
        clazz.setName(name);
        //返回总条数total
        int total=clazzService.getClazzListTotal(clazz);
        List<Clazz> clazzList = clazzService.getClazzList(clazz, page);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",clazzList);
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        try {
            String from = req.getParameter("from");
            if("stuFrom".equals(from)){
                objectMapper.writeValue(resp.getWriter(),clazzList);
            }else{
                objectMapper.writeValue(resp.getWriter(),map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void classList(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("class/classList.jsp").forward(req,resp);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
