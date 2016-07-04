package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 班级实体
 * 
 */
public class Clazz implements Serializable {

	private static final long serialVersionUID = -2323529372822604662L;
	private Integer id;
	private String name;// 班级名称
	private String teachername;// 班主任
	private String telephone;// 联系电话
	private String memo;// 备注信息
	private Profession profession;// 所属专业
	private Set<Student> students = new HashSet<>();// 拥有学生

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

	public String getTeachername() {
		return teachername;
	}

	public void setTeachername(String teachername) {
		this.teachername = teachername;
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

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
