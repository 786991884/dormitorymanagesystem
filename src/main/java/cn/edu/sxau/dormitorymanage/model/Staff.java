package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;

/**
 * 员工信息
 * 
 */
public class Staff implements Serializable {
	private static final long serialVersionUID = 3953336360167515026L;
	private Integer id;
	private String name;// 职工姓名
	private String sex;// 职工性别
	private String telephone;// 职工电话
	private String memo;// 备注信息
	private Building building;// 管理楼宇

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
}
