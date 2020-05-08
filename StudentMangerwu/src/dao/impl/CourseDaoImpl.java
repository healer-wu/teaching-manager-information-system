package dao.impl;

import dao.ICourseDao;
import domain.Course;
import domain.Page;
import domain.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * @date 2020/5/1 - 11:32
 */
public class CourseDaoImpl implements ICourseDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public boolean addCourse(Course course){
        String sql = "insert into s_course values(null,'"+course.getName()+"',"+course.getTeacherId()+",'"+course.getCourseDate()+"',0,"+course.getMaxNum()+",'"+course.getInfo()+"') ";
        int update = template.update(sql);
        return update>=0?true:false;
    }

    public List<Course> getCourseList(Course course, Page page){
        String sql = "select * from s_course where 1=1 ";
        List<Object> list = new ArrayList<>();
        if (!StringUtil.isEmpty(course.getName())) {
            sql += " and name like ?";
            list.add("%" + course.getName() + "%");
        }
        if (course.getTeacherId() != 0) {
            sql += " and teacherId= ? ";
            list.add(course.getTeacherId());
        }
        if (course.getId() != 0) {
            sql += " and id= ? ";
            list.add(course.getId());
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<Course> query = template.query(sql, new BeanPropertyRowMapper<Course>(Course.class), list.toArray());
        return query;
    }

    public int getCourseListTotal(Course course){
        String sql = "select count(*) as total from s_course where 1=1 ";
        List<Object> list = new ArrayList<>();
        int i = 0;
        if (!StringUtil.isEmpty(course.getName())) {
            sql += " and name like ? ";
            list.add("%" + course.getName() + "%");
            i = 1;
        }
        if (course.getTeacherId() != 0) {
            sql += " and teacherId= ?";
            list.add(course.getTeacherId());
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

    public boolean editCourse(Course course) {
        String sql = "update s_course set name = '"+course.getName()+"',teacherId = "+course.getTeacherId()+",courseDate = '"+course.getCourseDate()+"',maxNum = "+course.getMaxNum()+" ,info = '"+course.getInfo()+"' where id = " + course.getId();
        int update = template.update(sql);
        return update>=1?true:false;
    }
    public boolean deleteCourse(String ids) {
        String sql = "delete from s_course where id in("+ids+")";
        int update = template.update(sql);
        return update>=1?true:false;
    }
    /**
     * 检查该课程是否已选满
     * @param courseId
     * @return
     */
    public boolean isFull(int courseId){
        boolean ret = false;
        String sql = "select * from s_course where selectedNum >= maxNum and id = " + courseId;
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
        return ret;
    }
    /**
     * 更新课程已选人数
     * @param courseId
     */
    public void updateCourseSelectedNum(int courseId){
        String sql ="update s_course set selectedNum = selectedNum+1 where id = " + courseId;
        int update = template.update(sql);
    }
    public void updateCourseSelectedNum(List<Integer> ids) {
        for (int courseId:ids) {
            String sql = "update s_course set selectedNum = selectedNum-1 where id ="+courseId;
            int update = template.update(sql);
        }
    }
    public Course getCourse(int id) {
        String sql = "select * from s_student where id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Course course = new Course();
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                course.setId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
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
        return course;
    }
}
