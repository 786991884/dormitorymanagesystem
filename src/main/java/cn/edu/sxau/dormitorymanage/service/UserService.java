package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.UserBean;
import cn.edu.sxau.dormitorymanage.model.Tuser;

public interface UserService extends BaseService<Tuser> {
	/**
	 * 添加用户
	 * 
	 * @param userBean
	 * @throws Exception
	 */
	UserBean save(UserBean userBean) throws Exception;

	/**
	 * 用户登录
	 * 
	 * @param user
	 *            里面包含登录名和密码
	 * @return 用户对象
	 */
	UserBean login(UserBean userBean);

	/**
	 * 用户注册
	 * 
	 * @param userBean
	 *            里面包含登录名和密码
	 * @throws Exception
	 */
	void reg(UserBean userBean) throws Exception;

	/**
	 * 获得用户对象
	 * 
	 * @param id
	 * @return
	 */
	UserBean get(Integer id);

	/**
	 * 得到用户数据集合
	 * 
	 * @param userBean
	 * @return
	 */
	List<UserBean> getUserData(UserBean userBean);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param userBean
	 * @return
	 */
	DataGrid datagrid(UserBean userBean);

	/**
	 * 删除用户
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 批量删除用户
	 * 
	 * @param ids
	 */
	void remove(String ids);

	/**
	 * 编辑用户
	 * 
	 * @param user
	 * @throws Exception
	 */
	UserBean edit(UserBean user) throws Exception;

	/**
	 * 编辑用户密码
	 * 
	 * @param user
	 */
	void editPwd(UserBean userBean);

	/**
	 * 修改用户自己的密码
	 * 
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	boolean editCurrentUserPwd(String oldPwd, String pwd);

	/**
	 * 用户登录时的autocomplete
	 * 
	 * @param q
	 *            参数
	 * @return
	 */
	List<UserBean> loginCombobox(UserBean userBean);

	/**
	 * 用户登录时的combogrid
	 * 
	 * @param q
	 * @param ph
	 * @return
	 */
	DataGrid loginCombogrid(UserBean userBean);

	/**
	 * 用户授权
	 * 
	 * @param userBean
	 *            需要userBean.roleIds的属性值
	 */
	public void grant(UserBean userBean);

	/**
	 * 获得用户能访问的资源地址
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	public List<String> resourceList(Integer id);

	/**
	 * 修改用户自己的密码
	 * 
	 * @param sessionInfo
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	public boolean editCurrentUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd);

}
