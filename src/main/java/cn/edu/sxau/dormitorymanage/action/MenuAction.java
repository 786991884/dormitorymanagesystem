package cn.edu.sxau.dormitorymanage.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.bean.MenuBean;
import cn.edu.sxau.dormitorymanage.service.MenuService;

import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
public class MenuAction extends BaseAction implements ModelDriven<MenuBean> {
	private static final long serialVersionUID = -2227014139089655118L;
	private MenuBean menu = new MenuBean();
	@Resource
	private MenuService menuService;

	@Override
	public MenuBean getModel() {
		return menu;
	}

	/**
	 * 异步获取树节点
	 */
	public void getTreeNode() {
		super.writeJson(menuService.getTreeNode(menu.getId()));
	}

	public void getAllTreeNode() {
		super.writeJson(menuService.getAllTreeNode());
	}

}
