package cn.edu.sxau.dormitorymanage.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.model.Bed;
import cn.edu.sxau.dormitorymanage.model.Clazz;
import cn.edu.sxau.dormitorymanage.model.Student;
import cn.edu.sxau.dormitorymanage.service.BedService;
import cn.edu.sxau.dormitorymanage.service.ClazzService;
import cn.edu.sxau.dormitorymanage.service.StudentService;
import cn.edu.sxau.dormitorymanage.utils.ExcelUtil;
import cn.edu.sxau.dormitorymanage.utils.StringUtil;

@Controller
@Scope("prototype")
public class UploadAction extends BaseAction {

	private static final long serialVersionUID = 1150313694215559339L;
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	@Resource
	private ClazzService clazzService;
	@Resource
	private BedService bedService;
	@Resource
	private StudentService studentService;
	// 注意，file并不是指前端jsp上传过来的文件本身，而是文件上传过来存放在临时文件夹下面的文件
	private File uploadExcel;
	// 提交过来的file的MIME类型
	private String uploadExcelContentType;
	// 提交过来的file的名字
	private String uploadExcelFileName;
	private Map<String,String> message=new HashMap<>();

	public File getUploadExcel() {
		return uploadExcel;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	public void setUploadExcel(File uploadExcel) {
		this.uploadExcel = uploadExcel;
	}

	public String getUploadExcelContentType() {
		return uploadExcelContentType;
	}

	public void setUploadExcelContentType(String uploadExcelContentType) {
		this.uploadExcelContentType = uploadExcelContentType;
	}

	public String getUploadExcelFileName() {
		return uploadExcelFileName;
	}

	public void setUploadExcelFileName(String uploadExcelFileName) {
		this.uploadExcelFileName = uploadExcelFileName;
	}

	public String execute() throws Exception {
		String root = ServletActionContext.getServletContext().getRealPath("/upload");
		InputStream is = new FileInputStream(this.getUploadExcel());
		OutputStream os = new FileOutputStream(new File(root, getUploadExcelFileName()));
		System.out.println("fileFileName: " + getUploadExcelFileName());
		// 因为file是存放在临时文件夹的文件，我们可以将其文件名和文件路径打印出来，看和之前的fileFileName是否相同
		System.out.println("file: " + uploadExcel.getName());
		System.out.println("file: " + uploadExcel.getPath());
		byte[] buffer = new byte[500];
		@SuppressWarnings("unused")
		int length = 0;
		while (-1 != (length = is.read(buffer, 0, buffer.length))) {
			os.write(buffer);
		}
		os.close();
		is.close();
		// 执行解析文件
		EXECUTOR.execute(new AnalysExcel());
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("fileName", uploadExcelFileName);
		return SUCCESS;
	}

	public String uploadTemplateData() {
		try {
			String root = ServletActionContext.getServletContext().getRealPath("/upload");
			File file = new File(root); // 判断文件夹是否存在,如果不存在则创建文件夹
			if (!file.exists()) {
				file.mkdir();
			}
			File f = this.getUploadExcel();
			InputStream is = new FileInputStream(f);
			OutputStream os = new FileOutputStream(new File(root, getUploadExcelFileName()));
			System.out.println("fileFileName: " + getUploadExcelFileName());
			// 因为file是存放在临时文件夹的文件，我们可以将其文件名和文件路径打印出来，看和之前的fileFileName是否相同
			System.out.println("file: " + uploadExcel.getName());
			System.out.println("file: " + uploadExcel.getPath());
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
			os.close();
			is.close();
			// 执行解析文件
			EXECUTOR.execute(new AnalysExcel());
			message.put("success", "文件上传成功！");
			return SUCCESS;
		} catch (Exception e) {
			message.put("error", "对不起,文件上传失败了!");
			return ERROR;
		}
	}


	private class AnalysExcel extends Thread {

		@Override
		public void run() {
			// 获取文件路径
			// 读取excel
			FileInputStream fis;
			Workbook workbook;
			try {
				fis = new FileInputStream(uploadExcel);
				workbook = WorkbookFactory.create(fis);
				if (fis != null) {
					fis.close();
				}
				Sheet sheet = workbook.getSheetAt(0);
				// 循环遍历每一行、每一列。
				for (Row row : sheet) {
					if (row.getRowNum() == 0) {
						continue;
					}
					// 每一行
					Student s = new Student();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					s.setNumber(ExcelUtil.getCellValue(row.getCell(0)));
					s.setName(ExcelUtil.getCellValue(row.getCell(1)));
					s.setSex(ExcelUtil.getCellValue(row.getCell(2)));
					if (row.getCell(3) != null) {
						s.setBirthday(sdf.parse(ExcelUtil.getCellValue(row.getCell(3))));
					}
					s.setTelephone(ExcelUtil.getCellValue(row.getCell(4)));
					s.setAddress(ExcelUtil.getCellValue(row.getCell(5)));
					if (row.getCell(6) != null) {
						s.setGraddate(sdf.parse(ExcelUtil.getCellValue(row.getCell(6))));
					}
					if (row.getCell(7) != null) {
						s.setLivedate(sdf.parse(ExcelUtil.getCellValue(row.getCell(7))));
					}
					s.setMemo(ExcelUtil.getCellValue(row.getCell(8)));
					String clazzname = ExcelUtil.getCellValue(row.getCell(9));
					if (!StringUtil.isEmpty(clazzname)) {
						Clazz clazz = clazzService.getByHql("from Clazz c where c.name=" + clazzname);
						if (clazz != null) {
							s.setClazz(clazz);
						}
					}
					String bedname = ExcelUtil.getCellValue(row.getCell(10));
					if (!StringUtil.isEmpty(bedname)) {
						Bed bed = bedService.getByHql("from Bed b where b.name=" + bedname);
						if (bed != null) {
							s.setBed(bed);
						}
					}
					// 插入或者更新到数据库操作
					studentService.save(s);

				}
				// 更新上传记录表中的上传数量和上传结果

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
