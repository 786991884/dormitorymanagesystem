package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.ResourceBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;

public interface ResourceService {
	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 
	 * 通过用户ID判断
	 * 
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);

	/**
	 * 获得资源树(包括所有资源类型)
	 * 
	 * 通过用户ID判断
	 * 
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> allTree(SessionInfo sessionInfo);

	/**
	 * 获得资源列表
	 * 
	 * @param sessionInfo
	 * 
	 * @return
	 */
	public List<ResourceBean> treeGrid(SessionInfo sessionInfo);

	/**
	 * 添加资源
	 * 
	 * @param resourceBean
	 * @param sessionInfo
	 */
	public void add(ResourceBean resourceBean, SessionInfo sessionInfo);

	/**
	 * 删除资源
	 * 
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 修改资源
	 * 
	 * @param resourceBean
	 */
	public void edit(ResourceBean resourceBean);

	/**
	 * 获得一个资源
	 * 
	 * @param id
	 * @return
	 */
	public ResourceBean get(Integer id);
}
