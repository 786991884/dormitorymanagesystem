package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生实体
 * 
 */
public class Student implements Serializable {

	private static final long serialVersionUID = -276039428331174624L;
	private Integer id;
	private String number;// 学号
	private String name;// 姓名
	private String sex;// 性别
	private Date birthday;// 出生日期
	private String telephone;// 联系电话
	private String address;// 家庭住址
	private Date graddate;// 毕业时间
	private Date livedate;// 入住时间
	private String memo;// 备注信息
	private Clazz clazz;// 所属班级
	private Bed bed;// 所属床位

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



	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getGraddate() {
		return graddate;
	}

	public void setGraddate(Date graddate) {
		this.graddate = graddate;
	}

	public Date getLivedate() {
		return livedate;
	}

	public void setLivedate(Date livedate) {
		this.livedate = livedate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Bed getBed() {
		return bed;
	}

	public void setBed(Bed bed) {
		this.bed = bed;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
