package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.BuildingBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StaffBean;
import cn.edu.sxau.dormitorymanage.model.Staff;

public interface StaffService extends BaseService<Staff> {
	/**
	 * 添加床位
	 * 
	 * @param staffBean
	 * @throws Exception
	 */
	StaffBean save(StaffBean staffBean) throws Exception;

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
	 * @param staffBean
	 * @return
	 * @throws Exception
	 */
	StaffBean edit(StaffBean staffBean) throws Exception;

	/**
	 * 获得床位对象
	 * 
	 * @param id
	 * @return
	 */
	StaffBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param staffBean
	 * @return
	 */
	DataGrid datagrid(StaffBean staffBean);

	/**
	 * 根据名字模糊查询所有职工
	 * 
	 * @param buildingBean
	 * @return
	 */
	List<StaffBean> getStaffs(BuildingBean buildingBean);

	List<StaffBean> getStaffData(StaffBean staffBean);
}
