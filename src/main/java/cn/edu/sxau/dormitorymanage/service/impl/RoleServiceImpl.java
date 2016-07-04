package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.RoleBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;
import cn.edu.sxau.dormitorymanage.dao.ResourceDao;
import cn.edu.sxau.dormitorymanage.dao.RoleDao;
import cn.edu.sxau.dormitorymanage.dao.UserDao;
import cn.edu.sxau.dormitorymanage.model.Resource;
import cn.edu.sxau.dormitorymanage.model.Role;
import cn.edu.sxau.dormitorymanage.model.Tuser;
import cn.edu.sxau.dormitorymanage.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ResourceDao resourceDao;

	@Override
	public void add(RoleBean roleBean, SessionInfo sessionInfo) {
		Role role = new Role();
		BeanUtils.copyProperties(roleBean, role);
		if (roleBean.getPid() != null) {
			role.setRole(roleDao.getById(Role.class, roleBean.getPid()));
		}
		roleDao.save(role);

		// 刚刚添加的角色，赋予给当前的用户
		Tuser user = userDao.getById(Tuser.class, sessionInfo.getId());
		user.getRoles().add(role);
	}

	@Override
	public RoleBean get(Integer id) {
		RoleBean roleBean = new RoleBean();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Role role = roleDao.getByHql("select distinct r from Role r left join fetch r.resources resource where r.id = :id", params);
		if (role != null) {
			BeanUtils.copyProperties(role, roleBean);
			if (role.getRole() != null) {
				roleBean.setPid(role.getRole().getId());
				roleBean.setPname(role.getRole().getName());
			}
			Set<Resource> s = role.getResources();
			if (s != null && !s.isEmpty()) {
				boolean b = false;
				String ids = "";
				String names = "";
				for (Resource tr : s) {
					if (b) {
						ids += ",";
						names += ",";
					} else {
						b = true;
					}
					ids += tr.getId();
					names += tr.getName();
				}
				roleBean.setResourceIds(ids);
				roleBean.setResourceNames(names);
			}
		}
		return roleBean;
	}

	@Override
	public void edit(RoleBean roleBean) {
		Role role = roleDao.getById(Role.class, roleBean.getId());
		if (role != null) {
			BeanUtils.copyProperties(roleBean, role);
			if (roleBean.getPid() != null && !roleBean.getPid().equals("")) {
				role.setRole(roleDao.getById(Role.class, roleBean.getPid()));
			}
			if (roleBean.getPid() != null && !roleBean.getPid().equals("")) {// 说明前台选中了上级资源
				Role pt = roleDao.getById(Role.class, roleBean.getPid());
				isChildren(role, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				role.setRole(pt);
			} else {
				role.setRole(null);// 前台没有选中上级资源，所以就置空
			}
		}
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点
	 * 
	 * @param t
	 *            当前节点
	 * @param pt
	 *            要修改到的节点
	 * @return
	 */
	private boolean isChildren(Role t, Role pt) {
		if (pt != null && pt.getRole() != null) {
			if (pt.getRole().getId().equals(t.getId())) {
				pt.setRole(null);
				return true;
			} else {
				return isChildren(t, pt.getRole());
			}
		}
		return false;
	}

	@Override
	public List<RoleBean> treeGrid(SessionInfo sessionInfo) {
		List<RoleBean> roleBeans = new ArrayList<RoleBean>();
		List<Role> roles = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的角色
			roles = roleDao.find("select distinct r from Role r left join fetch r.resources resource join fetch r.tusers user where user.id = :userId order by r.seq", params);
		} else {
			roles = roleDao.find("select distinct r from Role r left join fetch r.resources resource order by r.seq");
		}
		if (roles != null && roles.size() > 0) {
			for (Role role : roles) {
				RoleBean roleBean = new RoleBean();
				BeanUtils.copyProperties(role, roleBean);
				roleBean.setIconCls("status_online");
				if (role.getRole() != null) {
					roleBean.setPid(role.getRole().getId());
					roleBean.setPname(role.getRole().getName());
				}
				Set<Resource> s = role.getResources();
				if (s != null && !s.isEmpty()) {
					boolean b = false;
					String ids = "";
					String names = "";
					for (Resource tr : s) {
						if (b) {
							ids += ",";
							names += ",";
						} else {
							b = true;
						}
						ids += tr.getId();
						names += tr.getName();
					}
					roleBean.setResourceIds(ids);
					roleBean.setResourceNames(names);
				}
				roleBeans.add(roleBean);
			}
		}
		return roleBeans;
	}

	@Override
	public void delete(Integer id) {
		Role r = roleDao.getById(Role.class, id);
		del(r);
		r.getTusers().clear();
	}

	private void del(Role role) {
		if (role.getRoles() != null && role.getRoles().size() > 0) {
			for (Role r : role.getRoles()) {
				del(r);
				r.getTusers().clear();
			}
		}
		roleDao.delete(role);
	}

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Role> roles = null;
		List<Tree> trees = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的角色
			roles = roleDao.find("select distinct r from Role r join fetch r.tusers user where user.id = :userId order by r.seq", params);
		} else {
			roles = roleDao.find("from Role r order by r.seq");
		}

		if (roles != null && roles.size() > 0) {
			for (Role t : roles) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(t, tree);
				tree.setText(t.getName());
				tree.setIconCls("status_online");
				if (t.getRole() != null) {
					tree.setPid(t.getRole().getId());
				}
				trees.add(tree);
			}
		}
		return trees;
	}

	@Override
	public List<Tree> allTree() {
		return this.tree(null);
	}

	@Override
	public void grant(RoleBean roleBean) {
		Role role = roleDao.getById(Role.class, roleBean.getId());
		if (roleBean.getResourceIds() != null && !roleBean.getResourceIds().equalsIgnoreCase("")) {
			String ids = "";
			boolean b = false;
			for (String id : roleBean.getResourceIds().split(",")) {
				if (b) {
					ids += ",";
				} else {
					b = true;
				}
				ids += "'" + id + "'";
			}
			role.setResources(new HashSet<Resource>(resourceDao.find("select distinct r from Resource r where r.id in (" + ids + ")")));
		} else {
			role.setResources(null);
		}
	}

}
