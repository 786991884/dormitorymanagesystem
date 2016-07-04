package cn.edu.sxau.dormitorymanage.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.BuildingBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.DormitoryBean;
import cn.edu.sxau.dormitorymanage.bean.StaffBean;
import cn.edu.sxau.dormitorymanage.dao.BuildingDao;
import cn.edu.sxau.dormitorymanage.dao.StaffDao;
import cn.edu.sxau.dormitorymanage.model.Building;
import cn.edu.sxau.dormitorymanage.model.Staff;
import cn.edu.sxau.dormitorymanage.service.BuildingService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class BuildingServiceImpl extends BaseServiceImpl<Building> implements BuildingService {
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private StaffDao staffDao;

	@Override
	public BuildingBean save(BuildingBean buildingBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", buildingBean.getNumber());
		if (buildingDao.count("select count(*) from Building b where b.number = :number", params) > 0) {
			throw new Exception("楼宇号已存在！");
		} else {
			Building building = new Building();
			BeanUtils.copyProperties(buildingBean, building);
			if (buildingBean.getStaffname() != null && !"".equals(buildingBean.getStaffname())) {
				if (buildingBean.getStaffname().equals("0")) {
					building.setStaff(null);
					buildingBean.setStaffname("");
				} else {
					Staff s = new Staff();
					s.setId(Integer.parseInt(buildingBean.getStaffname()));
					Staff byId = staffDao.getById(Staff.class, s.getId());
					if (byId.getBuilding() != null) {
						throw new Exception("该职工已存在管理楼宇！");
					}
					building.setStaff(s);
					byId.setBuilding(building);
					buildingBean.setStaffname(byId.getName());
				}
			}
			buildingDao.save(building);
			BeanUtils.copyProperties(building, buildingBean, new String[] { "staff", "dormitories" });
			return buildingBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Building b = buildingDao.getById(Building.class, nid);
		// b.setStaff(null);
		// buildingDao.update(b);
		// Staff staff = staffDao.getByHql("from Staff s where s.building.id=" + nid);
		// if (staff != null) {
		// staff.setBuilding(null);
		// }
		// staffDao.update(staff);
		buildingDao.delete(b);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		// for (String nid : nids) {
		// Integer id = Integer.parseInt(nid);
		// Building b = buildingDao.getById(Building.class, id);
		// b.setStaff(null);
		// buildingDao.update(b);
		// Staff staff = staffDao.getByHql("from Staff s where s.building.id=" + id);
		// if (staff != null) {
		// staff.setBuilding(null);
		// }
		// staffDao.update(staff);
		// }
		String hql = "delete Building b where b.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		buildingDao.executeHql(hql);
	}

	@Override
	public BuildingBean edit(BuildingBean buildingBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", buildingBean.getId());
		params.put("number", buildingBean.getNumber());
		if (buildingDao.count("select count(*) from Building b where b.number = :number and b.id != :id", params) > 0) {
			throw new Exception("楼宇名已存在！");
		} else {
			Building building = buildingDao.getById(Building.class, buildingBean.getId());
			BeanUtils.copyProperties(buildingBean, building);
			if (buildingBean.getStaffname() != null && !"".equals(buildingBean.getStaffname())) {
				if (buildingBean.getStaffname().equals("0")) {
					building.setStaff(null);
					buildingBean.setStaffname("");
				} else {
					Staff s = new Staff();
					s.setId(Integer.parseInt(buildingBean.getStaffname()));
					Staff byId = staffDao.getById(Staff.class, s.getId());
					if (byId.getBuilding() != null && byId.getBuilding().getId() != buildingBean.getId()) {
						throw new Exception("该职工已存在管理楼宇！");
					}
					building.setStaff(s);
					// staffDao.executeHql("update Staff s set s.building=null where s.building.id=" + buildingBean.getId());
					byId.setBuilding(building);
					buildingBean.setStaffname(byId.getName());
				}
			} else {
				if (building.getStaff() != null) {
					buildingBean.setStaffname(building.getStaff().getName());
				}
			}

		}

		return buildingBean;
	}

	@Override
	public BuildingBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Building building = buildingDao.getByHql("select distinct b from Building b where b.id = :id", params);
		BuildingBean buildingBean = new BuildingBean();
		BeanUtils.copyProperties(building, buildingBean, new String[] { "staff", "dormitories" });
		return buildingBean;
	}

	@Override
	public DataGrid datagrid(BuildingBean buildingBean) {
		DataGrid dg = new DataGrid();
		// String hql = "select b from Building b left join fetch b.staff s where 1=1 ";
		String hql = "from Building b where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(buildingBean, hql, params);
		String totalHql = "select count(*) from Building b where 1=1 " + addWhere(buildingBean, "", params);
		hql = addOrder(buildingBean, hql);

		List<Building> buildings = buildingDao.find(hql, params, buildingBean.getPage(), buildingBean.getRows());
		List<BuildingBean> BuildingBeans = new ArrayList<BuildingBean>();
		changeModel(buildings, BuildingBeans);
		dg.setTotal(buildingDao.count(totalHql, params));
		dg.setRows(BuildingBeans);
		return dg;
	}

	private void changeModel(List<Building> blist, List<BuildingBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Building b : blist) {
				BuildingBean buildingBean = new BuildingBean();
				BeanUtils.copyProperties(b, buildingBean, new String[] { "staff", "dormitories" });
				if (b.getStaff() != null) {
					buildingBean.setStaffname(b.getStaff().getName());
				}
				nl.add(buildingBean);
			}
		}
	}

	private String addOrder(BuildingBean buildingBean, String hql) {
		if (buildingBean.getSort() != null && buildingBean.getOrder() != null) {
			if (buildingBean.getSort().equals("number")) {
				hql += " order by convert (" + buildingBean.getSort() + ") " + buildingBean.getOrder();
			} else {
				hql += " order by " + buildingBean.getSort() + " " + buildingBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(BuildingBean buildingBean, String hql, Map<String, Object> params) {
		if (buildingBean != null) {
			if (buildingBean.getNumber() != null && !buildingBean.getNumber().trim().equals("")) {
				hql += " and b.number like :number";
				params.put("number", "%%" + buildingBean.getNumber().trim() + "%%");
			}
			if (!StringUtil.isEmpty(buildingBean.getStaffname())) {
				hql += " and b.staff.name like :staffname";
				params.put("staffname", "%%" + buildingBean.getStaffname().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<BuildingBean> getBuildings(StaffBean staffBean) {
		String q = staffBean.getQ() == null ? "" : staffBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", "%%" + q + "%%");
		List<Building> buildings = (List<Building>) buildingDao.find("from Building b where b.number like :number", params, 1, 30);
		List<BuildingBean> buildingBeans = new ArrayList<BuildingBean>();
		changeModel(buildings, buildingBeans);
		return buildingBeans;
	}

	@Override
	public List<BuildingBean> getBuildings(DormitoryBean dormitoryBean) {
		String q = dormitoryBean.getQ() == null ? "" : dormitoryBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", "%%" + q + "%%");
		List<Building> buildings = (List<Building>) buildingDao.find("from Building b where b.number like :number", params, 1, 30);
		List<BuildingBean> buildingBeans = new ArrayList<BuildingBean>();
		changeModel(buildings, buildingBeans);
		return buildingBeans;
	}

	@Override
	public JFreeChart getBarChartBuildType() {
		String title = "楼宇类型统计图";
		// 获得数据集
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Object[]> list = buildingDao.findByHqlToArray("select b.type, count(b.type) from Building b group by b.type");
		for (Object[] object : list) {
			String type = (String) object[0];
			Number number = (Number) object[1];
			dataset.setValue(number.intValue(), type, type);
		}
		JFreeChart chart = ChartFactory.createBarChart3D(title, // 图表标题
				"楼宇类型", // 目录轴的显示标签
				"楼宇数量", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例
				true, // 是否生成工具（提示）
				true // 是否生成URL链接
				);
		// 设置标题字体
		Font font = new Font("宋体", Font.BOLD, 18);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		chart.setTextAntiAlias(false);
		// 设置背景色
		chart.setBackgroundPaint(new Color(255, 255, 255));
		// 设置图例字体
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", Font.TRUETYPE_FONT, 13));

		// 获得柱状图的Plot对象
		CategoryPlot plot = chart.getCategoryPlot();
		// 取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		// 设置横轴显示标签的字体
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
		// 设置横轴标记的字体
		categoryAxis.setTickLabelFont(new Font("宋休", Font.TRUETYPE_FONT, 16));
		// 取得纵轴
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		// 设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
		return chart;
	}

	@Override
	public List<BuildingBean> getBuildingData(BuildingBean buildingBean) {
		String hql = "from Building b where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(buildingBean, hql, params);
		hql = addOrder(buildingBean, hql);
		List<Building> buildings = buildingDao.find(hql, params, 1, 500);
		List<BuildingBean> buildingBeans = new ArrayList<BuildingBean>();
		changeModel(buildings, buildingBeans);
		return buildingBeans;
	}
}
