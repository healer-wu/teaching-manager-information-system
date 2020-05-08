package dao.impl;

import dao.IAdminDao;
import domain.Admin;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/20 - 10:02
 */
public class AdminDaoImpl implements IAdminDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    private Admin admin=new Admin();
    @Override
    public Admin adminLogin(String name, String password) {
        int i=0;
//        String sql="select * from s_admin where name=? and password=?";
        String sql1="select * from s_admin";
//        Admin admin = template.queryForObject(sql, new BeanPropertyRowMapper<Admin>(Admin.class), name, password);
        List<Admin> query = template.query(sql1, new BeanPropertyRowMapper<Admin>(Admin.class));
        for (Admin q:query) {
            if((q.getName().equals(name)) && (q.getPassword().equals(password))){
                admin.setId(q.getId());
                admin.setName(q.getName());
                admin.setStatus(q.getStatus());
                admin.setPassword(q.getPassword());
                i=1;
                break;
            }
        }
        if(i==0)
            admin=null;
        return admin;
    }
    @Override
    public boolean editPassword(Admin admin,String newPassword) {
        String sql = "update s_admin set password = '"+newPassword+"' where id = " + admin.getId();
        int update = template.update(sql);
        return update>=1?true:false;
    }
}
