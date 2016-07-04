package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.BedService;
import cn.edu.sxau.dormitorymanage.service.ClazzService;
import cn.edu.sxau.dormitorymanage.service.StudentService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class StudentAction extends BaseAction implements ModelDriven<StudentBean> {
	private static final long serialVersionUID = -3079023029657680271L;
	private StudentBean studentBean = new StudentBean();
	@Resource
	private StudentService studentService;
	@Resource
	private ClazzService clazzService;
	@Resource
	private BedService bedService;

	@Override
	public StudentBean getModel() {
		return studentBean;
	}

	/**
	 * 跳转到学生管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "student";
	}

	public void add() {
		Json j = new Json();
		try {
			StudentBean s = studentService.save(studentBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(s);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	public void datagrid() {
		super.writeJson(studentService.datagrid(studentBean));
	}

	public void remove() {
		studentService.remove(studentBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void edit() {
		StudentBean s;
		Json j = new Json();
		try {
			s = studentService.edit(studentBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(s);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		super.writeJson(j);
	}

	public void getClazzs() {
		super.writeJson(clazzService.getClazzs(studentBean));
	}

	public void getBeds() {
		super.writeJson(bedService.getBeds(studentBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		List<StudentBean> list = studentService.getStudentData(studentBean);// 获取数据
		String title = "学生Excel表";
		String[] hearders = new String[] { "编号", "学号", "姓名", "性别", "出生日期", "电话", "家庭住址", "毕业时间", "入住时间", "备注信息", "班级名称", "床位号" };// 表头数组
		String[] fields = new String[] { "id", "number", "name", "sex", "birthday", "telephone", "address", "graddate", "livedate", "memo", "clazzname", "bedname" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
