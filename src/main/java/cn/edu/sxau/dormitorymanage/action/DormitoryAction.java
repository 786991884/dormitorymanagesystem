package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.DormitoryBean;
import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.BuildingService;
import cn.edu.sxau.dormitorymanage.service.DormitoryService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class DormitoryAction extends BaseAction implements ModelDriven<DormitoryBean> {
	private static final long serialVersionUID = 1205896970628845508L;
	private DormitoryBean dormitoryBean = new DormitoryBean();
	@Resource
	private DormitoryService dormitoryService;
	@Resource
	private BuildingService buildingService;

	@Override
	public DormitoryBean getModel() {
		return dormitoryBean;
	}

	/**
	 * 跳转到宿舍管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "dormitory";
	}

	public void add() {
		Json j = new Json();
		try {
			DormitoryBean d = dormitoryService.save(dormitoryBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(d);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(dormitoryService.datagrid(dormitoryBean));
	}

	public void remove() {
		dormitoryService.remove(dormitoryBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		DormitoryBean d;
		Json j = new Json();
		try {
			d = dormitoryService.edit(dormitoryBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(d);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		super.writeJson(j);
	}

	public void getBuildings() {
		super.writeJson(buildingService.getBuildings(dormitoryBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<DormitoryBean> list = dormitoryService.getDormitoryData(dormitoryBean);// 获取数据
		String title = "宿舍Excel表";
		String[] hearders = new String[] { "编号", "房间号", "房间类型", "备注信息", "楼宇名称" };// 表头数组
		String[] fields = new String[] { "id", "number", "type", "memo", "buildingname" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
