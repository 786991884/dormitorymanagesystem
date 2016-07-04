package cn.edu.sxau.dormitorymanage.dao.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import cn.edu.sxau.dormitorymanage.dao.ResourceTypeDao;
import cn.edu.sxau.dormitorymanage.model.ResourceType;

@Repository
public class ResourceTypeDaoImpl extends BaseDaoImpl<ResourceType> implements ResourceTypeDao {

	@Override
	@Cacheable(value = "resourceTypeDaoCache", key = "#id")
	public ResourceType getById(Integer id) {
		return super.getById(ResourceType.class, id);
	}

}
