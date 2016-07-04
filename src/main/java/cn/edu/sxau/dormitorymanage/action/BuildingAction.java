package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.BuildingBean;
import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.BuildingService;
import cn.edu.sxau.dormitorymanage.service.StaffService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class BuildingAction extends BaseAction implements ModelDriven<BuildingBean> {
	private static final long serialVersionUID = 8006751974341017971L;
	private BuildingBean buildingBean = new BuildingBean();
	@Resource
	private BuildingService buildingService;
	@Resource
	private StaffService staffService;

	@Override
	public BuildingBean getModel() {
		return buildingBean;
	}

	/**
	 * 跳转到楼宇管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "building";
	}

	public void add() {
		Json j = new Json();
		try {
			BuildingBean b = buildingService.save(buildingBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(buildingService.datagrid(buildingBean));
	}

	public void remove() {
		buildingService.remove(buildingBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		BuildingBean b;
		Json j = new Json();
		try {
			b = buildingService.edit(buildingBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);
	}

	public void getStaffs() {
		super.writeJson(staffService.getStaffs(buildingBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<BuildingBean> list = buildingService.getBuildingData(buildingBean);// 获取数据
		String title = "楼宇Excel表";
		String[] hearders = new String[] { "编号", "楼宇名", "楼层数", "楼类型", "备注信息", "职工姓名" };// 表头数组
		String[] fields = new String[] { "id", "number", "height", "type", "memo", "staffname" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
