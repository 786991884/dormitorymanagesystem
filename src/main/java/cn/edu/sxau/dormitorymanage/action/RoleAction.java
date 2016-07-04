package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.RoleBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;
import cn.edu.sxau.dormitorymanage.service.RoleService;
import cn.edu.sxau.dormitorymanage.utils.ConfigUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction implements ModelDriven<RoleBean> {
	private static final long serialVersionUID = -1412128047845212306L;
	private RoleBean roleBean = new RoleBean();
	@Resource
	private RoleService roleService;

	@Override
	public RoleBean getModel() {
		return roleBean;
	}

	/**
	 * 跳转到角色管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "role";
	}

	/**
	 * 跳转到角色添加页面
	 * 
	 * @return
	 */
	public String addPage() {
		RoleBean r = new RoleBean();
		ActionContext.getContext().put("role", r);
		return "roleAdd";
	}

	/**
	 * 添加角色
	 * 
	 * @return
	 */
	public void add() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		roleService.add(roleBean, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		super.writeJson(j);
	}

	/**
	 * 跳转到角色修改页面
	 * 
	 * @return
	 */
	public String editPage() {
		RoleBean r = roleService.get(roleBean.getId());
		ActionContext.getContext().put("role", r);
		return "roleEdit";
	}

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @return
	 */
	public void edit() {
		Json j = new Json();
		roleService.edit(roleBean);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		super.writeJson(j);
	}

	/**
	 * 获得角色列表
	 * 
	 * @return
	 */
	public void treeGrid() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		List<RoleBean> treeGrid = roleService.treeGrid(sessionInfo);
		super.writeJson(treeGrid);
	}

	/**
	 * 角色树(只能看到自己拥有的角色)
	 * 
	 * @return
	 */
	public void tree() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		List<Tree> trees = roleService.tree(sessionInfo);
		super.writeJson(trees);
	}

	/**
	 * 角色树
	 * 
	 * @return
	 */
	public void allTree() {
		super.writeJson(roleService.allTree());
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	public void delete() {
		Json j = new Json();
		roleService.delete(roleBean.getId());
		j.setMsg("删除成功！");
		j.setSuccess(true);
		super.writeJson(j);
	}

	/**
	 * 跳转到角色授权页面
	 * 
	 * @return
	 */
	public String grantPage() {
		RoleBean r = roleService.get(roleBean.getId());
		ActionContext.getContext().put("role", r);
		return "roleGrant";
	}

	/**
	 * 授权
	 * 
	 * @param role
	 * @return
	 */
	public void grant() {
		Json j = new Json();
		roleService.grant(roleBean);
		j.setMsg("授权成功！");
		j.setSuccess(true);
		super.writeJson(j);
	}
}
