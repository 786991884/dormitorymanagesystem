package cn.edu.sxau.dormitorymanage.interceptor;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.utils.ConfigUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 权限拦截器
 * 
 */
public class SecurityInterceptor implements Interceptor {

	private static final long serialVersionUID = 4746410574782436207L;
	private List<String> excludeUrls;// 不需要拦截的资源

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init() {
		excludeUrls = new ArrayList<>();
		excludeUrls.add("initAction!init.action");// 初始化数据库
		excludeUrls.add("menuAction!getAllTreeNode.action");// 菜单
		excludeUrls.add("resourceAction!tree.action");// 首页左侧功能菜单
		//excludeUrls.add("userAction!reg.action");// 用户注册
		excludeUrls.add("userAction!login.action");// 用户登录
		excludeUrls.add("userAction!logout.action");// 注销登录
		excludeUrls.add("resourceAction!allTree.action");// 角色访问资源下拉树
		excludeUrls.add("roleAction!tree.action");// 用户访问角色树(只能看到自己拥有的角色)
		excludeUrls.add("roleAction!allTree.action");// 用户访问角色树
		excludeUrls.add("userAction!editCurrentUserPwdPage.action");// 用户修改自己的密码页面
		excludeUrls.add("userAction!editCurrentUserPwd.action");// 用户修改自己的密码
		excludeUrls.add("userAction!currentUserRolePage.action");// 查看自己的角色页面
		excludeUrls.add("userAction!currentUserResourcePage.action");// 查看自己的权限页面
		excludeUrls.add("bedAction!getStudents.action");// 得到学生信息下拉列表
		excludeUrls.add("bedAction!getDormitorys.action");// 得到宿舍信息下拉列表
		excludeUrls.add("buildingAction!getStaffs.action");// 得到员工信息下拉列表
		excludeUrls.add("clazzAction!getProfessions.action");// 得到专业信息下拉列表
		excludeUrls.add("dormitoryAction!getBuildings.action");// 得到楼宇信息下拉列表
		excludeUrls.add("professionAction!getColleges.action");// 得到学院信息下拉列表
		excludeUrls.add("staffAction!getBuildings.action");// 得到楼宇信息下拉列表
		excludeUrls.add("studentAction!getClazzs.action");// 得到班级信息下拉列表
		excludeUrls.add("studentAction!getBeds.action");// 得到床位信息下拉列表
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		SessionInfo sessionInfo = (SessionInfo) invocation.getInvocationContext().getSession().get(ConfigUtil.getSessionInfoName());
		String actionName = invocation.getProxy().getActionName();
		String namespace = invocation.getProxy().getNamespace();
		String method = invocation.getProxy().getMethod();
		String privilegeUrl = null;
		if (namespace.endsWith("/")) {
			privilegeUrl = namespace + actionName + "!" + method + ".action";
		} else {
			privilegeUrl = namespace + "/" + actionName + "!" + method + ".action";
		}
		// 要去掉开头的'/'
		if (privilegeUrl.startsWith("/")) {
			privilegeUrl = privilegeUrl.substring(1);
		}
		if (excludeUrls.contains(privilegeUrl)) {// 如果要访问的资源是不需要验证的
			return invocation.invoke();
		}
		if (sessionInfo == null || sessionInfo.getId().equals("")) {// 如果没有登录或登录超时
			ActionContext.getContext().put("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			return "index";
		}

		if (!sessionInfo.getResourceList().contains(privilegeUrl)) {// 如果当前用户没有访问此资源的权限
			ActionContext.getContext().put("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + privilegeUrl + "]<br/>的资源访问权限！");
			return "noSecurity";
		}
		return invocation.invoke();
	}
}
