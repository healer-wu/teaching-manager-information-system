package service.impl;

import dao.impl.AdminDaoImpl;
import domain.Admin;
import service.IAdminService;

/**
 * @author wu
 * @date 2020/4/20 - 10:35
 */
public class AdminServiceImpl implements IAdminService {
    private AdminDaoImpl admindaoimpl=new AdminDaoImpl();
    @Override
    public Admin loginAdmin(String name, String password) {
        Admin admin1 = admindaoimpl.adminLogin(name,password);
        return admin1;
    }

    @Override
    public boolean editPassword(Admin admin, String newPassword) {
        return admindaoimpl.editPassword(admin,newPassword);
    }
}
