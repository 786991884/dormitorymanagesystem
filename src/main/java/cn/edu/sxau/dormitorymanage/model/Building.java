package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 楼宇信息
 */
public class Building implements Serializable {
	private static final long serialVersionUID = -1943452096812459012L;
	private Integer id;
	private String number;// 楼宇号
	private Integer height;// 楼宇层数
	private String type;// 楼宇类型 男女男女混合
	private String memo;// 备注信息
	private Staff staff;// 管理员工
	private Set<Dormitory> dormitories = new HashSet<>();// 拥有宿舍

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Set<Dormitory> getDormitories() {
		return dormitories;
	}

	public void setDormitories(Set<Dormitory> dormitories) {
		this.dormitories = dormitories;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
