package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.dao.ResourceDao;
import cn.edu.sxau.dormitorymanage.dao.ResourceTypeDao;
import cn.edu.sxau.dormitorymanage.dao.RoleDao;
import cn.edu.sxau.dormitorymanage.dao.UserDao;
import cn.edu.sxau.dormitorymanage.model.Resource;
import cn.edu.sxau.dormitorymanage.model.ResourceType;
import cn.edu.sxau.dormitorymanage.model.Role;
import cn.edu.sxau.dormitorymanage.model.Tuser;
import cn.edu.sxau.dormitorymanage.service.InitService;

@Service
public class InitServiceImpl implements InitService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;

	@Override
	synchronized public void init() {
		initResourceType();// 初始化资源类型
		initResource();// 初始化资源
		initRole();// 初始化角色
		initUser();// 初始化用户
	}

	private void initResource() {
		ResourceType menuType = resourceTypeDao.getById(ResourceType.class, 0);// 菜单类型
		ResourceType funType = resourceTypeDao.getById(ResourceType.class, 1);// 功能类型

		Resource xtgl = new Resource();
		xtgl.setName("系统管理");
		xtgl.setResourceType(menuType);
		xtgl.setSeq(0);
		xtgl.setIcon("plugin");
		resourceDao.saveOrUpdate(xtgl);

		Resource zygl = new Resource();
		zygl.setName("资源管理");
		zygl.setResourceType(menuType);
		zygl.setResource(xtgl);
		zygl.setSeq(1);
		zygl.setUrl("resourceAction!manager.action");
		zygl.setIcon("database_gear");
		zygl.setRemark("管理系统中所有的菜单或功能");
		resourceDao.saveOrUpdate(zygl);

		Resource zyglTreeGrid = new Resource();
		zyglTreeGrid.setName("资源表格");
		zyglTreeGrid.setResourceType(funType);
		zyglTreeGrid.setResource(zygl);
		zyglTreeGrid.setSeq(1);
		zyglTreeGrid.setUrl("resourceAction!treeGrid.action");
		zyglTreeGrid.setIcon("wrench");
		zyglTreeGrid.setRemark("显示资源列表");
		resourceDao.saveOrUpdate(zyglTreeGrid);

		Resource zyglMenu = new Resource();
		zyglMenu.setName("功能菜单");
		zyglMenu.setResourceType(funType);
		zyglMenu.setResource(zygl);
		zyglMenu.setSeq(2);
		zyglMenu.setUrl("resourceAction!tree.action");
		zyglMenu.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglMenu);

		Resource zyglAddPage = new Resource();
		zyglAddPage.setName("添加资源页面");
		zyglAddPage.setResourceType(funType);
		zyglAddPage.setResource(zygl);
		zyglAddPage.setSeq(3);
		zyglAddPage.setUrl("resourceAction!addPage.action");
		zyglAddPage.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglAddPage);

		Resource zyglAdd = new Resource();
		zyglAdd.setName("添加资源");
		zyglAdd.setResourceType(funType);
		zyglAdd.setResource(zygl);
		zyglAdd.setSeq(4);
		zyglAdd.setUrl("resourceAction!add.action");
		zyglAdd.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglAdd);

		Resource zyglEditPage = new Resource();
		zyglEditPage.setName("编辑资源页面");
		zyglEditPage.setResourceType(funType);
		zyglEditPage.setResource(zygl);
		zyglEditPage.setSeq(5);
		zyglEditPage.setUrl("resourceAction!editPage.action");
		zyglEditPage.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglEditPage);

		Resource zyglEdit = new Resource();
		zyglEdit.setName("编辑资源");
		zyglEdit.setResourceType(funType);
		zyglEdit.setResource(zygl);
		zyglEdit.setSeq(6);
		zyglEdit.setUrl("resourceAction!edit.action");
		zyglEdit.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglEdit);

		Resource zyglDelete = new Resource();
		zyglDelete.setName("删除资源");
		zyglDelete.setResourceType(funType);
		zyglDelete.setResource(zygl);
		zyglDelete.setSeq(7);
		zyglDelete.setUrl("resourceAction!delete.action");
		zyglDelete.setIcon("wrench");
		resourceDao.saveOrUpdate(zyglDelete);

		Resource jsgl = new Resource();
		jsgl.setName("角色管理");
		jsgl.setResourceType(menuType);
		jsgl.setResource(xtgl);
		jsgl.setSeq(2);
		jsgl.setUrl("roleAction!manager.action");
		jsgl.setIcon("tux");
		resourceDao.saveOrUpdate(jsgl);

		Resource jsglTreeGrid = new Resource();
		jsglTreeGrid.setName("角色表格");
		jsglTreeGrid.setResourceType(funType);
		jsglTreeGrid.setResource(jsgl);
		jsglTreeGrid.setSeq(1);
		jsglTreeGrid.setUrl("roleAction!treeGrid.action");
		jsglTreeGrid.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglTreeGrid);

		Resource jsglAddPage = new Resource();
		jsglAddPage.setName("添加角色页面");
		jsglAddPage.setResourceType(funType);
		jsglAddPage.setResource(jsgl);
		jsglAddPage.setSeq(2);
		jsglAddPage.setUrl("roleAction!addPage.action");
		jsglAddPage.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglAddPage);

		Resource jsglAdd = new Resource();
		jsglAdd.setName("添加角色");
		jsglAdd.setResourceType(funType);
		jsglAdd.setResource(jsgl);
		jsglAdd.setSeq(3);
		jsglAdd.setUrl("roleAction!add.action");
		jsglAdd.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglAdd);

		Resource jsglEditPage = new Resource();
		jsglEditPage.setName("编辑角色页面");
		jsglEditPage.setResourceType(funType);
		jsglEditPage.setResource(jsgl);
		jsglEditPage.setSeq(4);
		jsglEditPage.setUrl("roleAction!editPage.action");
		jsglEditPage.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglEditPage);

		Resource jsglEdit = new Resource();
		jsglEdit.setName("编辑角色");
		jsglEdit.setResourceType(funType);
		jsglEdit.setResource(jsgl);
		jsglEdit.setSeq(5);
		jsglEdit.setUrl("roleAction!edit.action");
		jsglEdit.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglEdit);

		Resource jsglDelete = new Resource();
		jsglDelete.setName("删除角色");
		jsglDelete.setResourceType(funType);
		jsglDelete.setResource(jsgl);
		jsglDelete.setSeq(6);
		jsglDelete.setUrl("roleAction!delete.action");
		jsglDelete.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglDelete);

		Resource jsglGrantPage = new Resource();
		jsglGrantPage.setName("角色授权页面");
		jsglGrantPage.setResourceType(funType);
		jsglGrantPage.setResource(jsgl);
		jsglGrantPage.setSeq(7);
		jsglGrantPage.setUrl("roleAction!grantPage.action");
		jsglGrantPage.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglGrantPage);

		Resource jsglGrant = new Resource();
		jsglGrant.setName("角色授权");
		jsglGrant.setResourceType(funType);
		jsglGrant.setResource(jsgl);
		jsglGrant.setSeq(8);
		jsglGrant.setUrl("roleAction!grant.action");
		jsglGrant.setIcon("wrench");
		resourceDao.saveOrUpdate(jsglGrant);

		Resource yhgl = new Resource();
		yhgl.setName("用户管理");
		yhgl.setResourceType(menuType);
		yhgl.setResource(xtgl);
		yhgl.setSeq(3);
		yhgl.setUrl("userAction!manager.action");
		yhgl.setIcon("status_online");
		resourceDao.saveOrUpdate(yhgl);

		Resource yhglDateGrid = new Resource();
		yhglDateGrid.setName("用户表格");
		yhglDateGrid.setResourceType(funType);
		yhglDateGrid.setResource(yhgl);
		yhglDateGrid.setSeq(1);
		yhglDateGrid.setUrl("userAction!dataGrid.action");
		yhglDateGrid.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglDateGrid);

		Resource yhglAddPage = new Resource();
		yhglAddPage.setName("添加用户页面");
		yhglAddPage.setResourceType(funType);
		yhglAddPage.setResource(yhgl);
		yhglAddPage.setSeq(2);
		yhglAddPage.setUrl("userAction!addPage.action");
		yhglAddPage.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglAddPage);

		Resource yhglAdd = new Resource();
		yhglAdd.setName("添加用户");
		yhglAdd.setResourceType(funType);
		yhglAdd.setResource(yhgl);
		yhglAdd.setSeq(3);
		yhglAdd.setUrl("userAction!add.action");
		yhglAdd.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglAdd);

		Resource yhglEditPage = new Resource();
		yhglEditPage.setName("编辑用户页面");
		yhglEditPage.setResourceType(funType);
		yhglEditPage.setResource(yhgl);
		yhglEditPage.setSeq(4);
		yhglEditPage.setUrl("userAction!editPage.action");
		yhglEditPage.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglEditPage);

		Resource yhglEdit = new Resource();
		yhglEdit.setName("编辑用户");
		yhglEdit.setResourceType(funType);
		yhglEdit.setResource(yhgl);
		yhglEdit.setSeq(5);
		yhglEdit.setUrl("userAction!edit.action");
		yhglEdit.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglEdit);

		Resource yhglDelete = new Resource();
		yhglDelete.setName("删除用户");
		yhglDelete.setResourceType(funType);
		yhglDelete.setResource(yhgl);
		yhglDelete.setSeq(6);
		yhglDelete.setUrl("userAction!delete.action");
		yhglDelete.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglDelete);

		Resource yhglBatchDelete = new Resource();
		yhglBatchDelete.setName("批量删除用户");
		yhglBatchDelete.setResourceType(funType);
		yhglBatchDelete.setResource(yhgl);
		yhglBatchDelete.setSeq(7);
		yhglBatchDelete.setUrl("userAction!batchDelete.action");
		yhglBatchDelete.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglBatchDelete);

		Resource yhglGrantPage = new Resource();
		yhglGrantPage.setName("用户授权页面");
		yhglGrantPage.setResourceType(funType);
		yhglGrantPage.setResource(yhgl);
		yhglGrantPage.setSeq(8);
		yhglGrantPage.setUrl("userAction!grantPage.action");
		yhglGrantPage.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglGrantPage);

		Resource yhglGrant = new Resource();
		yhglGrant.setName("用户授权");
		yhglGrant.setResourceType(funType);
		yhglGrant.setResource(yhgl);
		yhglGrant.setSeq(9);
		yhglGrant.setUrl("userAction!grant.action");
		yhglGrant.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglGrant);

		Resource yhglEditPwdPage = new Resource();
		yhglEditPwdPage.setName("用户修改密码页面");
		yhglEditPwdPage.setResourceType(funType);
		yhglEditPwdPage.setResource(yhgl);
		yhglEditPwdPage.setSeq(10);
		yhglEditPwdPage.setUrl("userAction!editPwdPage.action");
		yhglEditPwdPage.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglEditPwdPage);

		Resource yhglEditPwd = new Resource();
		yhglEditPwd.setName("用户修改密码");
		yhglEditPwd.setResourceType(funType);
		yhglEditPwd.setResource(yhgl);
		yhglEditPwd.setSeq(11);
		yhglEditPwd.setUrl("userAction!editPwd.action");
		yhglEditPwd.setIcon("wrench");
		resourceDao.saveOrUpdate(yhglEditPwd);

		Resource wjgl = new Resource();
		wjgl.setName("文件管理");
		wjgl.setResourceType(funType);
		wjgl.setResource(xtgl);
		wjgl.setSeq(6);
		wjgl.setUrl("");
		wjgl.setIcon("server_database.action");
		resourceDao.saveOrUpdate(wjgl);

		Resource chart = new Resource();
		chart.setName("图表管理");
		chart.setResourceType(menuType);
		chart.setSeq(7);
		chart.setIcon("chart_bar");
		resourceDao.saveOrUpdate(chart);

		Resource userCreateDatetimeChart = new Resource();
		userCreateDatetimeChart.setName("用户图表");
		userCreateDatetimeChart.setResourceType(menuType);
		userCreateDatetimeChart.setUrl("chartAction!userCreateDatetimeChart.action");
		userCreateDatetimeChart.setSeq(1);
		userCreateDatetimeChart.setIcon("chart_curve");
		userCreateDatetimeChart.setResource(chart);
		resourceDao.saveOrUpdate(userCreateDatetimeChart);

	}

	private void initResourceType() {
		ResourceType t = new ResourceType();
		t.setId(0);
		t.setName("菜单类型");
		resourceTypeDao.saveOrUpdate(t);

		ResourceType t2 = new ResourceType();
		t2.setId(1);
		t2.setName("功能类型");
		resourceTypeDao.saveOrUpdate(t2);
	}

	private void initRole() {
		Role superAdmin = new Role();
		superAdmin.setName("超管");
		superAdmin.getResources().addAll(resourceDao.find("from Resource r"));// 让超管可以访问所有资源
		superAdmin.setSeq(0);
		superAdmin.setRemark("超级管理员角色，拥有系统中所有的资源访问权限");
		roleDao.saveOrUpdate(superAdmin);

		Role zyAdmin = new Role();
		zyAdmin.setName("资源管理员");
		zyAdmin.setRole(superAdmin);// 父级是超管
		zyAdmin.setSeq(1);
		zyAdmin.getResources().addAll(resourceDao.find("from Resource r where r.resource.name in ('资源管理') or r.name in ('资源管理')"));
		roleDao.saveOrUpdate(zyAdmin);

		Role jsAdmin = new Role();
		jsAdmin.setName("角色管理员");
		jsAdmin.setRole(superAdmin);// 父级是超管
		jsAdmin.setSeq(2);
		jsAdmin.getResources().addAll(resourceDao.find("from Resource r where r.resource.name in ('角色管理') or r.name in ('角色管理')"));
		roleDao.saveOrUpdate(jsAdmin);

		Role yhAdmin = new Role();
		yhAdmin.setName("用户管理员");
		yhAdmin.setRole(superAdmin);// 父级是超管
		yhAdmin.setSeq(3);
		yhAdmin.getResources().addAll(resourceDao.find("from Resource r where r.resource.name in ('用户管理') or r.name in ('用户管理')"));
		roleDao.saveOrUpdate(yhAdmin);

	}

	private void initUser() {
		List<Tuser> l = userDao.find("from Tuser t where t.name in ('徐冰浩','admin1','admin2','admin3')");
		if (l != null && l.size() > 0) {
			for (Tuser user : l) {
				userDao.delete(user);
			}
		}

		Tuser admin = new Tuser();
		admin.setName("徐冰浩");
		admin.setPwd("123456");
		admin.setCreatedatetime(new Date());
		admin.getRoles().addAll(roleDao.find("from Role r"));// 给用户赋予所有角色
		userDao.saveOrUpdate(admin);

		Tuser admin1 = new Tuser();
		admin1.setName("admin1");
		admin1.setPwd("123456");
		admin1.setCreatedatetime(new Date());
		admin1.getRoles().addAll(roleDao.find("from Role r where r.name = '资源管理员'"));// 给用户赋予资源管理员角色
		userDao.saveOrUpdate(admin1);

		Tuser admin2 = new Tuser();
		admin2.setName("admin2");
		admin2.setPwd("123456");
		admin2.setCreatedatetime(new Date());
		admin2.getRoles().addAll(roleDao.find("from Role r where r.name = '角色管理员'"));// 给用户赋予角色管理员角色
		userDao.saveOrUpdate(admin2);

		Tuser admin3 = new Tuser();
		admin3.setName("admin3");
		admin3.setPwd("123456");
		admin3.setCreatedatetime(new Date());
		admin3.getRoles().addAll(roleDao.find("from Role r where r.id = '用户管理员'"));// 给用户赋予用户管理员角色
		userDao.saveOrUpdate(admin3);

	}
}
