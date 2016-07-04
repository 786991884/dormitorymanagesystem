package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.ResourceBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;
import cn.edu.sxau.dormitorymanage.dao.ResourceDao;
import cn.edu.sxau.dormitorymanage.dao.ResourceTypeDao;
import cn.edu.sxau.dormitorymanage.dao.UserDao;
import cn.edu.sxau.dormitorymanage.model.Resource;
import cn.edu.sxau.dormitorymanage.model.Role;
import cn.edu.sxau.dormitorymanage.model.Tuser;
import cn.edu.sxau.dormitorymanage.service.ResourceService;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements ResourceService {
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	@Autowired
	private UserDao userDao;

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Resource> resources = null;
		List<Tree> trees = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceTypeId", 0);// 菜单类型的资源

		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type join fetch r.roles role join role.tusers user where type.id = :resourceTypeId and user.id = :userId order by r.seq", params);
		} else {
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type where type.id = :resourceTypeId order by r.seq", params);
		}

		if (resources != null && resources.size() > 0) {
			for (Resource r : resources) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getResource() != null) {
					tree.setPid(r.getResource().getId());
				}
				tree.setText(r.getName());
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				trees.add(tree);
			}
		}
		return trees;
	}

	@Override
	public List<Tree> allTree(SessionInfo sessionInfo) {
		List<Resource> resources = null;
		List<Tree> trees = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的资源
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type join fetch r.roles role join role.tusers user where user.id = :userId order by r.seq", params);
		} else {
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type order by r.seq", params);
		}

		if (resources != null && resources.size() > 0) {
			for (Resource r : resources) {
				Tree tree = new Tree();
				BeanUtils.copyProperties(r, tree);
				if (r.getResource() != null) {
					tree.setPid(r.getResource().getId());
				}
				tree.setText(r.getName());
				tree.setIconCls(r.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", r.getUrl());
				tree.setAttributes(attr);
				trees.add(tree);
			}
		}
		return trees;
	}

	@Override
	public List<ResourceBean> treeGrid(SessionInfo sessionInfo) {
		List<Resource> resources = null;
		List<ResourceBean> resourceBeans = new ArrayList<ResourceBean>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type join fetch r.roles role join role.tusers user where user.id = :userId order by r.seq", params);
		} else {
			resources = resourceDao.find("select distinct r from Resource r join fetch r.resourceType type order by r.seq", params);
		}

		if (resources != null && resources.size() > 0) {
			for (Resource resource : resources) {
				ResourceBean resourceBean = new ResourceBean();
				BeanUtils.copyProperties(resource, resourceBean);
				if (resource.getResource() != null) {
					resourceBean.setPid(resource.getResource().getId());
					resourceBean.setPname(resource.getResource().getName());
				}
				resourceBean.setTypeId(resource.getResourceType().getId());
				resourceBean.setTypeName(resource.getResourceType().getName());
				if (resource.getIcon() != null && !resource.getIcon().equalsIgnoreCase("")) {
					resourceBean.setIconCls(resource.getIcon());
				}
				resourceBeans.add(resourceBean);
			}
		}

		return resourceBeans;
	}

	@Override
	public void add(ResourceBean resourceBean, SessionInfo sessionInfo) {
		Resource resource = new Resource();
		BeanUtils.copyProperties(resourceBean, resource);
		if (resourceBean.getPid() != null) {
			resource.setResource(resourceDao.getById(Resource.class, resourceBean.getPid()));
		}
		if (resourceBean.getTypeId() != null) {
			resource.setResourceType(resourceTypeDao.getById(resourceBean.getTypeId()));
		}
		if (resourceBean.getIconCls() != null && !resourceBean.getIconCls().equalsIgnoreCase("")) {
			resource.setIcon(resourceBean.getIconCls());
		}
		resourceDao.save(resource);

		// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		Tuser user = userDao.getById(Tuser.class, sessionInfo.getId());
		Set<Role> roles = user.getRoles();
		for (Role r : roles) {
			r.getResources().add(resource);
		}
	}

	@Override
	public void delete(Integer id) {
		Resource r = resourceDao.getById(Resource.class, id);
		del(r);
		r.getRoles().clear();
	}

	private void del(Resource t) {
		if (t.getResources() != null && t.getResources().size() > 0) {
			for (Resource r : t.getResources()) {
				del(r);
				r.getRoles().clear();
			}
		}
		resourceDao.delete(t);
	}

	@Override
	public void edit(ResourceBean resourceBean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", resourceBean.getId());
		Resource resource = resourceDao.getByHql("select distinct r from Resource r where r.id = :id", params);
		if (resource != null) {
			BeanUtils.copyProperties(resourceBean, resource);
			if (resourceBean.getTypeId() != null) {
				resource.setResourceType(resourceTypeDao.getById(resourceBean.getTypeId()));// 赋值资源类型
			}
			if (resourceBean.getIconCls() != null && !resourceBean.getIconCls().equalsIgnoreCase("")) {
				resource.setIcon(resourceBean.getIconCls());
			}
			if (resourceBean.getPid() != null) {// 说明前台选中了上级资源
				Resource pt = resourceDao.getById(Resource.class, resourceBean.getPid());
				isChildren(resource, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				resource.setResource(pt);
			} else {
				resource.setResource(null);// 前台没有选中上级资源，所以就置空
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
	private boolean isChildren(Resource t, Resource pt) {
		if (pt != null && pt.getResource() != null) {
			if (pt.getResource().getId().equals(t.getId())) {
				pt.setResource(null);
				return true;
			} else {
				return isChildren(t, pt.getResource());
			}
		}
		return false;
	}

	@Override
	public ResourceBean get(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Resource resource = resourceDao.getByHql("from Resource r left join fetch r.resource resource left join fetch r.resourceType resourceType where r.id = :id", params);
		ResourceBean resourceBean = new ResourceBean();
		BeanUtils.copyProperties(resource, resourceBean);
		if (resource.getResource() != null) {
			resourceBean.setPid(resource.getResource().getId());
			resourceBean.setPname(resource.getResource().getName());
		}
		resourceBean.setTypeId(resource.getResourceType().getId());
		resourceBean.setTypeName(resource.getResourceType().getName());
		if (resource.getIcon() != null && !resource.getIcon().equalsIgnoreCase("")) {
			resourceBean.setIconCls(resource.getIcon());
		}
		return resourceBean;
	}

}
