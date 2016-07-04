package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;

/**
 * 床位信息
 * 
 */
public class Bed implements Serializable {

	private static final long serialVersionUID = -376618708576966545L;
	private Integer id;
	private String name;// 床位名
	private String memo;// 备注信息
	private Student student;// 所属学生
	private Dormitory dormitory;// 所属寝室

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Dormitory getDormitory() {
		return dormitory;
	}

	public void setDormitory(Dormitory dormitory) {
		this.dormitory = dormitory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
