package cn.edu.sxau.dormitorymanage.interceptor;

import cn.edu.sxau.dormitorymanage.model.Student;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LoginInterceptor implements Interceptor {

	private static final long serialVersionUID = -2280664193494620101L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// 逻辑是啥？？
		// 先取得Session用户登陆的标志变量，如果存在，是合法放行继续往下执行
		// 不合法的就让它到登陆页面
		Student student = (Student) invocation.getInvocationContext().getSession().get("admin");
		String actionName = invocation.getProxy().getActionName();
		String namespace = invocation.getProxy().getNamespace();
		System.out.println("---------------------------------------------");
		System.out.println("action:" + actionName);
		System.out.println("namespace:" + namespace);
		if (student == null) {// 木通过登陆
			// 放行login.action
			// 1、请求Action的name
			// 格力一定要放行登陆Action
			if ("login".equals(actionName) && "/admin".equals(namespace)) {
				String result = invocation.invoke();
				return result;
			}
			return "login";

		} else {
			String result = invocation.invoke();
			System.out.println(result);
			return result;
		}
	}

}
