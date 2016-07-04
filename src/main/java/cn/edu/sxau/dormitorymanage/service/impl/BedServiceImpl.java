package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.dao.BedDao;
import cn.edu.sxau.dormitorymanage.dao.DormitoryDao;
import cn.edu.sxau.dormitorymanage.dao.StudentDao;
import cn.edu.sxau.dormitorymanage.model.Bed;
import cn.edu.sxau.dormitorymanage.model.Dormitory;
import cn.edu.sxau.dormitorymanage.model.Student;
import cn.edu.sxau.dormitorymanage.service.BedService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class BedServiceImpl extends BaseServiceImpl<Bed> implements BedService {
	@Autowired
	private BedDao bedDao;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private DormitoryDao dormitoryDao;

	@Override
	public BedBean save(BedBean bedBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", bedBean.getName());
		if (bedDao.count("select count(*) from Bed b where b.name = :name", params) > 0) {
			throw new Exception("床位号已存在！");
		} else {
			Bed bed = new Bed();
			BeanUtils.copyProperties(bedBean, bed);
			if (bedBean.getStudentnumber() != null && !"".equals(bedBean.getStudentnumber())) {
				if (bedBean.getStudentnumber().equals("0")) {
					bed.setStudent(null);
					Student s = studentDao.getByHql("from Student s where s.bed.id=" + bedBean.getId());
					if (s != null) {
						s.setBed(null);
					}
					studentDao.update(s);
					bedBean.setStudentnumber("");
				} else {
					Student s = new Student();
					s.setId(Integer.parseInt(bedBean.getStudentnumber()));
					Student byId = studentDao.getById(Student.class, s.getId());
					if (byId.getBed() != null) {
						throw new Exception("该床位已有学生！");
					}
					bed.setStudent(s);
					byId.setBed(bed);
					bedBean.setStudentnumber(byId.getNumber());
				}
			}
			if (bedBean.getDormitoryname() != null && !"".equals(bedBean.getDormitoryname())) {
				if (bedBean.getDormitoryname().equals("0")) {
					bed.setDormitory(null);
					bedBean.setDormitoryname("");
				} else {
					Dormitory d = new Dormitory();
					d.setId(Integer.parseInt(bedBean.getDormitoryname()));
					Dormitory byId = dormitoryDao.getById(Dormitory.class, d.getId());
					bed.setDormitory(d);
					bedBean.setDormitoryname(byId.getNumber());
				}
			}
			bedDao.save(bed);
			BeanUtils.copyProperties(bed, bedBean, new String[] { "student", "dormitory" });
			return bedBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Bed b = bedDao.getById(Bed.class, nid);
		// b.setStudent(null);
		// bedDao.update(b);
		Student s = studentDao.getByHql("from Student s where s.bed.id=" + nid);
		if (s != null) {
			s.setBed(null);
		}
		// studentDao.update(s);
		bedDao.delete(b);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		for (String nid : nids) {
			Integer id = Integer.parseInt(nid);
			// Bed b = bedDao.getById(Bed.class, id);
			// b.setStudent(null);
			// bedDao.update(b);
			Student s = studentDao.getByHql("from Student s where s.bed.id=" + id);
			if (s != null) {
				s.setBed(null);
			}
			// studentDao.update(s);
		}
		String hql = "delete Bed b where b.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		bedDao.executeHql(hql);
	}

	@Override
	public BedBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Bed bed = bedDao.getByHql("select distinct b from Bed b where b.id = :id", params);
		BedBean bedBean = new BedBean();
		BeanUtils.copyProperties(bed, bedBean, new String[] { "student", "dormitory" });
		return bedBean;
	}

	@Override
	public DataGrid datagrid(BedBean bedBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Bed b where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(bedBean, hql, params);
		String totalHql = "select count(*) from Bed b where 1=1 " + addWhere(bedBean, "", params);
		hql = addOrder(bedBean, hql);

		List<Bed> bed = bedDao.find(hql, params, bedBean.getPage(), bedBean.getRows());
		List<BedBean> bedBeans = new ArrayList<BedBean>();
		changeModel(bed, bedBeans);
		dg.setTotal(bedDao.count(totalHql, params));
		dg.setRows(bedBeans);
		return dg;
	}

	@Override
	public BedBean edit(BedBean bedBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", bedBean.getId());
		params.put("name", bedBean.getName());
		if (bedDao.count("select count(*) from Bed b where b.name = :name and b.id != :id", params) > 0) {
			throw new Exception("床位名已存在！");
		} else {
			Bed bed = bedDao.getById(Bed.class, bedBean.getId());
			BeanUtils.copyProperties(bedBean, bed);
			if (bedBean.getDormitoryname() != null && !"".equals(bedBean.getDormitoryname())) {
				if (bedBean.getDormitoryname().equals("0")) {
					bed.setDormitory(null);
					bedBean.setDormitoryname("");
				} else {
					Dormitory d = new Dormitory();
					d.setId(Integer.parseInt(bedBean.getDormitoryname()));
					Dormitory byId = dormitoryDao.getById(Dormitory.class, d.getId());
					bed.setDormitory(d);
					bedBean.setDormitoryname(byId.getNumber());
				}
			} else {
				if (bed.getDormitory() != null) {
					bedBean.setDormitoryname(bed.getDormitory().getNumber());
				}
			}
			if (bedBean.getStudentnumber() != null && !"".equals(bedBean.getStudentnumber())) {
				if (bedBean.getStudentnumber().equals("0")) {
					bed.setStudent(null);
					Student s = studentDao.getByHql("from Student s where s.bed.id=" + bedBean.getId());
					if (s != null) {
						s.setBed(null);
					}
					studentDao.update(s);
					bedBean.setStudentnumber("");
				} else {
					Student s = new Student();
					s.setId(Integer.parseInt(bedBean.getStudentnumber()));
					Student byId = studentDao.getById(Student.class, s.getId());
					if (byId.getBed() != null && byId.getBed().getId() != bedBean.getId()) {
						throw new Exception("该学生已存在床位！");
					}
					bed.setStudent(s);
					// studentDao.executeHql("update Student s set s.bed=null where s.bed.id=" + bedBean.getId());
					byId.setBed(bed);
					bedBean.setStudentnumber(byId.getNumber());
				}
			} else {
				if (bed.getStudent() != null) {
					bedBean.setStudentnumber(bed.getStudent().getNumber());
				}
			}
		}

		return bedBean;
	}

	private void changeModel(List<Bed> blist, List<BedBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Bed b : blist) {
				BedBean bedBean = new BedBean();
				BeanUtils.copyProperties(b, bedBean, new String[] { "student", "dormitory" });
				if (b.getStudent() != null) {
					bedBean.setStudentnumber(b.getStudent().getNumber());
				}
				if (b.getDormitory() != null) {
					bedBean.setDormitoryname(b.getDormitory().getNumber());
				}
				nl.add(bedBean);
			}
		}
	}

	private String addOrder(BedBean bedBean, String hql) {
		if (bedBean.getSort() != null && bedBean.getOrder() != null) {
			if (bedBean.getSort().equals("name")) {
				hql += " order by convert (" + bedBean.getSort() + ") " + bedBean.getOrder();
			} else {
				hql += " order by " + bedBean.getSort() + " " + bedBean.getOrder();
			}

		}
		return hql;
	}

	private String addWhere(BedBean bedBean, String hql, Map<String, Object> params) {
		if (bedBean != null) {
			if (bedBean.getName() != null && !bedBean.getName().trim().equals("")) {
				hql += " and b.name like :name";
				params.put("name", "%%" + bedBean.getName().trim() + "%%");
			}
			if (!StringUtil.isEmpty(bedBean.getStudentnumber())) {
				hql += " and b.student.number like :studentnumber";
				params.put("studentnumber", "%%" + bedBean.getStudentnumber().trim() + "%%");
			}
			if (!StringUtil.isEmpty(bedBean.getDormitoryname())) {
				hql += " and b.dormitory.number like :dormitoryname";
				params.put("dormitoryname", "%%" + bedBean.getDormitoryname().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<BedBean> getBeds(StudentBean studentBean) {
		String q = studentBean.getQ() == null ? "" : studentBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q + "%%");
		List<Bed> beds = (List<Bed>) bedDao.find("from Bed b where b.name like :name", params, 1, 30);
		List<BedBean> bedBeans = new ArrayList<BedBean>();
		changeModel(beds, bedBeans);
		return bedBeans;
	}

	@Override
	public List<BedBean> getBedData(BedBean bedBean) {
		String hql = "from Bed b where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(bedBean, hql, params);
		hql = addOrder(bedBean, hql);
		List<Bed> beds = bedDao.find(hql, params, 1, 500);
		List<BedBean> bedBeans = new ArrayList<BedBean>();
		changeModel(beds, bedBeans);
		return bedBeans;
	}

}
