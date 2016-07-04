package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.ClazzBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.ProfessionBean;
import cn.edu.sxau.dormitorymanage.model.Profession;

public interface ProfessionService extends BaseService<Profession> {
	/**
	 * 添加专业
	 * 
	 * @param professionBean
	 * @throws Exception
	 */
	ProfessionBean save(ProfessionBean professionBean) throws Exception;

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
	 * @param professionBean
	 * @return
	 * @throws Exception
	 */
	ProfessionBean edit(ProfessionBean professionBean) throws Exception;

	/**
	 * 获得专业对象
	 * 
	 * @param id
	 * @return
	 */
	ProfessionBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param professionBean
	 * @return
	 */
	DataGrid datagrid(ProfessionBean professionBean);

	/**
	 * 模糊查询专业集合
	 * 
	 * @param clazzBean
	 * @return
	 */
	List<ProfessionBean> getProfessions(ClazzBean clazzBean);

	List<ProfessionBean> getProfessionData(ProfessionBean professionBean);

}
