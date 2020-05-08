package dao.impl;

import domain.Page;
import domain.Score;
import domain.SelectedCourse;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @date 2020/5/6 - 20:05
 */
public class ScoreDaoimpl {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public List<Score> getScoreList(Score score, Page page) {
        String sql = "select * from s_score where 1=1 ";
        List<Integer> list = new ArrayList<>();
        if (score.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(score.getStudentId());
        }
        if (score.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(score.getCourseId());
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<Score> query = template.query(sql, new BeanPropertyRowMapper<Score>(Score.class), list.toArray());
        return query;
    }

    /**
     * 获取符合某一条件的数据
     * @param score
     * @return
     */
    public List<Map<String, Object>> getScoreList(Score score) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        String sql = "select s_score.*,s_student.name as studentName,s_course.name as courseName from s_score,s_student,s_course where s_score.studentId=s_student.id and s_score.courseId=s_course.id ";
        if(score.getStudentId() != 0){
            sql += " and studentId = " + score.getStudentId();
        }
        if(score.getCourseId() != 0){
            sql += " and courseId = " + score.getCourseId();
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Map<String, Object> s = new HashMap<String, Object>();
                s.put("id",resultSet.getInt("id"));
                s.put("courseId",resultSet.getInt("courseId"));
                s.put("studentId",resultSet.getInt("studentId"));
                s.put("score",resultSet.getDouble("score"));
                s.put("remark",resultSet.getString("remark"));
                s.put("studentName", resultSet.getString("studentName"));
                s.put("courseName", resultSet.getString("courseName"));
                ret.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public int getScoreListTotal(Score score) {
        String sql = "select count(*) as total from s_score where 1=1 ";
        List<Integer> list = new ArrayList<>();
        int i=0;
        if (score.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(score.getStudentId());
            i++;
        }
        if (score.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(score.getCourseId());
            i++;
        }
        if (i != 0) {
            Integer integer = template.queryForObject(sql, Integer.class, list.toArray());
            return integer;
        } else {
            Integer integer = template.queryForObject(sql, Integer.class);
            return integer;
        }
    }
    public boolean addScore(Score score){
        String sql = "insert into s_score values(null,"+score.getStudentId()+","+score.getCourseId()+","+score.getScore()+", '"+score.getRemark()+"' )";
        int update = template.update(sql);
        return update>=0?true:false;
    }
    public boolean isAdd(int studentId,int courseId){
        String sql = "select * from s_score where studentId = " + studentId + " and courseId = " + courseId;
        List<Score> query = template.query(sql, new BeanPropertyRowMapper<>(Score.class));
        if(query.size()==0)
            return false;
        else
            return  true;
    }
    public boolean editScore(Score score) {
        String sql = "update s_score set score = " + score.getScore();
        sql += ",remark = '" + score.getRemark() + "'";
        sql += " where id = " + score.getId();
        int update = template.update(sql);
        return update>=1?true:false;
    }
    public boolean deleteScore(int id) {
        // TODO Auto-generated method stub
        String sql = "delete from s_score where id = " + id;
        int update = template.update(sql);
        return update>=1?true:false;
    }
    public Map<String, Object> getAvgStats(Score score){
        Map<String,Object> ret = new HashMap<String, Object>();
        String sql = "select max(s_score.score) as max_score,avg(s_score.score) as avg_score,min(s_score.score) as min_score,s_course.name as courseName from s_score,s_course where s_score.courseId=s_course.id and s_score.courseId = " + score.getCourseId();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ret.put("max_score",resultSet.getDouble("max_score"));
                ret.put("avg_score",resultSet.getDouble("avg_score"));
                ret.put("min_score",resultSet.getDouble("min_score"));
                ret.put("courseName", resultSet.getString("courseName"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}
