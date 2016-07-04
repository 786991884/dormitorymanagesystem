package cn.edu.sxau.dormitorymanage.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生模型对象
 * 
 */
public class StudentBean implements Serializable {

	private static final long serialVersionUID = -1434919608899012909L;
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
	private int page;
	private int rows;
	private String sort;
	private String order;
	private String q;
	private String ids;
	private String clazzname;// 班级名称
	private String bedname;// 班级名称

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


	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public String getClazzname() {
		return clazzname;
	}

	public void setClazzname(String clazzname) {
		this.clazzname = clazzname;
	}

	public String getBedname() {
		return bedname;
	}

	public void setBedname(String bedname) {
		this.bedname = bedname;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
