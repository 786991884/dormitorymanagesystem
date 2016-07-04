package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 专业实体
 * 
 */
public class Profession implements Serializable {

	private static final long serialVersionUID = 7343938024177768200L;
	private Integer id;
	private String name;// 专业名称
	private String telephone;// 联系电话
	private String memo;// 备注
	private College college;// 所属学院
	private Set<Clazz> clazzs = new HashSet<>();//拥有班级

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

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public Set<Clazz> getClazzs() {
		return clazzs;
	}

	public void setClazzs(Set<Clazz> clazzs) {
		this.clazzs = clazzs;
	}
}
