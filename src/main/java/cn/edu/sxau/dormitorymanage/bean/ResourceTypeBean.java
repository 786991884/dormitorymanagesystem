package cn.edu.sxau.dormitorymanage.bean;

import java.io.Serializable;

public class ResourceTypeBean implements Serializable {

	private static final long serialVersionUID = 8903006429210851909L;
	private Integer id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
