package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.UserBean;
import cn.edu.sxau.dormitorymanage.dao.ResourceDao;
import cn.edu.sxau.dormitorymanage.dao.RoleDao;
import cn.edu.sxau.dormitorymanage.dao.UserDao;
import cn.edu.sxau.dormitorymanage.model.Resource;
import cn.edu.sxau.dormitorymanage.model.Role;
import cn.edu.sxau.dormitorymanage.model.Tuser;
import cn.edu.sxau.dormitorymanage.service.UserService;
import cn.edu.sxau.dormitorymanage.utils.ConfigUtil;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

import com.opensymphony.xwork2.ActionContext;

@Service
public class UserServiceImpl extends BaseServiceImpl<Tuser> implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public UserBean save(UserBean userBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", userBean.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser tuser = new Tuser();
			BeanUtils.copyProperties(userBean, tuser);
			tuser.setCreatedatetime(new Date());
			tuser.setPwd(userBean.getPwd());
			userDao.save(tuser);
			BeanUtils.copyProperties(tuser, userBean);
			return userBean;
		}

	}

	@Override
	public UserBean login(UserBean userBean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pwd", userBean.getPwd());
		params.put("name", userBean.getName());
		Tuser tuser = userDao.getByHql("from Tuser t where t.name = :name and t.pwd = :pwd", params);
		if (tuser != null) {
			BeanUtils.copyProperties(tuser, userBean);
			return userBean;
		}
		return null;
	}

	public List<UserBean> getUserData(UserBean userBean) {
		String hql = "from Tuser t where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(userBean, hql, params);
		hql = addOrder(userBean, hql);
		List<Tuser> tusers = userDao.find(hql, params, 1, 500);
		List<UserBean> userBeans = new ArrayList<UserBean>();
		changeModel(tusers, userBeans);
		return userBeans;
	}

	@Override
	public DataGrid datagrid(UserBean userBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Tuser t where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(userBean, hql, params);
		String totalHql = "select count(*) " + hql;
		hql = addOrder(userBean, hql);

		List<Tuser> tusers = userDao.find(hql, params, userBean.getPage(), userBean.getRows());
		List<UserBean> userBeans = new ArrayList<UserBean>();
		changeModel(tusers, userBeans);
		dg.setTotal(userDao.count(totalHql, params));
		dg.setRows(userBeans);
		return dg;
	}

	private void changeModel(List<Tuser> tusers, List<UserBean> userBeans) {
		if (tusers != null && tusers.size() > 0) {
			for (Tuser tuser : tusers) {
				UserBean userBean = new UserBean();
				BeanUtils.copyProperties(tuser, userBean);
				Set<Role> roles = tuser.getRoles();
				if (roles != null && !roles.isEmpty()) {
					String roleIds = "";
					String roleNames = "";
					boolean b = false;
					for (Role tr : roles) {
						if (b) {
							roleIds += ",";
							roleNames += ",";
						} else {
							b = true;
						}
						roleIds += tr.getId();
						roleNames += tr.getName();
					}
					userBean.setRoleIds(roleIds);
					userBean.setRoleNames(roleNames);
				}
				userBeans.add(userBean);
			}
		}
	}

	private String addOrder(UserBean userBean, String hql) {
		if (!StringUtil.isEmpty(userBean.getSort()) && !StringUtil.isEmpty(userBean.getOrder())) {
			if (userBean.getSort().equals("name")) {
				hql += " order by convert (" + userBean.getSort() + ") " + userBean.getOrder();
			} else {
				hql += " order by " + userBean.getSort() + " " + userBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(UserBean userBean, String hql, Map<String, Object> params) {
		if (userBean != null) {
			if (!StringUtil.isEmpty(userBean.getName())) {
				hql += " and t.name like :name";
				params.put("name", "%%" + userBean.getName().trim() + "%%");
			}
			if (userBean.getCreatedatetime() != null) {
				hql += " and t.createdatetime >= :createdatetime";
				params.put("createdatetime", userBean.getCreatedatetime());
			}
			if (userBean.getModifydatetime() != null) {
				hql += " and t.modifydatetime >= :modifydatetime";
				params.put("modifydatetime", userBean.getModifydatetime());
			}
		}
		return hql;
	}

	@Override
	public void delete(String id) {
		userDao.delete(userDao.getById(Tuser.class, id));
	}

	@Override
	public void remove(String ids) {
		// for (String id : ids.split(",")) {
		// Tuser u = userDao.get(Tuser.class, id);
		// if (u != null) {
		// userDao.delete(u);
		// }
		// }
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		String[] nids = ids.split(",");
		String hql = "delete Tuser t where t.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			if (nids[i].equals(sessionInfo.getId())) {
				continue;
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		userDao.executeHql(hql);
	}

	@Override
	public UserBean edit(UserBean userBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", userBean.getId());
		params.put("name", userBean.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name and t.id != :id", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser tuser = userDao.getById(Tuser.class, userBean.getId());
			BeanUtils.copyProperties(userBean, tuser, new String[] { "pwd", "createdatetime" });
			tuser.setModifydatetime(new Date());
		}
		return userBean;
	}

	@Override
	public void reg(UserBean userBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", userBean.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser tuser = new Tuser();
			tuser.setName(userBean.getName());
			tuser.setPwd(userBean.getPwd());
			tuser.setCreatedatetime(new Date());
			userDao.save(tuser);
		}
	}

	@Override
	public UserBean get(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser tuser = userDao.getByHql("select distinct t from Tuser t left join fetch t.roles role  where t.id = :id", params);
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(tuser, userBean);
		if (tuser.getRoles() != null && !tuser.getRoles().isEmpty()) {
			String roleIds = "";
			String roleNames = "";
			boolean b = false;
			for (Role role : tuser.getRoles()) {
				if (b) {
					roleIds += ",";
					roleNames += ",";
				} else {
					b = true;
				}
				roleIds += role.getId();
				roleNames += role.getName();
			}
			userBean.setRoleIds(roleIds);
			userBean.setRoleNames(roleNames);
		}
		return userBean;
	}

	@Override
	public void editPwd(UserBean userBean) {
		if (userBean != null && userBean.getPwd() != null && !userBean.getPwd().trim().equalsIgnoreCase("")) {
			Tuser tuser = userDao.getById(Tuser.class, userBean.getId());
			tuser.setPwd(userBean.getPwd());
			tuser.setModifydatetime(new Date());
		}
	}

	@Override
	public boolean editCurrentUserPwd(String oldPwd, String pwd) {
		Tuser tuser = userDao.getById(Tuser.class, ((UserBean) (ActionContext.getContext().getSession().get("user"))).getId());
		if (tuser.getPwd().equalsIgnoreCase(oldPwd)) {// 说明原密码输入正确
			tuser.setPwd(pwd);
			tuser.setModifydatetime(new Date());
			return true;
		}
		return false;
	}

	@Override
	public List<UserBean> loginCombobox(UserBean userBean) {
		if (userBean.getQ() == null) {
			userBean.setQ("");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + userBean.getQ().trim() + "%%");
		List<Tuser> tusers = userDao.find("from Tuser t where t.name like :name order by name", params, 1, 10);
		List<UserBean> userBeans = new ArrayList<UserBean>();
		if (tusers != null && tusers.size() > 0) {
			for (Tuser t : tusers) {
				UserBean u = new UserBean();
				u.setName(t.getName());
				userBeans.add(u);
			}
		}
		return userBeans;
	}

	@Override
	public DataGrid loginCombogrid(UserBean userBean) {
		if (userBean.getQ() == null) {
			userBean.setQ("");
		}
		DataGrid dg = new DataGrid();
		List<UserBean> userBeans = new ArrayList<UserBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + userBean.getQ().trim() + "%%");
		List<Tuser> tusers = userDao.find("from Tuser t where t.name like :name order by " + userBean.getSort() + " " + userBean.getOrder(), params, userBean.getPage(), userBean.getRows());
		if (tusers != null && tusers.size() > 0) {
			for (Tuser t : tusers) {
				UserBean u = new UserBean();
				u.setName(t.getName());
				u.setCreatedatetime(t.getCreatedatetime());
				u.setModifydatetime(t.getModifydatetime());
				userBeans.add(u);
			}
		}
		dg.setRows(userBeans);
		dg.setTotal(userDao.count("select count(*) from Tuser t where t.name like :name", params));
		return dg;
	}

	@Override
	public void grant(UserBean userBean) {
		if (userBean.getIds() != null && userBean.getIds().length() > 0) {
			List<Role> roles = new ArrayList<Role>();
			if (userBean.getRoleIds() != null) {
				for (String roleId : userBean.getRoleIds().split(",")) {
					roles.add(roleDao.getById(Role.class, Integer.valueOf(roleId.trim())));
				}
			}
			for (String id : userBean.getIds().split(",")) {
				if (id != null && !id.equalsIgnoreCase("")) {
					Tuser t = userDao.getById(Tuser.class, Integer.valueOf(id));
					t.setRoles(new HashSet<Role>(roles));
				}
			}
		}
	}

	@Override
	public List<String> resourceList(Integer id) {
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser tuser = userDao.getByHql("from Tuser t join fetch t.roles role join fetch role.resources resource where t.id = :id", params);
		if (tuser != null) {
			Set<Role> roles = tuser.getRoles();
			if (roles != null && !roles.isEmpty()) {
				for (Role role : roles) {
					Set<Resource> resources = role.getResources();
					if (resources != null && !resources.isEmpty()) {
						for (Resource resource : resources) {
							if (resource != null && resource.getUrl() != null) {
								resourceList.add(resource.getUrl());
							}
						}
					}
				}
			}
		}
		return resourceList;
	}

	@Override
	public boolean editCurrentUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd) {
		Tuser u = userDao.getById(Tuser.class, sessionInfo.getId());
		if (u.getPwd().equals(oldPwd)) {// 说明原密码输入正确
			u.setPwd(pwd);
			u.setModifydatetime(new Date());
			return true;
		}
		return false;
	}

}
