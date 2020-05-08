package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
import dao.impl.*;
import domain.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileUploadException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @date 2020/5/6 - 19:51
 */
@WebServlet("/scoreservlet")
public class ScoreServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("toScoreListView".equals(method)) {
            try {
                req.getRequestDispatcher("score/scoreList.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } else if ("AddScore".equals(method)) {
            addScore(req, resp);
        } else if ("ScoreList".equals(method)) {
            getScoreList(req, resp);
        } else if ("EditScore".equals(method)) {
            editScore(req, resp);
        } else if ("DeleteScore".equals(method)) {
            deleteScore(req, resp);
        } else if ("ImportScore".equals(method)) {
            importScore(req, resp);
        } else if ("ExportScoreList".equals(method)) {
            exportScore(req, resp);
        } else if ("toScoreStatsView".equals(method)) {
            try {
                req.getRequestDispatcher("score/scoreStats.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } else if ("getStatsList".equals(method)) {
            getStatsList(req, resp);
        }
    }

    private void getStatsList(HttpServletRequest req, HttpServletResponse resp) {
        int courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid").toString());
        String searchType = req.getParameter("searchType");
        resp.setCharacterEncoding("UTF-8");
        if (courseId == 0) {
            try {
                resp.getWriter().write("error");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        ScoreDaoimpl scoreDao = new ScoreDaoimpl();
        Score score = new Score();
        score.setCourseId(courseId);
        if ("avg".equals(searchType)) {
            Map<String, Object> avgStats = scoreDao.getAvgStats(score);
            List<Double> scoreList = new ArrayList<Double>();
            scoreList.add(Double.parseDouble(avgStats.get("max_score").toString()));
            scoreList.add(Double.parseDouble(avgStats.get("min_score").toString()));
            scoreList.add(Double.parseDouble(avgStats.get("avg_score").toString()));
            List<String> avgStringList = new ArrayList<String>();
            avgStringList.add("最高分");
            avgStringList.add("最低分");
            avgStringList.add("平均分");
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("courseName", avgStats.get("courseName").toString());
            retMap.put("scoreList", scoreList);
            retMap.put("avgList", avgStringList);
            retMap.put("type", "suceess");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(resp.getWriter(),retMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        List<Map<String, Object>> scoreList = scoreDao.getScoreList(score);

        List<Integer> numberList = new ArrayList<Integer>();
        numberList.add(0);
        numberList.add(0);
        numberList.add(0);
        numberList.add(0);
        numberList.add(0);
        List<String> rangeStringList = new ArrayList<String>();
        rangeStringList.add("60分以下");
        rangeStringList.add("60~70分");
        rangeStringList.add("70~80分");
        rangeStringList.add("80~90分");
        rangeStringList.add("90~100分");
        String courseName = "";
        for (Map<String, Object> entry : scoreList) {
            courseName = entry.get("courseName").toString();
            double scoreValue = Double.parseDouble(entry.get("score").toString());
            if (scoreValue < 60) {
                numberList.set(0, numberList.get(0) + 1);
                continue;
            }
            if (scoreValue <= 70 && scoreValue >= 60) {
                numberList.set(1, numberList.get(1) + 1);
                continue;
            }
            if (scoreValue <= 80 && scoreValue > 70) {
                numberList.set(2, numberList.get(2) + 1);
                continue;
            }
            if (scoreValue <= 90 && scoreValue > 80) {
                numberList.set(3, numberList.get(3) + 1);
                continue;
            }
            if (scoreValue <= 100 && scoreValue > 90) {
                numberList.set(4, numberList.get(4) + 1);
                continue;
            }
        }
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("courseName", courseName);
        retMap.put("numberList", numberList);
        retMap.put("rangeList", rangeStringList);
        retMap.put("type", "suceess");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(resp.getWriter(),retMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportScore(HttpServletRequest req, HttpServletResponse resp) {
        int studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid"));
        int courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid"));
        //获取当前登录用户类型
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        if (userType == 2) {
            //如果是学生，只能查看自己的信息
            Student currentUser = (Student) req.getSession().getAttribute("user");
            studentId = currentUser.getId();
        }
        Score score = new Score();
        score.setStudentId(studentId);
        score.setCourseId(courseId);
        try {
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("score_list_sid_" + studentId + "_cid_" + courseId + ".xls", "UTF-8"));
            resp.setHeader("Connection", "close");
            resp.setHeader("Content-Type", "application/octet-stream");
            ServletOutputStream outputStream = resp.getOutputStream();
            ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
            List<Map<String, Object>> scoreList = scoreDaoimpl.getScoreList(score);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet createSheet = hssfWorkbook.createSheet("成绩列表");
            HSSFRow createRow = createSheet.createRow(0);
            createRow.createCell(0).setCellValue("学生");
            createRow.createCell(1).setCellValue("课程");
            createRow.createCell(2).setCellValue("成绩");
            createRow.createCell(3).setCellValue("备注");
            //实现将数据装入到excel文件中
            int row = 1;
            for (Map<String, Object> entry : scoreList) {
                createRow = createSheet.createRow(row++);
                createRow.createCell(0).setCellValue(entry.get("studentName").toString());
                createRow.createCell(1).setCellValue(entry.get("courseName").toString());
                createRow.createCell(2).setCellValue(new Double(entry.get("score") + ""));
                createRow.createCell(3).setCellValue(entry.get("remark") + "");
            }
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void importScore(HttpServletRequest req, HttpServletResponse resp) {
        FileUpload fileUpload = new FileUpload(req);
        fileUpload.setFileFormat("xls");
        fileUpload.setFileFormat("xlsx");
        fileUpload.setFileSize(2048);
        resp.setCharacterEncoding("UTF-8");
        try {
            InputStream uploadInputStream = null;
            uploadInputStream = fileUpload.getUploadInputStream();
            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(uploadInputStream);
            XSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
            int count = 0;
            String errorMsg = "";
            StudentDaoImpl studentDao = new StudentDaoImpl();
            CourseDaoImpl courseDao = new CourseDaoImpl();
            SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
            ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
            for (int rowNum = 1; rowNum <= sheetAt.getLastRowNum(); rowNum++) {
                Row row = sheetAt.getRow(rowNum);
                Cell cell = row.getCell(0);
                //获取第0列，学生id
                if (cell == null) {
                    errorMsg += "第" + rowNum + "行学生id缺失！\n";
                    continue;
                }
                if (cell.getCellType() != CellType.NUMERIC) {
                    errorMsg += "第" + rowNum + "行学生id类型不是整数！\n";
                    continue;
                }
                int studentId = new Double(cell.getNumericCellValue()).intValue();
                //获取第1列，课程id
                cell = row.getCell(1);
                if (cell == null) {
                    errorMsg += "第" + rowNum + "行课程id缺失！\n";
                    continue;
                }
                if (cell.getCellType() != CellType.NUMERIC) {
                    errorMsg += "第" + rowNum + "行课程id不是整数！\n";
                    continue;
                }
                int courseId = new Double(cell.getNumericCellValue()).intValue();
                //获取第2列，成绩
                cell = row.getCell(2);
                if (cell == null) {
                    errorMsg += "第" + rowNum + "行成绩缺失！\n";
                    continue;
                }
                if (cell.getCellType() != CellType.NUMERIC) {
                    errorMsg += "第" + rowNum + "行成绩类型不是数字！\n";
                    continue;
                }
                double scoreValue = cell.getNumericCellValue();
                //获取第3列，备注
                cell = row.getCell(3);
                String remark = null;
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    remark = cell.getStringCellValue();
                }
                Student student = studentDao.getStudent(studentId);
                if (student == null) {
                    errorMsg += "第" + rowNum + "行学生id不存在！\n";
                    continue;
                }
                Course course = courseDao.getCourse(courseId);
                if (course == null) {
                    errorMsg += "第" + rowNum + "行课程id不存在！\n";
                    continue;
                }
                if (!selectedCourseDao.isSeleected(studentId, courseId)) {
                    errorMsg += "第" + rowNum + "行课程该同学未选，不合法！\n";
                    continue;
                }
                if (scoreDaoimpl.isAdd(studentId, courseId)) {
                    errorMsg += "第" + rowNum + "行成绩已经被添加，请勿重复添加！\n";
                    continue;
                }
                Score score = new Score();
                score.setCourseId(courseId);
                score.setRemark(remark);
                score.setScore(scoreValue);
                score.setStudentId(studentId);
                if (scoreDaoimpl.addScore(score)) {
                    count++;
                }
            }
            errorMsg += "成功录入" + count + "条成绩信息！";
            try {
                resp.getWriter().write("<div id='message'>" + errorMsg + "</div>");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
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
        } catch (NullFileException e1) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传的文件为空!</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e1.printStackTrace();
        } catch (SizeException e2) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传文件大小不能超过" + fileUpload.getFileSize() + "！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e2.printStackTrace();
        } catch (IOException e3) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>读取文件出错！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e3.printStackTrace();
        } catch (FileFormatException e4) {
            // TODO: handle exception
            try {
                resp.getWriter().write("<div id='message'>上传文件格式不正确，请上传 " + fileUpload.getFileFormat() + " 格式的文件！</div>");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            e4.printStackTrace();
        } catch (FileUploadException e5) {
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

    private void deleteScore(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        int i = Integer.parseInt(id);
        ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
        boolean b = scoreDaoimpl.deleteScore(i);
        if (b) {
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!b) {
            try {
                resp.getWriter().write("not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editScore(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
        Score score = new Score();
        try {
            BeanUtils.populate(score, parameterMap);
            boolean add = scoreDaoimpl.isAdd(score.getStudentId(), score.getCourseId());
            if (!add) {
                resp.getWriter().write("added");
                return;
            }
            boolean b = scoreDaoimpl.editScore(score);
            if (b) {
                resp.getWriter().write("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScoreList(HttpServletRequest req, HttpServletResponse resp) {
        int studentId = req.getParameter("studentid") == null ? 0 : Integer.parseInt(req.getParameter("studentid"));
        int courseId = req.getParameter("courseid") == null ? 0 : Integer.parseInt(req.getParameter("courseid"));
        Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
        Score score = new Score();
        //获取当前登录用户类型
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        if (userType == 2) {
            //如果是学生，只能查看自己的信息
            Student currentUser = (Student) req.getSession().getAttribute("user");
            studentId = currentUser.getId();
        }
        score.setCourseId(courseId);
        score.setStudentId(studentId);
        ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
        List<Score> scoreList = scoreDaoimpl.getScoreList(score, new Page(currentPage, pageSize));
        int scoreListTotal = scoreDaoimpl.getScoreListTotal(score);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("total", scoreListTotal);
        ret.put("rows", scoreList);
        resp.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String from = req.getParameter("from");
            if ("combox".equals(from)) {
                objectMapper.writeValue(resp.getWriter(), scoreList);
            } else {
                objectMapper.writeValue(resp.getWriter(), ret);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addScore(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        ScoreDaoimpl scoreDaoimpl = new ScoreDaoimpl();
        Score score = new Score();
        try {
            BeanUtils.populate(score, parameterMap);
            boolean add = scoreDaoimpl.isAdd(score.getStudentId(), score.getCourseId());
            if (add) {
                resp.getWriter().write("added");
                return;
            }
            boolean b = scoreDaoimpl.addScore(score);
            if (b) {
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
