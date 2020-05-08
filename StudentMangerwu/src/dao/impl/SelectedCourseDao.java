package dao.impl;

import dao.ISelectedCourseDao;
import domain.Page;
import domain.SelectedCourse;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import utils.JDBCUtils;
import utils.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 * @date 2020/5/2 - 14:54
 */
public class SelectedCourseDao implements ISelectedCourseDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public List<SelectedCourse> getSelectedCourseList(SelectedCourse selectedCourse, Page page) {
        String sql = "select * from s_selected_course where 1=1 ";
        List<Integer> list = new ArrayList<>();
        if (selectedCourse.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(selectedCourse.getStudentId());
        }
        if (selectedCourse.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(selectedCourse.getCourseId());
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<SelectedCourse> query = template.query(sql, new BeanPropertyRowMapper<SelectedCourse>(SelectedCourse.class), list.toArray());
        return query;
    }

    public int getSelectedCourseListTotal(SelectedCourse selectedCourse) {
        String sql = "select count(*) as total from s_selected_course where 1=1 ";
        int i = 0;
        List<Integer> list = new ArrayList<>();
        if (selectedCourse.getStudentId()!=0) {
            sql += " and studentId= ?";
            list.add(selectedCourse.getStudentId());
            i=1;
        }
        if (selectedCourse.getCourseId()!=0) {
            sql += " and courseId= ? ";
            list.add(selectedCourse.getCourseId());
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
    public boolean addSelectedCourse(SelectedCourse selectedCourse){
        String sql = "insert into s_selected_course values(null,"+selectedCourse.getStudentId()+","+selectedCourse.getCourseId()+")";
        int update = template.update(sql);
        return update>=0?true:false;
    }
    public boolean deleteSelectedCourse(String id){
        String sql = "delete from s_selected_course where id in (" + id + ")";
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
    public List<Integer> getSelectedCourseId(String id){
        String sql = "select * from s_selected_course where id in (" + id + ")";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        List<Integer> integers = new ArrayList<>();
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                integers.add(resultSet.getInt("courseId"));
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
        return integers;

    }
    public List<Integer> getSelectedCourseIdByStudentId(String id){
        String sql = "select * from s_selected_course where studentId in (" + id + ")";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        List<Integer> integers = new ArrayList<>();
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                integers.add(resultSet.getInt("courseId"));
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
        return integers;

    }
    public boolean isSeleected(SelectedCourse selectedCourse){
        String sql="select * from s_selected_course where studentId="+selectedCourse.getStudentId()+" and courseId="+selectedCourse.getCourseId();
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
    public boolean isSeleected(int studentId,int courseId){
        SelectedCourse selectedCourse = new SelectedCourse();
        selectedCourse.setStudentId(studentId);
        selectedCourse.setCourseId(courseId);
        String sql="select * from s_selected_course where studentId="+selectedCourse.getStudentId()+" and courseId="+selectedCourse.getCourseId();
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

}
