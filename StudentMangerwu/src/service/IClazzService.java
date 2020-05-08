package service;

import domain.Clazz;
import domain.Page;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/21 - 21:12
 */
public interface IClazzService {
    public List<Clazz> getClazzList(Clazz clazz, Page page);
    public int getClazzListTotal(Clazz clazz);
    public boolean addClazz(Clazz clazz);
    public boolean deleteClazz(int id);
    public boolean editClazz(Clazz clazz);
}
