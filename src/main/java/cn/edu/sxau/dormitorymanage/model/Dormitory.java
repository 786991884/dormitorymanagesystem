package cn.edu.sxau.dormitorymanage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 宿舍信息
 * 
 */
public class Dormitory implements Serializable {

	private static final long serialVersionUID = 3062854231558948947L;
	private Integer id;
	private String number;// 房间号
	private Integer type;// 房间类型
	private String memo;// 备注信息
	private Building building;// 所属楼宇
	private Set<Bed> beds = new HashSet<>();//拥有床位

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Set<Bed> getBeds() {
		return beds;
	}

	public void setBeds(Set<Bed> beds) {
		this.beds = beds;
	}
}
