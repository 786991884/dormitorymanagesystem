package cn.edu.sxau.dormitorymanage.action;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.UserBean;
import cn.edu.sxau.dormitorymanage.excelTools.ExcelUtils;
import cn.edu.sxau.dormitorymanage.excelTools.JsGridReportBase;
import cn.edu.sxau.dormitorymanage.excelTools.TableData;
import cn.edu.sxau.dormitorymanage.service.ResourceService;
import cn.edu.sxau.dormitorymanage.service.RoleService;
import cn.edu.sxau.dormitorymanage.service.UserService;
import cn.edu.sxau.dormitorymanage.utils.ConfigUtil;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction implements ModelDriven<UserBean> {
	private static final long serialVersionUID = -8290052228569077571L;
	private UserBean userBean = new UserBean();
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private ResourceService resourceService;

	@Override
	public UserBean getModel() {
		return userBean;
	}

	public void reg() {
		Json j = new Json();
		try {
			userService.reg(userBean);
			j.setSuccess(true);
			j.setMsg("注册成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}

		super.writeJson(j);

	}

	/**
	 * 跳转到添加用户页面
	 * 
	 * @return
	 */
	public String addPage() {
		return "userAdd";
	}

	public void add() {
		Json j = new Json();
		try {
			UserBean u = userService.save(userBean);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setObj(u);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		super.writeJson(j);

	}

	/**
	 * 跳转到用户管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "user";
	}

	public void login() {
		UserBean u = userService.login(userBean);
		Json j = new Json();
		if (u != null) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");
			j.setObj(u);
			// 合法用户，首先保存到Session,然后到合适页面
			//ActionContext.getContext().getSession().put("user", u);
			SessionInfo sessionInfo = new SessionInfo();
			BeanUtils.copyProperties(u, sessionInfo);
			sessionInfo.setResourceList(userService.resourceList(u.getId()));
			ActionContext.getContext().getSession().put(ConfigUtil.getSessionInfoName(), sessionInfo);
		} else {
			j.setMsg("登录失败，用户名或密码错误！");
		}

		super.writeJson(j);
	}

	public void dataGrid() {
		super.writeJson(userService.datagrid(userBean));
	}

	/**
	 * 导出excel数据表格
	 */
	public void exportExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/msexcel;charset=utf-8");
		try {
			String uname = java.net.URLDecoder.decode(userBean.getName(), "UTF-8");
			userBean.setName(uname);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<UserBean> list = userService.getUserData(userBean);// 获取数据
		String title = "用户Excel表";
		String[] hearders = new String[] { "编号", "姓名", "密码", "创建时间", "修改时间" };// 表头数组
		String[] fields = new String[] { "id", "name", "pwd", "createdatetime", "modifydatetime" };// User对象属性数组
		TableData td = ExcelUtils.createTableData(list, ExcelUtils.createTableHeader(hearders), fields);
		try {
			JsGridReportBase report = new JsGridReportBase(request, response);
			report.exportToExcel(title, "admin", td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove() {
		userService.remove(userBean.getIds());
		Json j = new Json();
		j.setSuccess(true);
		j.setMsg("删除成功！");
		super.writeJson(j);
	}

	public void editPwd() {
		Json j = new Json();
		userService.editPwd(userBean);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		super.writeJson(j);
	}

	/**
	 * 跳转到用户修改页面
	 * 
	 * @return
	 */
	public String editPage() {
		UserBean bean = userService.get(userBean.getId());
		ActionContext.getContext().put("user", bean);
		return "userEdit";
	}

	public void edit() {
		UserBean u;
		Json j = new Json();
		try {
			u = userService.edit(userBean);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setObj(u);
			super.writeJson(j);
		} catch (Exception e) {
			j.setSuccess(true);
			j.setMsg(e.getMessage());
			super.writeJson(j);
			e.printStackTrace();
		}

	}

	public void loginCombobox() {
		super.writeJson(userService.loginCombobox(userBean));
	}

	public void loginCombogrid() {
		super.writeJson(userService.loginCombogrid(userBean));
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public void logout() {
		Json j = new Json();
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null) {
			session.clear();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		super.writeJson(j);
	}

	/**
	 * 用户授权
	 * 
	 * @return
	 */
	public void grant() {
		Json j = new Json();
		userService.grant(userBean);
		j.setSuccess(true);
		j.setMsg("授权成功！");
		super.writeJson(j);
	}

	/**
	 * 修改自己的密码
	 * 
	 * @param session
	 * @param pwd
	 * @return
	 */
	public void editCurrentUserPwd() {
		Json j = new Json();
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		if (sessionInfo != null) {
			if (userService.editCurrentUserPwd(sessionInfo, userBean.getOldPwd(), userBean.getPwd())) {
				j.setSuccess(true);
				j.setMsg("编辑密码成功，下次登录生效！");
			} else {
				j.setMsg("原密码错误！");
			}
		} else {
			j.setMsg("登录超时，请重新登录！");
		}
		super.writeJson(j);
	}

	/**
	 * 跳转到显示用户角色页面
	 * 
	 * @return
	 */
	public String currentUserRolePage() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		ActionContext.getContext().put("userRoles", JSON.toJSONString(roleService.tree(sessionInfo)));
		return "userRole";
	}

	/**
	 * 跳转到显示用户权限页面
	 * 
	 * @return
	 */
	public String currentUserResourcePage() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		ActionContext.getContext().put("userResources", JSON.toJSONString(resourceService.allTree(sessionInfo)));
		return "userResource";
	}

	/**
	 * 跳转到用户授权页面
	 * 
	 * @return
	 */
	public String grantPage() {
		ActionContext.getContext().put("ids", userBean.getIds());
		if (userBean.getIds() != null && !userBean.getIds().equalsIgnoreCase("") && userBean.getIds().indexOf(",") == -1) {
			UserBean u = userService.get(Integer.valueOf(userBean.getIds()));
			ActionContext.getContext().put("user", u);
		}
		return "userGrant";
	}

	/**
	 * 跳转到编辑用户密码页面
	 * 
	 * @return
	 */
	public String editPwdPage() {
		UserBean u = userService.get(userBean.getId());
		ActionContext.getContext().put("user", u);
		return "userEditPwd";
	}

}
