package service.impl;

import dao.impl.ClazzDaoImpl;
import domain.Clazz;
import domain.Page;
import service.IClazzService;

import java.util.List;

/**
 * @author wu
 * @date 2020/4/21 - 21:15
 */
public class ClazzServiceImpl implements IClazzService {
    private ClazzDaoImpl clazzDao = new ClazzDaoImpl();
    @Override
    public List<Clazz> getClazzList(Clazz clazz, Page page){
        List<Clazz> clazzList = clazzDao.getClazzList(clazz, page);
        return clazzList;
    }

    @Override
    public int getClazzListTotal(Clazz clazz) {
        return clazzDao.getClazzListTotal(clazz);
    }

    @Override
    public boolean addClazz(Clazz clazz) {
        boolean b = clazzDao.addClazz(clazz);
        return b;
    }

    @Override
    public boolean deleteClazz(int id) {
        return clazzDao.deleteClazz(id);
    }

    @Override
    public boolean editClazz(Clazz clazz) {
        return clazzDao.editClazz(clazz);
    }
}
