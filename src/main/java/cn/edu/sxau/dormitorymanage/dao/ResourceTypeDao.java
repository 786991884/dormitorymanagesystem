package cn.edu.sxau.dormitorymanage.dao;

import cn.edu.sxau.dormitorymanage.model.ResourceType;

public interface ResourceTypeDao extends BaseDao<ResourceType> {
	/**
	 * 通过ID获得资源类型
	 * 
	 * @param id
	 * @return
	 */
	public ResourceType getById(Integer id);
}
