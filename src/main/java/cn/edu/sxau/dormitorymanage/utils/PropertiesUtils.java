package cn.edu.sxau.dormitorymanage.utils;

import java.util.ResourceBundle;

public class PropertiesUtils {

	private static ResourceBundle rb;

	// 初始化资源文件
	static {
		rb = ResourceBundle.getBundle("violation");
	}

	private PropertiesUtils() {
	}

	public static String getProValue(String key) {
		return rb.getString(key);
	}

}
