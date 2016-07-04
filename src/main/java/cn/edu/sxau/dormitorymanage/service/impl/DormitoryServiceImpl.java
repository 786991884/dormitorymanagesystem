package cn.edu.sxau.dormitorymanage.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.DormitoryBean;
import cn.edu.sxau.dormitorymanage.dao.BedDao;
import cn.edu.sxau.dormitorymanage.dao.BuildingDao;
import cn.edu.sxau.dormitorymanage.dao.DormitoryDao;
import cn.edu.sxau.dormitorymanage.model.Building;
import cn.edu.sxau.dormitorymanage.model.Dormitory;
import cn.edu.sxau.dormitorymanage.service.DormitoryService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class DormitoryServiceImpl extends BaseServiceImpl<Dormitory> implements DormitoryService {
	@Autowired
	private DormitoryDao dormitoryDao;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BedDao bedDao;

	@Override
	public DormitoryBean save(DormitoryBean dormitoryBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", dormitoryBean.getNumber());
		if (dormitoryDao.count("select count(*) from Dormitory d where d.number = :number", params) > 0) {
			throw new Exception("宿舍号已存在！");
		} else {
			Dormitory dormitory = new Dormitory();
			BeanUtils.copyProperties(dormitoryBean, dormitory);
			if (dormitoryBean.getBuildingname() != null && !"".equals(dormitoryBean.getBuildingname())) {
				if (dormitoryBean.getBuildingname().equals("0")) {
					dormitoryBean.setBuildingname("");
				}
				Building b = new Building();
				b.setId(Integer.parseInt(dormitoryBean.getBuildingname()));
				dormitory.setBuilding(b);
				Building byId = buildingDao.getById(Building.class, b.getId());
				dormitoryBean.setBuildingname(byId.getNumber());
			}
			dormitoryDao.save(dormitory);
			BeanUtils.copyProperties(dormitory, dormitoryBean, new String[] { "building", "beds" });
			return dormitoryBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Dormitory dormitory = dormitoryDao.getById(Dormitory.class, nid);
		dormitory.setBuilding(null);
		bedDao.executeHql("update Bed b set b.dormitory=null where b.dormitory.id=" + nid);
		dormitoryDao.delete(dormitory);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		for (String nid : nids) {
			Integer id = Integer.parseInt(nid);
			Dormitory dormitory = dormitoryDao.getById(Dormitory.class, id);
			dormitory.setBuilding(null);
			bedDao.executeHql("update Bed b set b.dormitory=null where b.dormitory.id=" + id);
		}
		String hql = "delete Dormitory d where d.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		dormitoryDao.executeHql(hql);
	}

	@Override
	public DormitoryBean edit(DormitoryBean dormitoryBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", dormitoryBean.getId());
		params.put("number", dormitoryBean.getNumber());
		if (dormitoryDao.count("select count(*) from Dormitory c where c.number = :number and c.id != :id", params) > 0) {
			throw new Exception("宿舍名已存在！");
		} else {
			Dormitory dormitory = dormitoryDao.getById(Dormitory.class, dormitoryBean.getId());
			BeanUtils.copyProperties(dormitoryBean, dormitory);
			if (dormitoryBean.getBuildingname() != null && !"".equals(dormitoryBean.getBuildingname())) {
				if (dormitoryBean.getBuildingname().equals("0")) {
					dormitory.setBuilding(null);
					dormitoryBean.setBuildingname("");
				} else {
					Building b = new Building();
					b.setId(Integer.parseInt(dormitoryBean.getBuildingname()));
					dormitory.setBuilding(b);
					Building byId = buildingDao.getById(Building.class, b.getId());
					dormitoryBean.setBuildingname(byId.getNumber());
				}
			} else {
				if (dormitory.getBuilding() != null) {
					dormitoryBean.setBuildingname(dormitory.getBuilding().getNumber());
				}
			}
		}
		return dormitoryBean;
	}

	@Override
	public DormitoryBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Dormitory dormitory = dormitoryDao.getByHql("select distinct d from Dormitory d where d.id = :id", params);
		DormitoryBean dormitoryBean = new DormitoryBean();
		BeanUtils.copyProperties(dormitory, dormitoryBean, new String[] { "building", "beds" });
		return dormitoryBean;
	}

	@Override
	public DataGrid datagrid(DormitoryBean dormitoryBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Dormitory d where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(dormitoryBean, hql, params);
		String totalHql = "select count(*) from Dormitory d where 1=1 " + addWhere(dormitoryBean, "", params);
		hql = addOrder(dormitoryBean, hql);

		List<Dormitory> dormitories = dormitoryDao.find(hql, params, dormitoryBean.getPage(), dormitoryBean.getRows());
		List<DormitoryBean> dormitoryBeans = new ArrayList<DormitoryBean>();
		changeModel(dormitories, dormitoryBeans);
		dg.setTotal(dormitoryDao.count(totalHql, params));
		dg.setRows(dormitoryBeans);
		return dg;
	}

	private void changeModel(List<Dormitory> blist, List<DormitoryBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Dormitory b : blist) {
				DormitoryBean dormitoryBean = new DormitoryBean();
				BeanUtils.copyProperties(b, dormitoryBean, new String[] { "building", "beds" });
				if (b.getBuilding() != null) {
					dormitoryBean.setBuildingname(b.getBuilding().getNumber());
				}
				nl.add(dormitoryBean);
			}
		}
	}

	private String addOrder(DormitoryBean dormitoryBean, String hql) {
		if (dormitoryBean.getSort() != null && dormitoryBean.getOrder() != null) {
			if (dormitoryBean.getSort().equals("number")) {
				hql += " order by convert (" + dormitoryBean.getSort() + ") " + dormitoryBean.getOrder();
			} else {
				hql += " order by " + dormitoryBean.getSort() + " " + dormitoryBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(DormitoryBean dormitoryBean, String hql, Map<String, Object> params) {
		if (dormitoryBean != null) {
			if (dormitoryBean.getNumber() != null && !dormitoryBean.getNumber().trim().equals("")) {
				hql += " and d.number like :number";
				params.put("number", "%%" + dormitoryBean.getNumber().trim() + "%%");
			}
			if (!StringUtil.isEmpty(dormitoryBean.getBuildingname())) {
				hql += " and d.building.number like :buildingnumber";
				params.put("buildingnumber", "%%" + dormitoryBean.getBuildingname().trim() + "%%");
			}

		}
		return hql;
	}

	@Override
	public List<DormitoryBean> getDormitorys(BedBean bedBean) {
		String q = bedBean.getQ() == null ? "" : bedBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", "%%" + q + "%%");
		List<Dormitory> dormitories = (List<Dormitory>) dormitoryDao.find("from Dormitory d where d.number like :number", params, 1, 30);
		List<DormitoryBean> dormitoryBeans = new ArrayList<DormitoryBean>();
		changeModel(dormitories, dormitoryBeans);
		return dormitoryBeans;
	}

	@Override
	public JFreeChart getPieChart3DDormType() {
		String hql = "select d.type, count(d.type) from Dormitory d group by d.type";
		List<Object[]> list = dormitoryDao.findByHqlToArray(hql);
		String title = "宿舍类型比例饼状图";
		// 获得数据集
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Object[] object : list) {
			Integer type = (Integer) object[0];
			Number number = (Number) object[1];
			dataset.setValue(type + "人间", number.intValue());
		}
		// 利用chart工厂创建一个jfreechart实例
		JFreeChart chart = ChartFactory.createPieChart3D(title, // 图表标题
				dataset, // 图表数据集
				true, // 是否显示图例
				true, // 是否生成工具（提示）
				false // 是否生成URL链接
				);
		// 设置pieChart的标题与字体
		Font font = new Font("宋体", Font.BOLD, 25);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		chart.setTextAntiAlias(false);
		// 设置背景色
		chart.setBackgroundPaint(new Color(255, 255, 255));
		// 设置图例字体
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", 1, 15));
		// 设置标签字体
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 12));
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.95f);
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
		// 设置第一个饼块截面开始的位置，默认是12点钟方向
		plot.setStartAngle(90);
		return chart;
	}

	@Override
	public List<DormitoryBean> getDormitoryData(DormitoryBean dormitoryBean) {
		String hql = "from Dormitory d where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(dormitoryBean, hql, params);
		hql = addOrder(dormitoryBean, hql);
		List<Dormitory> dormitories = dormitoryDao.find(hql, params, 1, 500);
		List<DormitoryBean> dormitoryBeans = new ArrayList<DormitoryBean>();
		changeModel(dormitories, dormitoryBeans);
		return dormitoryBeans;
	}

}
