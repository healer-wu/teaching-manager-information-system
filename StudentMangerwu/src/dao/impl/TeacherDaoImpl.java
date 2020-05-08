package dao.impl;

import dao.ITeacherDao;
import domain.Page;
import domain.Student;
import domain.Teacher;
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
 * @date 2020/4/29 - 8:43
 */
public class TeacherDaoImpl implements ITeacherDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public boolean addTeacher(Teacher teacher) {
        String sql = "insert into s_teacher values(null,?,?,?,?,?,?,?,null)";
        int update = template.update(sql, teacher.getSn(), teacher.getName(), teacher.getPassword(), teacher.getClass_id(), teacher.getSex(), teacher.getMobile(), teacher.getQq());
        if (update >= 1)
            return true;
        else
            return false;
    }

    @Override
    public List<Teacher> getTeacherList(Teacher teacher, Page page) {
        String sql = "select id,class_id,mobile,name,qq,sex,sn from s_teacher where 1=1 ";
        List<Object> list = new ArrayList<>();
        if (!StringUtil.isEmpty(teacher.getName())) {
            sql += " and name like ?";
            list.add("%" + teacher.getName() + "%");
        }
        if (teacher.getClass_id() != 0) {
            sql += " and class_id= ? ";
            list.add(teacher.getClass_id());
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<Teacher> query = template.query(sql, new BeanPropertyRowMapper<Teacher>(Teacher.class), list.toArray());
        return query;
    }

    @Override
    public int getTeacherListTotal(Teacher teacher) {
        String sql = "select count(*) as total from s_teacher where 1=1 ";
        List<Object> list = new ArrayList<>();
        int i = 0;
        if (!StringUtil.isEmpty(teacher.getName())) {
            sql += " and name like ? ";
            list.add("%" + teacher.getName() + "%");
            i = 1;
        }
        if (teacher.getClass_id() != 0) {
            sql += " and class_id= ?";
            list.add(teacher.getClass_id());
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

    @Override
    public boolean editTeacher(Teacher teacher) {
        String sql = "update s_teacher set name=? , class_id=? , qq=? , mobile=? , sex=?  where id=?";
        int update = template.update(sql, teacher.getName(), teacher.getClass_id(), teacher.getQq(), teacher.getMobile(), teacher.getSex(), teacher.getId());
        return update >= 1 ? true : false;
    }

    @Override
    public boolean deleteTeacher(String id) {
        String sql = "delete from s_teacher where id in (" + id + ")";
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

    @Override
    public Teacher getTeacher(int id) {
        String sql = "select photo from s_teacher where id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Teacher teacher = new Teacher();
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teacher.setPhoto(resultSet.getBinaryStream("photo"));
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
        return teacher;
    }

    @Override
    public boolean editTeacherPhoto(Teacher teacher) {
        String sql = "update s_teacher set photo=?  where id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int update = 0;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBinaryStream(1, teacher.getPhoto());
            preparedStatement.setInt(2, teacher.getId());
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

    @Override
    public Teacher teacherLogin(String sn, String password) {
        String sql = "select id,class_id,mobile,name,qq,sex,sn,password from s_teacher";
        int i=0;
        Teacher teacher=new Teacher();
        List<Teacher> query = template.query(sql, new BeanPropertyRowMapper<Teacher>(Teacher.class));
        for (Teacher q:query) {
            if((q.getSn().equals(sn)) && (q.getPassword().equals(password))){
                teacher.setId(q.getId());
                teacher.setName(q.getName());
                teacher.setClass_id(q.getClass_id());
                teacher.setPassword(q.getPassword());
                teacher.setMobile(q.getMobile());
                teacher.setQq(q.getQq());
                teacher.setSn(q.getSn());
                i=1;
                break;
            }
        }
        if(i==0){
            teacher=null;
        }
        return teacher;
    }

    @Override
    public boolean editPassword(Teacher teacher, String newPassword) {
        String sql = "update s_teacher set password = '"+newPassword+"' where id = " + teacher.getId();
        int update = template.update(sql);
        return update>=1?true:false;
    }
}
