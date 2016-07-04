package cn.edu.sxau.dormitorymanage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.bean.ResourceTypeBean;
import cn.edu.sxau.dormitorymanage.dao.ResourceTypeDao;
import cn.edu.sxau.dormitorymanage.model.ResourceType;
import cn.edu.sxau.dormitorymanage.service.ResourceTypeService;

@Service
public class ResourceTypeServiceImpl extends BaseServiceImpl<ResourceType> implements ResourceTypeService {
	@Autowired
	private ResourceTypeDao resourceTypeDao;

	@Override
	@Cacheable(value = "resourceTypeServiceCache", key = "'resourceTypeList'")
	public List<ResourceTypeBean> getResourceTypeList() {
		List<ResourceType> resourceTypes = resourceTypeDao.find("from ResourceType r");
		List<ResourceTypeBean> resourceTypeBeans = new ArrayList<ResourceTypeBean>();
		if (resourceTypes != null && resourceTypes.size() > 0) {
			for (ResourceType t : resourceTypes) {
				ResourceTypeBean rt = new ResourceTypeBean();
				BeanUtils.copyProperties(t, rt);
				resourceTypeBeans.add(rt);
			}
		}
		return resourceTypeBeans;
	}

}
