package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.model.Bed;

public interface BedService extends BaseService<Bed> {

	/**
	 * 添加床位
	 * 
	 * @param bedBean
	 * @throws Exception
	 */
	BedBean save(BedBean bedBean) throws Exception;

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
	 * @param bedBean
	 * @return
	 * @throws Exception
	 */
	BedBean edit(BedBean bedBean) throws Exception;

	/**
	 * 获得床位对象
	 * 
	 * @param id
	 * @return
	 */
	BedBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param bedBean
	 * @return
	 */
	DataGrid datagrid(BedBean bedBean);

	/**
	 * 根据名称模糊查询床位集合
	 * 
	 * @param studentBean
	 * @return
	 */
	List<BedBean> getBeds(StudentBean studentBean);

	/**
	 * 封装导出的excel数据
	 * 
	 * @param bedBean
	 * @return
	 */
	List<BedBean> getBedData(BedBean bedBean);

}
