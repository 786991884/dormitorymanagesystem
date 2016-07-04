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
import cn.edu.sxau.dormitorymanage.bean.ProfessionBean;
import cn.edu.sxau.dormitorymanage.dao.ClazzDao;
import cn.edu.sxau.dormitorymanage.dao.CollegeDao;
import cn.edu.sxau.dormitorymanage.dao.ProfessionDao;
import cn.edu.sxau.dormitorymanage.model.College;
import cn.edu.sxau.dormitorymanage.model.Profession;
import cn.edu.sxau.dormitorymanage.service.ProfessionService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class ProfessionServiceImpl extends BaseServiceImpl<Profession> implements ProfessionService {
	@Autowired
	private ProfessionDao professionDao;
	@Autowired
	private CollegeDao collegeDao;
	@Autowired
	private ClazzDao clazzDao;

	@Override
	public ProfessionBean save(ProfessionBean professionBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", professionBean.getName());
		if (professionDao.count("select count(*) from Profession p where p.name = :name", params) > 0) {
			throw new Exception("专业名已存在！");
		} else {
			Profession profession = new Profession();
			BeanUtils.copyProperties(professionBean, profession);
			if (professionBean.getCollegename() != null && !"".equals(professionBean.getCollegename())) {
				if (professionBean.getCollegename().equals("0")) {
					profession.setCollege(null);
					professionBean.setCollegename("");
				} else {
					College c = new College();
					c.setId(Integer.parseInt(professionBean.getCollegename()));
					profession.setCollege(c);
					College byId = collegeDao.getById(College.class, c.getId());
					professionBean.setCollegename(byId.getName());
				}
			}
			professionDao.save(profession);
			BeanUtils.copyProperties(profession, professionBean, new String[] { "college", "clazzs" });
			return professionBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Profession profession = professionDao.getById(Profession.class, nid);
		profession.setCollege(null);
		clazzDao.executeHql("update Clazz c set c.profession=null where c.profession.id=" + nid);
		professionDao.delete(profession);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		for (String nid : nids) {
			Integer id = Integer.parseInt(nid);
			Profession profession = professionDao.getById(Profession.class, id);
			profession.setCollege(null);
			clazzDao.executeHql("update Clazz c set c.profession=null where c.profession.id=" + id);
		}
		String hql = "delete Profession p where p.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		professionDao.executeHql(hql);
	}

	@Override
	public ProfessionBean edit(ProfessionBean professionBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", professionBean.getId());
		params.put("name", professionBean.getName());
		if (professionDao.count("select count(*) from Profession p where p.name = :name and p.id != :id", params) > 0) {
			throw new Exception("专业名已存在！");
		} else {
			Profession profession = professionDao.getById(Profession.class, professionBean.getId());
			BeanUtils.copyProperties(professionBean, profession);
			if (professionBean.getCollegename() != null && !"".equals(professionBean.getCollegename())) {
				if (professionBean.getCollegename().equals("0")) {
					profession.setCollege(null);
					professionBean.setCollegename("");
				} else {
					College c = new College();
					c.setId(Integer.parseInt(professionBean.getCollegename()));
					profession.setCollege(c);
					College byId = collegeDao.getById(College.class, c.getId());
					professionBean.setCollegename(byId.getName());
				}
			} else {
				if (profession.getCollege() != null) {
					professionBean.setCollegename(profession.getCollege().getName());
				}
			}
		}

		return professionBean;
	}

	@Override
	public ProfessionBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Profession profession = professionDao.getByHql("select distinct p from Profession p where c.id = :id", params);
		ProfessionBean professionBean = new ProfessionBean();
		BeanUtils.copyProperties(profession, professionBean, new String[] { "college", "clazzs" });
		return professionBean;
	}

	@Override
	public DataGrid datagrid(ProfessionBean professionBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Profession p where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(professionBean, hql, params);
		String totalHql = "select count(*) from Profession p where 1=1 " + addWhere(professionBean, "", params);
		hql = addOrder(professionBean, hql);

		List<Profession> professions = professionDao.find(hql, params, professionBean.getPage(), professionBean.getRows());
		List<ProfessionBean> professionBeans = new ArrayList<ProfessionBean>();
		changeModel(professions, professionBeans);
		dg.setTotal(professionDao.count(totalHql, params));
		dg.setRows(professionBeans);
		return dg;
	}

	private void changeModel(List<Profession> blist, List<ProfessionBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Profession b : blist) {
				ProfessionBean professionBean = new ProfessionBean();
				BeanUtils.copyProperties(b, professionBean, new String[] { "college", "clazzs" });
				if (b.getCollege() != null) {
					professionBean.setCollegename(b.getCollege().getName());
				}
				nl.add(professionBean);
			}
		}
	}

	private String addOrder(ProfessionBean professionBean, String hql) {
		if (professionBean.getSort() != null && professionBean.getOrder() != null) {
			if (professionBean.getSort().equals("name")) {
				hql += " order by convert (" + professionBean.getSort() + ") " + professionBean.getOrder();
			} else {
				hql += " order by " + professionBean.getSort() + " " + professionBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(ProfessionBean professionBean, String hql, Map<String, Object> params) {
		if (professionBean != null) {
			if (professionBean.getName() != null && !professionBean.getName().trim().equals("")) {
				hql += " and p.name like :name";
				params.put("name", "%%" + professionBean.getName().trim() + "%%");
			}
			if (!StringUtil.isEmpty(professionBean.getCollegename())) {
				hql += " and p.college.name like :collegename";
				params.put("collegename", "%%" + professionBean.getCollegename().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<ProfessionBean> getProfessions(ClazzBean clazzBean) {
		String q = clazzBean.getQ() == null ? "" : clazzBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q + "%%");
		List<Profession> professions = (List<Profession>) professionDao.find("from Profession p where p.name like :name", params, 1, 30);
		List<ProfessionBean> professionBeans = new ArrayList<ProfessionBean>();
		changeModel(professions, professionBeans);
		return professionBeans;
	}

	@Override
	public List<ProfessionBean> getProfessionData(ProfessionBean professionBean) {
		String hql = "from Profession p where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(professionBean, hql, params);
		hql = addOrder(professionBean, hql);
		List<Profession> professions = professionDao.find(hql, params, 1, 500);
		List<ProfessionBean> professionBeans = new ArrayList<ProfessionBean>();
		changeModel(professions, professionBeans);
		return professionBeans;
	}

}
