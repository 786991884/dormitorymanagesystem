package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import cn.edu.sxau.dormitorymanage.bean.BuildingBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.DormitoryBean;
import cn.edu.sxau.dormitorymanage.bean.StaffBean;
import cn.edu.sxau.dormitorymanage.model.Building;

public interface BuildingService extends BaseService<Building> {
	/**
	 * 添加楼宇
	 * 
	 * @param buildingBean
	 * @throws Exception
	 */
	BuildingBean save(BuildingBean buildingBean) throws Exception;

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
	 * @param buildingBean
	 * @return
	 * @throws Exception
	 */
	BuildingBean edit(BuildingBean buildingBean) throws Exception;

	/**
	 * 获得楼宇对象
	 * 
	 * @param id
	 * @return
	 */
	BuildingBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param buildingBean
	 * @return
	 */
	DataGrid datagrid(BuildingBean buildingBean);

	/**
	 * 模糊查询楼宇集合
	 * 
	 * @param staffBean
	 * @return
	 */
	List<BuildingBean> getBuildings(StaffBean staffBean);

	/**
	 * 模糊查询楼宇集合
	 * 
	 * @param dormitoryBean
	 * @return
	 */
	List<BuildingBean> getBuildings(DormitoryBean dormitoryBean);

	/**
	 * 楼宇类型数量统计
	 * 
	 * @return
	 */
	JFreeChart getBarChartBuildType();

	/**
	 * 导出封装的数据
	 * 
	 * @param buildingBean
	 * @return
	 */
	List<BuildingBean> getBuildingData(BuildingBean buildingBean);
}
