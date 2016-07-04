package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.StaffBean;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.BuildingService;
import cn.edu.sxau.dormitorymanage.service.StaffService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class StaffAction extends BaseAction implements ModelDriven<StaffBean> {
	private static final long serialVersionUID = 3634455915051630479L;
	private StaffBean staffBean = new StaffBean();
	@Resource
	private StaffService staffService;
	@Resource
	private BuildingService buildingService;

	@Override
	public StaffBean getModel() {
		return staffBean;
	}

	/**
	 * 跳转到职工管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "staff";
	}

	public void add() {
		Json j = new Json();
		try {
			StaffBean s = staffService.save(staffBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(s);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(staffService.datagrid(staffBean));
	}

	public void remove() {
		staffService.remove(staffBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		StaffBean s;
		Json j = new Json();
		try {
			s = staffService.edit(staffBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(s);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);
	}

	public void getBuildings() {
		super.writeJson(buildingService.getBuildings(staffBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<StaffBean> list = staffService.getStaffData(staffBean);// 获取数据
		String title = "职工Excel表";
		String[] hearders = new String[] { "编号", "姓名", "性别", "电话", "备注信息", "楼宇名称" };// 表头数组
		String[] fields = new String[] { "id", "name", "sex", "telephone", "memo", "buildingname" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
