package servlet;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
import domain.Student;
import domain.Teacher;
import org.apache.commons.fileupload.FileUploadException;
import service.impl.StudentServiceImpl;
import service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wu
 * @date 2020/4/26 - 8:19
 */
@WebServlet("/photoservlet")
public class PhotoServlet extends HttpServlet {
    private StudentServiceImpl stu=new StudentServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("getPhoto".equals(method)){
            getPhoto(req,resp);
        } else if("setPhoto".equals(method)){
            uploadPhoto(req,resp);
        }
    }

    private void uploadPhoto(HttpServletRequest req, HttpServletResponse resp) {
        int sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
        int tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));
        FileUpload fileUpload = new FileUpload(req);
        fileUpload.setFileFormat("jpg");
        fileUpload.setFileFormat("png");
        fileUpload.setFileFormat("jpeg");
        fileUpload.setFileFormat("gif");
        fileUpload.setFileSize(1024*100);
        resp.setCharacterEncoding("UTF-8");
        try {
            InputStream uploadInputStream = fileUpload.getUploadInputStream();
            if(sid != 0){
                Student student = new Student();
                student.setId(sid);
                student.setPhoto(uploadInputStream);
                StudentServiceImpl studentService = new StudentServiceImpl();
                boolean b = studentService.editStudentPhoto(student);
                if(b){
                    resp.getWriter().write("<div id='message'>上传成功！</div>");
                }else{
                    resp.getWriter().write("<div id='message'>上传失败！</div>");
                }
            }
            if(tid != 0) {
                Teacher teacher = new Teacher();
                teacher.setId(tid);
                teacher.setPhoto(uploadInputStream);
                TeacherServiceImpl teacherDao = new TeacherServiceImpl();
                if (teacherDao.editTeacherPhoto(teacher)) {
                    resp.getWriter().write("<div id='message'>上传成功！</div>");
                } else {
                    resp.getWriter().write("<div id='message'>上传失败！</div>");
                }
            }
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            try {
                resp.getWriter().write("<div id='message'>上传协议错误！</div>");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }catch (NullFileException e1) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传的文件为空!</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e1.printStackTrace();
        }
        catch (SizeException e2) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传文件大小不能超过"+fileUpload.getFileSize()+"！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e2.printStackTrace();
        }
        catch (IOException e3) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>读取文件出错！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e3.printStackTrace();
        }
        catch (FileFormatException e4) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传文件格式不正确，请上传 "+fileUpload.getFileFormat()+" 格式的文件！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e4.printStackTrace();
        }
        catch (FileUploadException e5) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传文件失败！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e5.printStackTrace();
        }
    }

    private void getPhoto(HttpServletRequest req, HttpServletResponse resp) {
        int sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
        int tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));
        if(sid != 0){
            //学生
            StudentServiceImpl studentDao = new StudentServiceImpl();
            Student student = studentDao.getStudent(sid);
            if(student != null){
                InputStream photo = student.getPhoto();
                if(photo != null){
                    try {
                        byte[] b = new byte[photo.available()];
                        photo.read(b);
                        resp.getOutputStream().write(b,0,b.length);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        if(tid != 0){
            //教师
            TeacherServiceImpl teacherDao = new TeacherServiceImpl();
            Teacher teacher = teacherDao.getTeacher(tid);
            if(teacher != null){
                InputStream photo = teacher.getPhoto();
                if(photo != null){
                    try {
                        byte[] b = new byte[photo.available()];
                        photo.read(b);
                        resp.getOutputStream().write(b,0,b.length);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        String path = req.getServletContext().getRealPath("");
        File file = new File(path+"\\file\\1313.jpg");
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            resp.getOutputStream().write(b,0,b.length);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
