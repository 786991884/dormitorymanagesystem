package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.ClazzBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.model.Clazz;

public interface ClazzService extends BaseService<Clazz> {
	/**
	 * 添加班级
	 * 
	 * @param clazzBean
	 * @throws Exception
	 */
	ClazzBean save(ClazzBean clazzBean) throws Exception;

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
	 * @param clazzBean
	 * @return
	 * @throws Exception
	 */
	ClazzBean edit(ClazzBean clazzBean) throws Exception;

	/**
	 * 获得班级对象
	 * 
	 * @param id
	 * @return
	 */
	ClazzBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param clazzBean
	 * @return
	 */
	DataGrid datagrid(ClazzBean clazzBean);

	/**
	 * 根据名字模糊查询班级集合
	 * 
	 * @param studentBean
	 * @return
	 */
	List<ClazzBean> getClazzs(StudentBean studentBean);

	List<ClazzBean> getClazzData(ClazzBean clazzBean);

}
