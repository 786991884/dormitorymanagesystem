package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.CollegeBean;
import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.CollegeService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class CollegeAction extends BaseAction implements ModelDriven<CollegeBean> {
	private static final long serialVersionUID = 1853285725325458107L;
	private CollegeBean collegeBean = new CollegeBean();
	@Resource
	private CollegeService collegeService;

	@Override
	public CollegeBean getModel() {
		return collegeBean;
	}

	/**
	 * 跳转到学院管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "college";
	}

	public void add() {
		Json j = new Json();
		try {
			CollegeBean b = collegeService.save(collegeBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(collegeService.datagrid(collegeBean));
	}

	public void remove() {
		collegeService.remove(collegeBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		CollegeBean b;
		Json j = new Json();
		try {
			b = collegeService.edit(collegeBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		super.writeJson(j);

	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<CollegeBean> list = collegeService.getCollegeData(collegeBean);// 获取数据
		String title = "学院Excel表";
		String[] hearders = new String[] { "编号", "学院名", "院长姓名", "电话", "备注信息" };// 表头数组
		String[] fields = new String[] { "id", "name", "manname", "telphone", "memo" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
