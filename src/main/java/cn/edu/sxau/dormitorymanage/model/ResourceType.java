package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ResourceType implements Serializable {
	private static final long serialVersionUID = -3944021893242669541L;
	private Integer id;
	private String name;
	private Set<Resource> resources = new HashSet<>(0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
