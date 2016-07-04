package cn.edu.sxau.dormitorymanage.bean;

import java.io.Serializable;
import java.util.List;

public class SessionInfo implements Serializable {
	private static final long serialVersionUID = -3974732581914996292L;
	private Integer id;// 用户ID
	private String name;// 用户登录名

	private List<String> resourceList;// 用户可以访问的资源地址列表

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}
}
