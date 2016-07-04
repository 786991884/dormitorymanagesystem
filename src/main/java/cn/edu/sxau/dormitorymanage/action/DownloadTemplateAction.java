package cn.edu.sxau.dormitorymanage.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.utils.ExcelUtil;

@Controller
@Scope("prototype")
public class DownloadTemplateAction extends BaseAction {
	private static final long serialVersionUID = 4097913997653652951L;

	public void studentTemplate() {
		ExcelUtil excelUtil = new ExcelUtil();
		try {
			String excelName = "学生住宿信息模版.xls";
			String sheetName = "学生住宿信息";
			String[] fieldName = new String[] { "学号", "姓名", "性别", "出生日期", "联系电话", "家庭住址", "毕业时间", "入住时间", "所属班级", "所属床位", "备注信息" };
			List<Object[]> data = new ArrayList<>(0);
			excelUtil.makeStreamExcel(excelName, sheetName, fieldName, data, ServletActionContext.getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
