package dao.impl;

import dao.IStudentDao;
import domain.Admin;
import domain.Page;
import domain.Student;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import utils.JDBCUtils;
import utils.StringUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 * @date 2020/4/25 - 18:01
 */
public class StudentDaoImpl implements IStudentDao {
       private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public boolean addStudent(Student student) {
        String sql = "insert into s_student values(null,?,?,?,?,?,?,?,null)";
        int update = template.update(sql, student.getSn(), student.getName(), student.getPassword(), student.getClass_id(), student.getSex(), student.getMobile(), student.getQq());
        if (update >= 1)
            return true;
        else
            return false;
    }

    //    public List<Student> getStudentList(Student student, Page page) {
//        String sql = "select * from s_student where 1=1 ";
//        List<Student> students = new ArrayList<Student>();
////        List<Integer> list = new ArrayList<>();
//        List<Object> list = new ArrayList<>();
//        Connection connection=null;
//        PreparedStatement preparedStatement =null;
//        ResultSet resultSet =null;
////        int j=0;
//        try {
//            connection = JDBCUtils.getConnection();
//            if (!StringUtil.isEmpty(student.getName())) {
//                  sql += "and name like '%" + student.getName() + "%'";
////                list.add("%" + student.getName() + "%");
////                j=1;
//            }
//            if (student.getClass_id() != 0) {
//                sql += " and class_id= "+student.getClass_id();
////                list.add(String.valueOf(student.getClass_id()));
//            }
//            int start = page.getStart();
//            list.add(start);
//            list.add(page.getPageSize());
//            sql += " limit ? , ?";
//            preparedStatement = connection.prepareStatement(sql);
//            for(int i=0;i<list.size();i++){
//                    preparedStatement.setInt(i+1,(int)list.get(i));
//            }
//            resultSet = preparedStatement.executeQuery();
//            while(resultSet.next()){
//                Student s = new Student();
//                s.setId(resultSet.getInt("id"));
//                s.setClass_id(resultSet.getInt("class_id"));
//                s.setMobile(resultSet.getString("mobile"));
//                s.setName(resultSet.getString("name"));
////                s.setPassword(resultSet.getString("password"));
////                s.setPhoto(resultSet.getBinaryStream("photo"));
//                s.setQq(resultSet.getString("qq"));
//                s.setSex(resultSet.getString("sex"));
//                s.setSn(resultSet.getString("sn"));
//                students.add(s);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                resultSet.close();
//                preparedStatement.close();
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
////        List<Student> query = template.query(sql, new BeanPropertyRowMapper<Student>(Student.class), list.toArray());
////        for(int i=0;i<query.size();i++){
////            query.get(i).setPhoto(null);
////        }
//        return students;
//    }
    @Override
    public List<Student> getStudentList(Student student, Page page) {
        String sql = "select id,class_id,mobile,name,qq,sex,sn from s_student where 1=1 ";
        List<Object> list = new ArrayList<>();
        if (!StringUtil.isEmpty(student.getName())) {
            sql += " and name like ?";
            list.add("%" + student.getName() + "%");
        }
        if (student.getClass_id() != 0) {
            sql += " and class_id= ? ";
            list.add(student.getClass_id());
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql += " limit ? , ?";
        List<Student> query = template.query(sql, new BeanPropertyRowMapper<Student>(Student.class), list.toArray());

        return query;
    }

    @Override
    public int getStudentListTotal(Student student) {
        String sql = "select count(*) as total from s_student where 1=1 ";
        List<Object> list = new ArrayList<>();
        int i = 0;
        if (!StringUtil.isEmpty(student.getName())) {
            sql += " and name like ? ";
            list.add("%" + student.getName() + "%");
            i = 1;
        }
        if (student.getClass_id() != 0) {
            sql += " and class_id= ?";
            list.add(student.getClass_id());
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
    public Student getStudent(int id) {
        String sql = "select id,photo from s_student where id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = new Student();
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student.setPhoto(resultSet.getBinaryStream("photo"));
                student.setId(resultSet.getInt("id"));
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
        return student;
    }

    @Override
    public boolean editStudent(Student student) {
        String sql = "update s_student set name=? , class_id=? , qq=? , mobile=? , sex=?  where id=?";
        int update = template.update(sql, student.getName(), student.getClass_id(), student.getQq(), student.getMobile(), student.getSex(), student.getId());
        return update >= 1 ? true : false;
    }

    @Override
    public boolean editStudentPhoto(Student student) {
        String sql = "update s_student set photo=?  where id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int update = 0;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBinaryStream(1, student.getPhoto());
            preparedStatement.setInt(2, student.getId());
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
    public boolean deleteStudent(String id) {
        String sql = "delete from s_student where id in (" + id + ")";
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
    public Student studentLogin(String sn, String password) {
        Student student = new Student();
        int i=0;
        String sql = "select id,class_id,mobile,name,qq,sex,sn,password from s_student";
        List<Student> query = template.query(sql, new BeanPropertyRowMapper<Student>(Student.class));
        for (Student q:query) {
            if((q.getSn().equals(sn)) && (q.getPassword().equals(password))){
                student.setId(q.getId());
                student.setName(q.getName());
                student.setClass_id(q.getClass_id());
                student.setPassword(q.getPassword());
                student.setMobile(q.getMobile());
                student.setQq(q.getQq());
                student.setSn(q.getSn());
                i=1;
                break;
            }
        }
        if(i==0){
            student=null;
        }
        return student;
    }

    @Override
    public boolean editPassword(Student student, String newPassword) {
        String sql = "update s_student set password = '"+newPassword+"' where id = " + student.getId();
        int update = template.update(sql);
        return update>=1?true:false;
    }
}
