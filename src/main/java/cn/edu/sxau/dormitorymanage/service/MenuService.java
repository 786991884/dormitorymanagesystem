package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.MenuBean;
import cn.edu.sxau.dormitorymanage.model.Tmenu;

public interface MenuService extends BaseService<Tmenu> {
	/**
	 * 异步请求树节点
	 * 
	 * @param id
	 * @return
	 */
	List<MenuBean> getTreeNode(String id);

	/**
	 * 一次加载所有树节点
	 * 
	 * @return
	 */
	List<MenuBean> getAllTreeNode();
}
