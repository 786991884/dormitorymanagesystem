package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.ClazzBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.dao.ClazzDao;
import cn.edu.sxau.dormitorymanage.dao.ProfessionDao;
import cn.edu.sxau.dormitorymanage.dao.StudentDao;
import cn.edu.sxau.dormitorymanage.model.Clazz;
import cn.edu.sxau.dormitorymanage.model.Profession;
import cn.edu.sxau.dormitorymanage.service.ClazzService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class ClazzServiceImpl extends BaseServiceImpl<Clazz> implements ClazzService {
	@Autowired
	private ClazzDao clazzDao;
	@Autowired
	private ProfessionDao professionDao;
	@Autowired
	private StudentDao studentDao;

	@Override
	public ClazzBean save(ClazzBean clazzBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", clazzBean.getName());
		if (clazzDao.count("select count(*) from Clazz c where c.name = :name", params) > 0) {
			throw new Exception("班级名已存在！");
		} else {
			Clazz clazz = new Clazz();
			BeanUtils.copyProperties(clazzBean, clazz);
			if (clazzBean.getProfessionname() != null && !"".equals(clazzBean.getProfessionname())) {
				if (clazzBean.getProfessionname().equals("0")) {
					clazz.setProfession(null);
					clazzBean.setProfessionname("");
				} else {
					Profession p = new Profession();
					p.setId(Integer.parseInt(clazzBean.getProfessionname()));
					clazz.setProfession(p);
					Profession byId = professionDao.getById(Profession.class, p.getId());
					clazzBean.setProfessionname(byId.getName());
				}

			}
			clazzDao.save(clazz);
			BeanUtils.copyProperties(clazz, clazzBean, new String[] { "profession", "students" });
			return clazzBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Clazz c = clazzDao.getById(Clazz.class, nid);
		c.setProfession(null);
		clazzDao.update(c);
		studentDao.executeHql("update Student s set s.clazz=null where s.clazz.id=" + id);
		clazzDao.delete(c);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		for (String nid : nids) {
			Integer id = Integer.parseInt(nid);
			Clazz c = clazzDao.getById(Clazz.class, id);
			c.setProfession(null);
			studentDao.executeHql("update Student s set s.clazz=null where s.clazz.id=" + id);
			clazzDao.update(c);
		}
		String hql = "delete Clazz c where c.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		clazzDao.executeHql(hql);
	}

	@Override
	public ClazzBean edit(ClazzBean clazzBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", clazzBean.getId());
		params.put("name", clazzBean.getName());
		if (clazzDao.count("select count(*) from Clazz c where c.name = :name and c.id != :id", params) > 0) {
			throw new Exception("班级名已存在！");
		} else {
			Clazz clazz = clazzDao.getById(Clazz.class, clazzBean.getId());
			BeanUtils.copyProperties(clazzBean, clazz);
			if (clazzBean.getProfessionname() != null && !"".equals(clazzBean.getProfessionname())) {
				if (clazzBean.getProfessionname().equals("0")) {
					clazz.setProfession(null);
					clazzBean.setProfessionname("");
				} else {
					Profession p = new Profession();
					p.setId(Integer.parseInt(clazzBean.getProfessionname()));
					clazz.setProfession(p);
					Profession byId = professionDao.getById(Profession.class, p.getId());
					clazzBean.setProfessionname(byId.getName());
				}
			} else {
				if (clazz.getProfession() != null) {
					clazzBean.setProfessionname(clazz.getProfession().getName());
				}
			}
		}

		return clazzBean;
	}

	@Override
	public ClazzBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Clazz clazz = clazzDao.getByHql("select distinct c from Clazz c where c.id = :id", params);
		ClazzBean clazzBean = new ClazzBean();
		BeanUtils.copyProperties(clazz, clazzBean, new String[] { "profession", "students" });
		return clazzBean;
	}

	@Override
	public DataGrid datagrid(ClazzBean clazzBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Clazz c where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(clazzBean, hql, params);
		String totalHql = "select count(*) from Clazz c where 1=1 " + addWhere(clazzBean, "", params);
		hql = addOrder(clazzBean, hql);

		List<Clazz> clazzs = clazzDao.find(hql, params, clazzBean.getPage(), clazzBean.getRows());
		List<ClazzBean> clazzBeans = new ArrayList<ClazzBean>();
		changeModel(clazzs, clazzBeans);
		dg.setTotal(clazzDao.count(totalHql, params));
		dg.setRows(clazzBeans);
		return dg;
	}

	private void changeModel(List<Clazz> blist, List<ClazzBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Clazz b : blist) {
				ClazzBean clazzBean = new ClazzBean();
				BeanUtils.copyProperties(b, clazzBean, new String[] { "profession", "students" });
				if (b.getProfession() != null) {
					clazzBean.setProfessionname(b.getProfession().getName());
				}
				nl.add(clazzBean);
			}
		}
	}

	private String addOrder(ClazzBean clazzBean, String hql) {
		if (clazzBean.getSort() != null && clazzBean.getOrder() != null) {
			if (clazzBean.getSort().equals("name")) {
				hql += " order by convert (" + clazzBean.getSort() + ") " + clazzBean.getOrder();
			} else {
				hql += " order by " + clazzBean.getSort() + " " + clazzBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(ClazzBean clazzBean, String hql, Map<String, Object> params) {
		if (clazzBean != null) {
			if (clazzBean.getName() != null && !clazzBean.getName().trim().equals("")) {
				hql += " and c.name like :name";
				params.put("name", "%%" + clazzBean.getName().trim() + "%%");
			}
			if (!StringUtil.isEmpty(clazzBean.getProfessionname())) {
				hql += " and c.profession.name like :professionname";
				params.put("professionname", "%%" + clazzBean.getProfessionname().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<ClazzBean> getClazzs(StudentBean studentBean) {
		String q = studentBean.getQ() == null ? "" : studentBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q + "%%");
		List<Clazz> clazzs = (List<Clazz>) clazzDao.find("from Clazz c where c.name like :name", params, 1, 30);
		List<ClazzBean> clazzBeans = new ArrayList<ClazzBean>();
		changeModel(clazzs, clazzBeans);
		return clazzBeans;
	}

	@Override
	public List<ClazzBean> getClazzData(ClazzBean clazzBean) {
		String hql = "from Clazz c where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(clazzBean, hql, params);
		hql = addOrder(clazzBean, hql);
		List<Clazz> clazzs = clazzDao.find(hql, params, 1, 500);
		List<ClazzBean> clazzBeans = new ArrayList<ClazzBean>();
		changeModel(clazzs, clazzBeans);
		return clazzBeans;
	}

}
