package dao.impl;

import dao.IClazzDao;
import domain.Clazz;
import domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;
import utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wu
 * @date 2020/4/21 - 20:09
 */
public class ClazzDaoImpl implements IClazzDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
//    private Clazz clazz=new Clazz();
    @Override
    public List<Clazz> getClazzList(Clazz clazz, Page page) {
        String sql=sql ="select * from s_class";
        List<Object> list=new ArrayList<Object>();
        if(!StringUtil.isEmpty(clazz.getName())){
            sql +=" where name like ?";
            list.add("%"+clazz.getName()+"%");
        }
        list.add(page.getStart());
        list.add(page.getPageSize());
        sql+=" limit ? , ?";
        List<Clazz> query = template.query(sql, new BeanPropertyRowMapper<Clazz>(Clazz.class), list.toArray());
        return query;
    }

    @Override
    public int getClazzListTotal(Clazz clazz) {
        String sql=sql ="select count(*) as total from s_class";
        List<Object> list=new ArrayList<Object>();
        if(!StringUtil.isEmpty(clazz.getName())){
            sql +=" where name like ?";
            list.add("%"+clazz.getName()+"%");
        }
        Integer integer = template.queryForObject(sql, Integer.class, list.toArray());
        return integer;
    }

    @Override
    public boolean addClazz(Clazz clazz) {
        String sql="insert into s_class(id,name,info) values(NULL,?,?)";
        int update = template.update(sql, clazz.getName(), clazz.getInfo());
        if(update>=1)
            return true;
        else
            return false;
    }

    @Override
    public boolean deleteClazz(int id) {
        String sql="delete from s_class where id = ?";
        int update = template.update(sql, id);
        return update>=1? true:false;
    }

    @Override
    public boolean editClazz(Clazz clazz) {
        String sql="update s_class set name=? , info=? where id=?";
        int update = template.update(sql, clazz.getName(), clazz.getInfo(), clazz.getId());
        return update>=1? true:false;
    }
}
