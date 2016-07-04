package cn.edu.sxau.dormitorymanage.action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.edu.sxau.dormitorymanage.service.InitService;

@Controller
@Scope("prototype")
public class InitAction extends BaseAction {

	private static final long serialVersionUID = 7786904184101661312L;
	@Resource
	private InitService initService;

	public void init() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session != null && !session.isEmpty()) {
			session.clear();
		}
		initService.init();
	}
}
