package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.BedService;
import cn.edu.sxau.dormitorymanage.service.DormitoryService;
import cn.edu.sxau.dormitorymanage.service.StudentService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class BedAction extends BaseAction implements ModelDriven<BedBean> {
	private static final long serialVersionUID = 2073062370830623800L;
	private BedBean bedBean = new BedBean();
	@Resource
	private BedService bedService;
	@Resource
	private StudentService studentService;
	@Resource
	private DormitoryService dormitoryService;

	@Override
	public BedBean getModel() {
		return bedBean;
	}

	/**
	 * 跳转到床位管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "bed";
	}

	public void add() {
		Json j = new Json();
		try {
			BedBean b = bedService.save(bedBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(bedService.datagrid(bedBean));
	}

	public void remove() {
		bedService.remove(bedBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		BedBean b;
		Json j = new Json();
		try {
			b = bedService.edit(bedBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(b);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		super.writeJson(j);
	}

	public void getStudents() {
		super.writeJson(studentService.getStudents(bedBean));
	}

	public void getDormitorys() {
		super.writeJson(dormitoryService.getDormitorys(bedBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<BedBean> list = bedService.getBedData(bedBean);// 获取数据
		String title = "床位Excel表";
		String[] hearders = new String[] { "编号", "床位名", "备注信息", "宿舍名", "学号" };// 表头数组
		String[] fields = new String[] { "id", "name", "memo", "dormitoryname", "studentnumber" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
