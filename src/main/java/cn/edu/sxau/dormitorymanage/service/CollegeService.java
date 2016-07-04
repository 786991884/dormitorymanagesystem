package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import cn.edu.sxau.dormitorymanage.bean.CollegeBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.ProfessionBean;
import cn.edu.sxau.dormitorymanage.model.College;

public interface CollegeService extends BaseService<College> {
	/**
	 * 添加学院
	 * 
	 * @param collegeBean
	 * @throws Exception
	 */
	CollegeBean save(CollegeBean collegeBean) throws Exception;

	/**
	 * 删除单个
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 删除对象单个或者批量
	 * 
	 * @param ids
	 */
	void remove(String ids);

	/**
	 * 
	 * @param collegeBean
	 * @return
	 * @throws Exception
	 */
	CollegeBean edit(CollegeBean collegeBean) throws Exception;

	/**
	 * 获得学院对象
	 * 
	 * @param id
	 * @return
	 */
	CollegeBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param collegeBean
	 * @return
	 */
	DataGrid datagrid(CollegeBean collegeBean);

	/**
	 * 模糊查询学院集合
	 * 
	 * @param professionBean
	 * @return
	 */
	List<CollegeBean> getColleges(ProfessionBean professionBean);

	/**
	 * 学院专业数量统计
	 * 
	 * @return
	 */
	JFreeChart getBarChartColProfNum();

	List<CollegeBean> getCollegeData(CollegeBean collegeBean);

}
