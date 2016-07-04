package cn.edu.sxau.dormitorymanage.bean;

import java.io.Serializable;

/**
 * 
 * 床位模型对象
 */
public class BedBean implements Serializable {

	private static final long serialVersionUID = 8902490751819598506L;
	private Integer id;
	private String name;// 床位名
	private String memo;// 备注信息
	private int page;
	private int rows;
	private String sort;
	private String order;
	private String ids;
	private String q;
	private String dormitoryname;// 寝室名
	private String studentnumber;// 学号

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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDormitoryname() {
		return dormitoryname;
	}

	public void setDormitoryname(String dormitoryname) {
		this.dormitoryname = dormitoryname;
	}

	public String getStudentnumber() {
		return studentnumber;
	}

	public void setStudentnumber(String studentnumber) {
		this.studentnumber = studentnumber;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
}
