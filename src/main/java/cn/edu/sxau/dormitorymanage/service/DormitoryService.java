package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.DormitoryBean;
import cn.edu.sxau.dormitorymanage.model.Dormitory;

public interface DormitoryService extends BaseService<Dormitory> {
	/**
	 * 添加宿舍
	 * 
	 * @param dormitoryBean
	 * @throws Exception
	 */
	DormitoryBean save(DormitoryBean dormitoryBean) throws Exception;

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
	 * @param dormitoryBean
	 * @return
	 * @throws Exception
	 */
	DormitoryBean edit(DormitoryBean dormitoryBean) throws Exception;

	/**
	 * 获得宿舍对象
	 * 
	 * @param id
	 * @return
	 */
	DormitoryBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param dormitoryBean
	 * @return
	 */
	DataGrid datagrid(DormitoryBean dormitoryBean);

	/**
	 * 根据宿舍模糊查询
	 * 
	 * @param bedBean
	 * @return
	 */
	List<DormitoryBean> getDormitorys(BedBean bedBean);

	/**
	 * 宿舍类型数量统计
	 * 
	 * @return
	 */
	JFreeChart getPieChart3DDormType();

	List<DormitoryBean> getDormitoryData(DormitoryBean dormitoryBean);
}
