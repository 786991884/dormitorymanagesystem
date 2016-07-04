package cn.edu.sxau.dormitorymanage.action;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
public class FileDownloadAction extends ActionSupport {
	private static final long serialVersionUID = 7954833670124957136L;
	// the download file
	private String fileName;
	// the download folder
	private String inputPath;
	private List<String> fileNames;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		try {
			fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.fileName = fileName;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public InputStream getDownloadFile() throws Exception {
		String url = File.separator + inputPath + File.separator + fileName;
		// 如果下载文件名为中文，进行字符编码转换
		return ServletActionContext.getServletContext().getResourceAsStream(url);
	}

	public String getDownloadFileName() {
		String downFileName = fileName;
		try {
			downFileName = new String(downFileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downFileName;

	}

	public String download() throws Exception {
		String downloadDir = ServletActionContext.getServletContext().getRealPath(File.separator + inputPath);
		File rootFile = new File(downloadDir);
		File[] files = rootFile.listFiles();
		fileNames = new ArrayList<String>();
		for (File f : files) {
			if (!f.isDirectory()) {
				String name = f.getName();
				fileNames.add(name);
			}
		}
		return SUCCESS;
	}

}
