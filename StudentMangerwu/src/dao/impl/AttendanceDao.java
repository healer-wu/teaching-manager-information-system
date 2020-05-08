package dao.impl;

import domain.Attendance;
import domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 * @date 2020/5/5 - 13:16
 * 考勤数据库操作
 */
public class AttendanceDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public boolean addAttendance(Attendance attendance){
        String sql = "insert into s_attendance values(null,"+attendance.getCourseId()+","+attendance.getStudentId()+",'"+attendance.getType()+"','"+attendance.getDate()+"')";
        int update = template.update(sql);
        return update>=1?true:false;
    }
    /**
     * 判断当前是否已签到
     */
    public boolean isAttendanced(int studentId,int courseId,String type,String date){
        String sql = "select * from s_attendance where studentId = " + studentId + " and courseId = " + courseId + " and type = '" + type + "' and date = '" + date + "'";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
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
        return false;
    }
    public List<Attendance> getAttendanceList(Attendance attendance, Page page){
        String sql = "select * from s_attendance where 1=1 ";
        List<Integer> list = new ArrayList<>();
        if (attendance.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(attendance.getStudentId());
        }
        if (attendance.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(attendance.getCourseId());
        }
        if (attendance.getType()!=null) {
            sql += " and type = '"+attendance.getType()+"'";
        }
        if (attendance.getDate()!=null) {
            sql += " and date='"+attendance.getDate()+"'";
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<Attendance> query = template.query(sql, new BeanPropertyRowMapper<Attendance>(Attendance.class), list.toArray());
        return query;
    }
    public int  getAttendanceListTotal(Attendance attendance){
        String sql = "select count(*) as total from s_attendance where 1=1 ";
        int i=0;
        List<Integer> list = new ArrayList<>();
        if (attendance.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(attendance.getStudentId());
            i++;
        }
        if (attendance.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(attendance.getCourseId());
            i++;
        }
        if (attendance.getType()!=null) {
            sql += " and type = '"+attendance.getType()+"'";
        }
        if (attendance.getDate()!=null) {
            sql += " and date='"+attendance.getDate()+"'";
        }
        if(i!=0){
            return template.queryForObject(sql,Integer.class,list.toArray());
        }else {
            return template.queryForObject(sql,Integer.class);
        }
    }
    public boolean deleteAttendance(int id) {
        String sql = "delete from s_attendance where id in (" + id + ")";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int update = 0;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            update = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return update >= 1 ? true : false;
    }
}
