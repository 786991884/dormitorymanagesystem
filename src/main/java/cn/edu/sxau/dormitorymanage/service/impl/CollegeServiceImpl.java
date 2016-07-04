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

import cn.edu.sxau.dormitorymanage.bean.CollegeBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.ProfessionBean;
import cn.edu.sxau.dormitorymanage.dao.CollegeDao;
import cn.edu.sxau.dormitorymanage.dao.ProfessionDao;
import cn.edu.sxau.dormitorymanage.model.College;
import cn.edu.sxau.dormitorymanage.service.CollegeService;

@Service
public class CollegeServiceImpl extends BaseServiceImpl<College> implements CollegeService {
	@Autowired
	private CollegeDao collegeDao;
	@Autowired
	private ProfessionDao professionDao;

	@Override
	public CollegeBean save(CollegeBean collegeBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", collegeBean.getName());
		if (collegeDao.count("select count(*) from College c where c.name = :name", params) > 0) {
			throw new Exception("学院已存在！");
		} else {
			College college = new College();
			BeanUtils.copyProperties(collegeBean, college);
			collegeDao.save(college);
			BeanUtils.copyProperties(college, collegeBean, new String[] { "professions" });
			return collegeBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		professionDao.executeHql("update Profession p set p.college=null where p.college.id=" + nid);
		collegeDao.delete(collegeDao.getById(College.class, id));
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		for (String nid : nids) {
			professionDao.executeHql("update Profession p set p.college=null where p.college.id=" + nid);
		}
		String hql = "delete College c where c.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		collegeDao.executeHql(hql);
	}

	@Override
	public CollegeBean edit(CollegeBean collegeBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", collegeBean.getId());
		params.put("name", collegeBean.getName());
		if (collegeDao.count("select count(*) from College c where c.name = :name and c.id != :id", params) > 0) {
			throw new Exception("学院名已存在！");
		} else {
			College college = collegeDao.getById(College.class, collegeBean.getId());
			BeanUtils.copyProperties(collegeBean, college);
		}

		return collegeBean;
	}

	@Override
	public CollegeBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		College college = collegeDao.getByHql("select distinct c from College c where c.id = :id", params);
		CollegeBean collegeBean = new CollegeBean();
		BeanUtils.copyProperties(college, collegeBean, new String[] { "professions" });
		return collegeBean;
	}

	@Override
	public DataGrid datagrid(CollegeBean collegeBean) {
		DataGrid dg = new DataGrid();
		String hql = "from College c where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(collegeBean, hql, params);
		String totalHql = "select count(*) from College c where 1=1 " + addWhere(collegeBean, "", params);
		hql = addOrder(collegeBean, hql);

		List<College> colleges = collegeDao.find(hql, params, collegeBean.getPage(), collegeBean.getRows());
		List<CollegeBean> collegeBeans = new ArrayList<CollegeBean>();
		changeModel(colleges, collegeBeans);
		dg.setTotal(collegeDao.count(totalHql, params));
		dg.setRows(collegeBeans);
		return dg;
	}

	private void changeModel(List<College> blist, List<CollegeBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (College b : blist) {
				CollegeBean collegeBean = new CollegeBean();
				BeanUtils.copyProperties(b, collegeBean, new String[] { "professions" });
				nl.add(collegeBean);
			}
		}
	}

	private String addOrder(CollegeBean collegeBean, String hql) {
		if (collegeBean.getSort() != null && collegeBean.getOrder() != null) {
			if (collegeBean.getSort().equals("name")) {
				hql += " order by convert (" + collegeBean.getSort() + ") " + collegeBean.getOrder();
			} else {
				hql += " order by " + collegeBean.getSort() + " " + collegeBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(CollegeBean collegeBean, String hql, Map<String, Object> params) {
		if (collegeBean != null) {
			if (collegeBean.getName() != null && !collegeBean.getName().trim().equals("")) {
				hql += " and c.name like :name";
				params.put("name", "%%" + collegeBean.getName().trim() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<CollegeBean> getColleges(ProfessionBean professionBean) {
		String q = professionBean.getQ() == null ? "" : professionBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q + "%%");
		List<College> colleges = (List<College>) collegeDao.find("from College c where c.name like :name", params, 1, 30);
		List<CollegeBean> collegeBeans = new ArrayList<CollegeBean>();
		changeModel(colleges, collegeBeans);
		return collegeBeans;
	}

	@Override
	public JFreeChart getBarChartColProfNum() {
		String title = "学院专业数量统计图";
		// 获得数据集
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Object[]> list = collegeDao.findByHqlToArray("select p.college.name, count(p.name) from Profession p group by p.college.name");
		for (Object[] object : list) {
			String type = (String) object[0];
			Number number = (Number) object[1];
			dataset.setValue(number.intValue(), type, type);
		}
		JFreeChart chart = ChartFactory.createBarChart3D(title, // 图表标题
				"学院名称", // 目录轴的显示标签
				"专业数量", // 数值轴的显示标签
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
	public List<CollegeBean> getCollegeData(CollegeBean collegeBean) {
		String hql = "from College c where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(collegeBean, hql, params);
		hql = addOrder(collegeBean, hql);
		List<College> colleges = collegeDao.find(hql, params, 1, 500);
		List<CollegeBean> collegeBeans = new ArrayList<CollegeBean>();
		changeModel(colleges, collegeBeans);
		return collegeBeans;
	}

}
