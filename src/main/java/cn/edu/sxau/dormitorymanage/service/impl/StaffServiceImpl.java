package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.BuildingBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StaffBean;
import cn.edu.sxau.dormitorymanage.dao.BuildingDao;
import cn.edu.sxau.dormitorymanage.dao.StaffDao;
import cn.edu.sxau.dormitorymanage.model.Building;
import cn.edu.sxau.dormitorymanage.model.Staff;
import cn.edu.sxau.dormitorymanage.service.StaffService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class StaffServiceImpl extends BaseServiceImpl<Staff> implements StaffService {
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private BuildingDao buildingDao;

	@Override
	public StaffBean save(StaffBean staffBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", staffBean.getName());
		if (staffDao.count("select count(*) from Staff s where s.name = :name", params) > 0) {
			throw new Exception("员工已存在！");
		} else {
			Staff staff = new Staff();
			BeanUtils.copyProperties(staffBean, staff);
			if (staffBean.getBuildingname() != null && !"".equals(staffBean.getBuildingname())) {
				Building b = new Building();
				b.setId(Integer.parseInt(staffBean.getBuildingname()));
				Building byId = buildingDao.getById(Building.class, b.getId());
				if (byId.getStaff() != null) {
					throw new Exception("该楼宇已存在管理职工！");
				}
				staff.setBuilding(b);
				byId.setStaff(staff);
				staffBean.setBuildingname(byId.getNumber());
			}
			staffDao.save(staff);
			BeanUtils.copyProperties(staff, staffBean, new String[] { "building" });
			return staffBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Staff s = staffDao.getById(Staff.class, nid);
		// s.setBuilding(null);
		// staffDao.update(s);
		Building building = buildingDao.getByHql("from Building b where b.staff.id=" + nid);
		if (building != null) {
			building.setStaff(null);
		}
		buildingDao.update(building);
		staffDao.delete(s);
	}

	@Override
	public void remove(String ids) {
		// 先断开与楼宇的关联关系
		String[] nids = ids.split(",");
		for (String nid : nids) {
			Integer id = Integer.parseInt(nid);
			// Staff s = staffDao.getById(Staff.class, id);
			// s.setBuilding(null);
			// staffDao.update(s);
			Building building = buildingDao.getByHql("from Building b where b.staff.id=" + id);
			if (building != null) {
				building.setStaff(null);
			}
			buildingDao.update(building);
		}
		String hql = "delete Staff s where s.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		staffDao.executeHql(hql);
	}

	@Override
	public StaffBean edit(StaffBean staffBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", staffBean.getId());
		params.put("name", staffBean.getName());
		if (staffDao.count("select count(*) from Staff b where b.name = :name and b.id != :id", params) > 0) {
			throw new Exception("员工已存在！");
		} else {
			Staff staff = staffDao.getById(Staff.class, staffBean.getId());
			BeanUtils.copyProperties(staffBean, staff);
			if (staffBean.getBuildingname() != null && !"".equals(staffBean.getBuildingname())) {
				if (staffBean.getBuildingname().equals("0")) {
					staff.setBuilding(null);
					staffBean.setBuildingname("");
				} else {
					Building b = new Building();
					b.setId(Integer.parseInt(staffBean.getBuildingname()));
					Building byId = buildingDao.getById(Building.class, b.getId());
					if (byId.getStaff() != null && byId.getStaff().getId() != staffBean.getId()) {
						throw new Exception("该楼宇已存在管理职工！");
					}
					staff.setBuilding(b);
					// buildingDao.executeHql("update Building b set b.staff=null where b.staff.id=" + staffBean.getId());
					byId.setStaff(staff);
					staffBean.setBuildingname(byId.getNumber());
				}
			} else {
				if (staff.getBuilding() != null) {
					staffBean.setBuildingname(staff.getBuilding().getNumber());
				}
			}

		}
		return staffBean;
	}

	@Override
	public StaffBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Staff staff = staffDao.getByHql("select distinct s from Staff s where s.id = :id", params);
		StaffBean staffBean = new StaffBean();
		BeanUtils.copyProperties(staff, staffBean, new String[] { "building" });
		return staffBean;
	}

	@Override
	public DataGrid datagrid(StaffBean staffBean) {
		DataGrid dg = new DataGrid();
		// String hql = "select s from Staff s left join fetch s.building b where 1=1 ";
		String hql = "from Staff s where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(staffBean, hql, params);
		String totalHql = "select count(*) from Staff s where 1=1 " + addWhere(staffBean, "", params);
		hql = addOrder(staffBean, hql);

		List<Staff> staffs = staffDao.find(hql, params, staffBean.getPage(), staffBean.getRows());
		List<StaffBean> staffBeans = new ArrayList<StaffBean>();
		changeModel(staffs, staffBeans);
		dg.setTotal(staffDao.count(totalHql, params));
		dg.setRows(staffBeans);
		return dg;
	}

	private void changeModel(List<Staff> blist, List<StaffBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Staff b : blist) {
				StaffBean staffBean = new StaffBean();
				BeanUtils.copyProperties(b, staffBean, new String[] { "building" });
				if (b.getBuilding() != null) {
					staffBean.setBuildingname(b.getBuilding().getNumber());
				}
				nl.add(staffBean);
			}
		}
	}

	private String addOrder(StaffBean staffBean, String hql) {
		if (staffBean.getSort() != null && staffBean.getOrder() != null) {
			if (staffBean.getSort().equals("name")) {
				hql += " order by convert (" + staffBean.getSort() + ") " + staffBean.getOrder();
			} else {
				hql += " order by " + staffBean.getSort() + " " + staffBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(StaffBean staffBean, String hql, Map<String, Object> params) {
		if (staffBean != null) {
			if (staffBean.getName() != null && !staffBean.getName().trim().equals("")) {
				hql += " and s.name like :name";
				params.put("name", "%%" + staffBean.getName().trim() + "%%");
			}
			if (!StringUtil.isEmpty(staffBean.getBuildingname())) {
				hql += " and s.building.number like :buildingnumber";
				params.put("buildingnumber", "%%" + staffBean.getBuildingname().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<StaffBean> getStaffs(BuildingBean buildingBean) {
		String q = buildingBean.getQ() == null ? "" : buildingBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q + "%%");
		List<Staff> staffs = (List<Staff>) staffDao.find("from Staff s where s.name like :name", params, 1, 30);
		List<StaffBean> staffBeans = new ArrayList<StaffBean>();
		changeModel(staffs, staffBeans);
		return staffBeans;
	}

	@Override
	public List<StaffBean> getStaffData(StaffBean staffBean) {
		String hql = "from Staff s where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(staffBean, hql, params);
		hql = addOrder(staffBean, hql);
		List<Staff> staffs = staffDao.find(hql, params, 1, 500);
		List<StaffBean> staffBeans = new ArrayList<StaffBean>();
		changeModel(staffs, staffBeans);
		return staffBeans;
	}
}
