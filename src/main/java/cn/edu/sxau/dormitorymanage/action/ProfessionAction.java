package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.ProfessionBean;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.CollegeService;
import cn.edu.sxau.dormitorymanage.service.ProfessionService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class ProfessionAction extends BaseAction implements ModelDriven<ProfessionBean> {
	private static final long serialVersionUID = 4717155764403177789L;
	private ProfessionBean professionBean = new ProfessionBean();
	@Resource
	private ProfessionService professionService;
	@Resource
	private CollegeService collegeService;

	@Override
	public ProfessionBean getModel() {
		return professionBean;
	}

	/**
	 * 跳转到专业管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "profession";
	}

	public void add() {
		Json j = new Json();
		try {
			ProfessionBean p = professionService.save(professionBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(p);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(professionService.datagrid(professionBean));
	}

	public void remove() {
		professionService.remove(professionBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		ProfessionBean p;
		Json j = new Json();
		try {
			p = professionService.edit(professionBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(p);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);
	}

	public void getColleges() {
		super.writeJson(collegeService.getColleges(professionBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<ProfessionBean> list = professionService.getProfessionData(professionBean);// 获取数据
		String title = "专业Excel表";
		String[] hearders = new String[] { "编号", "专业名称", "联系电话", "备注信息", "学院名称" };// 表头数组
		String[] fields = new String[] { "id", "name", "telephone", "memo", "collegename" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
