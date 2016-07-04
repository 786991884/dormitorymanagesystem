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
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.dao.BedDao;
import cn.edu.sxau.dormitorymanage.dao.ClazzDao;
import cn.edu.sxau.dormitorymanage.dao.StudentDao;
import cn.edu.sxau.dormitorymanage.model.Bed;
import cn.edu.sxau.dormitorymanage.model.Clazz;
import cn.edu.sxau.dormitorymanage.model.Student;
import cn.edu.sxau.dormitorymanage.service.StudentService;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Service
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private BedDao bedDao;
	@Autowired
	private ClazzDao clazzDao;

	@Override
	public StudentBean save(StudentBean studentBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", studentBean.getNumber());
		if (studentDao.count("select count(*) from Student s where s.number = :number", params) > 0) {
			throw new Exception("学号已存在！");
		} else {
			Student student = new Student();
			BeanUtils.copyProperties(studentBean, student);
			if (studentBean.getClazzname() != null && !"".equals(studentBean.getClazzname())) {
				if (studentBean.getClazzname().equals("0")) {
					student.setClazz(null);
					studentBean.setClazzname("");
				} else {
					Clazz c = new Clazz();
					c.setId(Integer.parseInt(studentBean.getClazzname()));
					Clazz byId = clazzDao.getById(Clazz.class, c.getId());
					student.setClazz(c);
					studentBean.setClazzname(byId.getName());
				}
			}
			if (studentBean.getBedname() != null && !"".equals(studentBean.getBedname())) {
				if (studentBean.getBedname().equals("0")) {
					student.setBed(null);
					Bed b = bedDao.getByHql("from Bed b where b.student.id=" + studentBean.getId());
					if (b != null) {
						b.setStudent(null);
					}
					bedDao.update(b);
					studentBean.setBedname("");
				} else {
					Bed b = new Bed();
					b.setId(Integer.parseInt(studentBean.getBedname()));
					Bed byId = bedDao.getById(Bed.class, b.getId());
					if (byId.getStudent() != null) {
						throw new Exception("该床位已存在学生！");
					}
					student.setBed(b);
					byId.setStudent(student);
					studentBean.setBedname(byId.getName());
				}
			}
			studentDao.save(student);
			BeanUtils.copyProperties(student, studentBean, new String[] { "clazz", "bed" });
			return studentBean;
		}
	}

	@Override
	public void delete(String id) {
		Integer nid = Integer.parseInt(id);
		Student student = studentDao.getById(Student.class, nid);
		// student.setBed(null);
		// studentDao.update(student);
		// Bed b = bedDao.getByHql("from Bed b where b.student.id=" + nid);
		// if (b != null) {
		// b.setStudent(null);
		// }
		// bedDao.update(b);
		studentDao.delete(student);
	}

	@Override
	public void remove(String ids) {
		String[] nids = ids.split(",");
		// for (String nid : nids) {
		// Integer id = Integer.parseInt(nid);
		// Student student = studentDao.getById(Student.class, id);
		// student.setBed(null);
		// studentDao.update(student);
		// Bed b = bedDao.getByHql("from Bed b where b.student.id=" + id);
		// if (b != null) {
		// b.setStudent(null);
		// }
		// bedDao.update(b);
		// }
		String hql = "delete Student s where s.id in (";
		for (int i = 0; i < nids.length; i++) {
			if (i > 0) {
				hql += ",";
			}
			hql += "'" + nids[i] + "'";
		}
		hql += ")";
		studentDao.executeHql(hql);
	}

	@Override
	public StudentBean edit(StudentBean studentBean) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", studentBean.getId());
		params.put("number", studentBean.getNumber());
		if (studentDao.count("select count(*) from Student s where s.number = :number and s.id != :id", params) > 0) {
			throw new Exception("学号已存在！");
		} else {
			Student student = studentDao.getById(Student.class, studentBean.getId());
			BeanUtils.copyProperties(studentBean, student);
			if (studentBean.getClazzname() != null && !"".equals(studentBean.getClazzname())) {
				if (studentBean.getClazzname().equals("0")) {
					student.setClazz(null);
					studentBean.setClazzname("");
				} else {
					Clazz c = new Clazz();
					c.setId(Integer.parseInt(studentBean.getClazzname()));
					Clazz byId = clazzDao.getById(Clazz.class, c.getId());
					student.setClazz(c);
					studentBean.setClazzname(byId.getName());
				}
			} else {
				if (student.getClazz() != null) {
					studentBean.setClazzname(student.getClazz().getName());
				}
			}
			if (studentBean.getBedname() != null && !"".equals(studentBean.getBedname())) {
				if (studentBean.getBedname().equals("0")) {
					student.setBed(null);
					// Bed b = bedDao.getByHql("from Bed b where b.student.id=" + studentBean.getId());
					// if (b != null) {
					// b.setStudent(null);
					// }
					// bedDao.update(b);
					studentBean.setBedname("");
				} else {
					Bed b = new Bed();
					b.setId(Integer.parseInt(studentBean.getBedname()));
					Bed byId = bedDao.getById(Bed.class, b.getId());
					if (byId.getStudent() != null && byId.getStudent().getId() != studentBean.getId()) {
						throw new Exception("该床位已存在学生！");
					}
					student.setBed(b);
					// bedDao.executeHql("update Bed b set b.student=null where b.student.id=" + studentBean.getId());
					byId.setStudent(student);
					studentBean.setBedname(byId.getName());
				}
			} else {
				if (student.getBed() != null) {
					studentBean.setBedname(student.getBed().getName());
				}
			}
		}
		return studentBean;
	}

	@Override
	public StudentBean get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Student student = studentDao.getByHql("select distinct s from Student s where s.id = :id", params);
		StudentBean studentBean = new StudentBean();
		BeanUtils.copyProperties(student, studentBean, new String[] { "clazz", "bed" });
		return studentBean;
	}

	@Override
	public DataGrid datagrid(StudentBean studentBean) {
		DataGrid dg = new DataGrid();
		String hql = "from Student s  where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(studentBean, hql, params);
		String totalHql = "select count(*) from Student s where 1=1 " + addWhere(studentBean, "", params);
		hql = addOrder(studentBean, hql);

		List<Student> students = studentDao.find(hql, params, studentBean.getPage(), studentBean.getRows());
		List<StudentBean> studentBeans = new ArrayList<StudentBean>();
		changeModel(students, studentBeans);
		dg.setTotal(studentDao.count(totalHql, params));
		dg.setRows(studentBeans);
		return dg;
	}

	private void changeModel(List<Student> blist, List<StudentBean> nl) {
		if (blist != null && blist.size() > 0) {
			for (Student b : blist) {
				StudentBean studentBean = new StudentBean();
				BeanUtils.copyProperties(b, studentBean, new String[] { "clazz", "bed" });
				if (b.getBed() != null) {
					studentBean.setBedname(b.getBed().getName());
				}
				if (b.getClazz() != null) {
					studentBean.setClazzname(b.getClazz().getName());
				}
				nl.add(studentBean);
			}
		}
	}

	private String addOrder(StudentBean studentBean, String hql) {
		if (studentBean.getSort() != null && studentBean.getOrder() != null) {
			if (studentBean.getSort().equals("name")) {
				hql += " order by convert (" + "s." + studentBean.getSort() + ") " + studentBean.getOrder();
			} else {
				hql += " order by s." + studentBean.getSort() + " " + studentBean.getOrder();
			}
		}
		return hql;
	}

	private String addWhere(StudentBean studentBean, String hql, Map<String, Object> params) {
		if (studentBean != null) {
			if (studentBean.getName() != null && !studentBean.getName().trim().equals("")) {
				hql += " and s.name like :name";
				params.put("name", "%%" + studentBean.getName().trim() + "%%");
			}
			if (!StringUtil.isEmpty(studentBean.getNumber())) {
				hql += " and s.number like :number";
				params.put("number", "%%" + studentBean.getNumber().trim() + "%%");
			}
			if (!StringUtil.isEmpty(studentBean.getClazzname())) {
				hql += " and s.clazz.name like :clazzname";
				params.put("clazzname", "%%" + studentBean.getClazzname().trim() + "%%");
			}
			if (!StringUtil.isEmpty(studentBean.getBedname())) {
				hql += " and s.bed.name like :bedname";
				params.put("bedname", "%%" + studentBean.getBedname().trim() + "%%");
			}
			if (studentBean.getLivedate() != null) {
				hql += " and s.livedate >=:livedate";
				params.put("livedate", "%%" + studentBean.getLivedate() + "%%");
			}
		}
		return hql;
	}

	@Override
	public List<StudentBean> getStudents(BedBean bedBean) {
		String q = bedBean.getQ() == null ? "" : bedBean.getQ();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", "%%" + q + "%%");
		List<Student> students = (List<Student>) studentDao.find("from Student s where s.number like :number", params, 1, 30);
		List<StudentBean> studentBeans = new ArrayList<StudentBean>();
		changeModel(students, studentBeans);
		return studentBeans;
	}

	@Override
	public JFreeChart getPieChart3DManWoman() {
		String hql = "select s.sex, count(s.sex) from Student s group by s.sex";
		List<Object[]> list = studentDao.findByHqlToArray(hql);
		String title = "男女比例饼状图";
		// 获得数据集
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Object[] object : list) {
			String sex = (String) object[0];
			Number number = (Number) object[1];
			dataset.setValue(sex, number.intValue());
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
	public JFreeChart getPieChart3DColStuNum() {
		String hql = "select s.clazz.profession.college.name, count(s.number) from Student s group by s.clazz.profession.college.name";
		List<Object[]> list = studentDao.findByHqlToArray(hql);
		String title = "学院学生数量饼图";
		// 获得数据集
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Object[] object : list) {
			String collegename = (String) object[0];
			Number number = (Number) object[1];
			dataset.setValue(collegename, number.intValue());
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
	public JFreeChart getPieChart3DBuildStuNum() {
		String hql = "select s.bed.dormitory.building.number, count(s.number) from Student s group by s.bed.dormitory.building.number";
		List<Object[]> list = studentDao.findByHqlToArray(hql);
		String title = "楼宇学生数量比例饼状图";
		// 获得数据集
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Object[] object : list) {
			String collegename = (String) object[0];
			Number number = (Number) object[1];
			dataset.setValue(collegename + "号楼", number.intValue());
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
	public List<StudentBean> getStudentData(StudentBean studentBean) {
		String hql = "from Student s where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		hql = addWhere(studentBean, hql, params);
		hql = addOrder(studentBean, hql);
		List<Student> students = studentDao.find(hql, params, 1, 500);
		List<StudentBean> studentBeans = new ArrayList<StudentBean>();
		changeModel(students, studentBeans);
		return studentBeans;
	}

}
