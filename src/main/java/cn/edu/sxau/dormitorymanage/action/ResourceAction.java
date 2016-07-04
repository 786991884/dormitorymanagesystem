package cn.edu.sxau.dormitorymanage.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.Json;
import cn.edu.sxau.dormitorymanage.bean.ResourceBean;
import cn.edu.sxau.dormitorymanage.bean.SessionInfo;
import cn.edu.sxau.dormitorymanage.bean.Tree;
import cn.edu.sxau.dormitorymanage.service.ResourceService;
import cn.edu.sxau.dormitorymanage.service.ResourceTypeService;
import cn.edu.sxau.dormitorymanage.utils.ConfigUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class ResourceAction extends BaseAction implements ModelDriven<ResourceBean> {
	private static final long serialVersionUID = -7467090704799760982L;
	private ResourceBean resourceBean = new ResourceBean();
	@Resource
	private ResourceService resourceService;
	@Resource
	private ResourceTypeService resourceTypeService;

	@Override
	public ResourceBean getModel() {
		return resourceBean;
	}

	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @return
	 */
	public void tree() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		List<Tree> trees = resourceService.tree(sessionInfo);
		super.writeJson(trees);
	}

	/**
	 * 获得资源树(包括所有资源类型)
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @return
	 */
	public void allTree() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		List<Tree> allTree = resourceService.allTree(sessionInfo);
		super.writeJson(allTree);
	}

	/**
	 * 跳转到资源管理页面
	 * 
	 * @return
	 */
	public String manager() {
		return "resource";
	}

	/**
	 * 跳转到资源添加页面
	 * 
	 * @return
	 */
	public String addPage() {
		ActionContext.getContext().put("resourceTypeList", resourceTypeService.getResourceTypeList());
		ResourceBean resourceBean = new ResourceBean();
		ActionContext.getContext().put("resource", resourceBean);
		return "resourceAdd";
	}

	/**
	 * 添加资源
	 * 
	 * @return
	 */
	public void add() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		resourceService.add(resourceBean, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		super.writeJson(j);
	}

	/**
	 * 跳转到资源编辑页面
	 * 
	 * @return
	 */
	public String editPage() {
		ActionContext.getContext().put("resourceTypeList", resourceTypeService.getResourceTypeList());
		ResourceBean r = resourceService.get(resourceBean.getId());
		ActionContext.getContext().put("resource", r);
		return "resourceEdit";
	}

	/**
	 * 编辑资源
	 * 
	 * @param resource
	 * @return
	 */
	public void edit() {
		Json j = new Json();
		resourceService.edit(resourceBean);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		super.writeJson(j);
	}

	/**
	 * 获得资源列表
	 * 
	 * 通过用户ID判断，他能看到的资源
	 * 
	 * @return
	 */
	public void treeGrid() {
		SessionInfo sessionInfo = (SessionInfo) ActionContext.getContext().getSession().get(ConfigUtil.getSessionInfoName());
		List<ResourceBean> treeGrid = resourceService.treeGrid(sessionInfo);
		super.writeJson(treeGrid);
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 */
	public void delete() {
		Json j = new Json();
		resourceService.delete(resourceBean.getId());
		j.setMsg("删除成功！");
		j.setSuccess(true);
		super.writeJson(j);
	}
}
