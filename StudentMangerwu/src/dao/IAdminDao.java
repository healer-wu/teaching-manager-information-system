package dao;

import domain.Admin;

/**
 * @author wu
 * @date 2020/4/20 - 8:58
 */
public interface IAdminDao {
    public Admin adminLogin(String name, String password);
    public boolean editPassword(Admin admin,String newPassword);
}
