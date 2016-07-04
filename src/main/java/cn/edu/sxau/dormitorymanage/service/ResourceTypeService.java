package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.ResourceTypeBean;

public interface ResourceTypeService {
	/**
	 * 获取资源类型
	 * 
	 * @return
	 */
	public List<ResourceTypeBean> getResourceTypeList();
}
