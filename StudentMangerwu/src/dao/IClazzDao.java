package dao;

import domain.Clazz;
import domain.Page;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/21 - 20:06
 */
public interface IClazzDao {
    public List<Clazz> getClazzList(Clazz clazz, Page page);
    public int getClazzListTotal(Clazz clazz);
    public boolean addClazz(Clazz clazz);
    public boolean deleteClazz(int id);
    public boolean editClazz(Clazz clazz);
}

