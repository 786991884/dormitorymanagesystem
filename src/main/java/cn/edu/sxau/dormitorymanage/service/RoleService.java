package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.RoleBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;

public interface RoleService {
	/**
	 * 保存角色
	 * 
	 * @param roleBean
	 */
	public void add(RoleBean roleBean, SessionInfo sessionInfo);

	/**
	 * 获得角色
	 * 
	 * @param id
	 * @return
	 */
	public RoleBean get(Integer id);

	/**
	 * 编辑角色
	 * 
	 * @param roleBean
	 */
	public void edit(RoleBean roleBean);

	/**
	 * 获得角色treeGrid
	 * 
	 * @return
	 */
	public List<RoleBean> treeGrid(SessionInfo sessionInfo);

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 获得角色树(只能看到自己拥有的角色)
	 * 
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);

	/**
	 * 获得角色树
	 * 
	 * @return
	 */
	public List<Tree> allTree();

	/**
	 * 为角色授权
	 * 
	 * @param roleBean
	 */
	public void grant(RoleBean roleBean);
}
