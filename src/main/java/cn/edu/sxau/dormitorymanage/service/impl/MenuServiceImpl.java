package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.MenuBean;
import cn.edu.sxau.dormitorymanage.dao.MenuDao;
import cn.edu.sxau.dormitorymanage.model.Tmenu;
import cn.edu.sxau.dormitorymanage.service.MenuService;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Tmenu> implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<MenuBean> getTreeNode(String id) {
		List<MenuBean> menuBeans = new ArrayList<MenuBean>();
		String hql = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (id == null || id.equals("")) {
			// 查询所有根节点
			hql = "from Tmenu t where t.tmenu is null";
		} else {
			// 异步加载当前id下的子节点
			hql = "from Tmenu t where t.tmenu.id = :id ";
			params.put("id", id);
		}
		List<Tmenu> tmenus = menuDao.find(hql, params);
		if (tmenus != null && tmenus.size() > 0) {
			for (Tmenu tmunu : tmenus) {
				MenuBean menuBean = new MenuBean();
				BeanUtils.copyProperties(tmunu, menuBean);
				Set<Tmenu> set = tmunu.getTmenus();
				if (set != null && !set.isEmpty()) {
					menuBean.setState("closed");// 节点以文件夹的形式体现
				} else {
					menuBean.setState("open");// 节点以文件的形式体现
				}
				menuBeans.add(menuBean);
			}
		}
		return menuBeans;
	}

	@Override
	public List<MenuBean> getAllTreeNode() {
		List<MenuBean> menuBeans = new ArrayList<MenuBean>();
		String hql = "from Tmenu t";
		List<Tmenu> tmenus = menuDao.find(hql);
		if (tmenus != null && tmenus.size() > 0) {
			for (Tmenu tmenu : tmenus) {
				MenuBean menuBean = new MenuBean();
				BeanUtils.copyProperties(tmenu, menuBean);
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("url", tmenu.getUrl());
				menuBean.setAttributes(attributes);
				Tmenu tm = tmenu.getTmenu();// 获得当前节点的父节点对象
				if (tm != null) {
					menuBean.setPid(tm.getId());
				}
				menuBeans.add(menuBean);
			}
		}
		return menuBeans;
	}

}
