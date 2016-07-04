package cn.edu.sxau.dormitorymanage.bean;

import java.io.Serializable;

/**
 * 班级模型对象
 * 
 */
public class ClazzBean implements Serializable {

	private static final long serialVersionUID = -8948923811879184550L;
	private Integer id;
	private String name;// 班级名称
	private String teachername;// 班主任
	private String telephone;// 联系电话
	private String memo;// 备注信息
	private int page;
	private int rows;
	private String sort;
	private String order;
	private String q;
	private String ids;
	private String professionname;// 专业名

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

	public String getProfessionname() {
		return professionname;
	}

	public void setProfessionname(String professionname) {
		this.professionname = professionname;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
