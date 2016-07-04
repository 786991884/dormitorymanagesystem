package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.ClazzBean;
import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.ClazzService;
import cn.edu.sxau.dormitorymanage.service.ProfessionService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class ClazzAction extends BaseAction implements ModelDriven<ClazzBean> {
	private static final long serialVersionUID = 4717155764403177789L;
	private ClazzBean clazzBean = new ClazzBean();
	@Resource
	private ClazzService clazzService;
	@Resource
	private ProfessionService professionService;

	@Override
	public ClazzBean getModel() {
		return clazzBean;
	}
	/**
	 * 跳转到班级管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "clazz";
	}
	public void add() {
		Json j = new Json();
		try {
			ClazzBean c = clazzService.save(clazzBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(c);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(clazzService.datagrid(clazzBean));
	}

	public void remove() {
		clazzService.remove(clazzBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		ClazzBean c;
		Json j = new Json();
		try {
			c = clazzService.edit(clazzBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(c);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);
	}

	public void getProfessions() {
		super.writeJson(professionService.getProfessions(clazzBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<ClazzBean> list = clazzService.getClazzData(clazzBean);// 获取数据
		String title = "班级Excel表";
		String[] hearders = new String[] { "编号", "班级名称", "班主任", "电话", "备注信息", "专业名称" };// 表头数组
		String[] fields = new String[] { "id", "name", "teachername", "telphone", "memo", "professionname" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
