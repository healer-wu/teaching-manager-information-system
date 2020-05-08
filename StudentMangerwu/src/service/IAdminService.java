package service;

import domain.Admin;

/**
 * @author wu
 * @date 2020/4/20 - 10:15
 */
public interface IAdminService {
    public Admin loginAdmin(String name, String password);
    public boolean editPassword(Admin admin,String newPassword);
}
