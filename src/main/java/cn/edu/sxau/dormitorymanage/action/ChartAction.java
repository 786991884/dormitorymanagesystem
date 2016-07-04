package cn.edu.sxau.dormitorymanage.action;

import javax.annotation.Resource;

import org.jfree.chart.JFreeChart;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.edu.sxau.dormitorymanage.service.BuildingService;
import cn.edu.sxau.dormitorymanage.service.CollegeService;
import cn.edu.sxau.dormitorymanage.service.DormitoryService;
import cn.edu.sxau.dormitorymanage.service.ProfessionService;
import cn.edu.sxau.dormitorymanage.service.StudentService;

@Controller
@Scope("prototype")
public class ChartAction extends BaseAction {
	private static final long serialVersionUID = -3559214681828621847L;
	@Resource
	private StudentService studentService;
	@Resource
	private BuildingService buildingService;
	@Resource
	private DormitoryService dormitoryService;
	@Resource
	private ProfessionService professionService;
	@Resource
	private CollegeService collegeService;
	private JFreeChart chart = null;

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	/**
	 * 学校男女比例饼状图
	 */
	public String makePieChart3DManWoman() {
		chart = studentService.getPieChart3DManWoman();
		return SUCCESS;
	}

	/**
	 * 宿舍类型数量饼图
	 */
	public String makePieChart3DDormType() {
		chart = dormitoryService.getPieChart3DDormType();
		return SUCCESS;
	}

	/**
	 * 楼宇类型比例柱状图
	 * 
	 * @return
	 */
	public String makeBarChartBuildType() {
		chart = buildingService.getBarChartBuildType();
		return SUCCESS;
	}

	/**
	 * 学院专业数量比例柱状图
	 * 
	 * @return
	 */
	public String makeBarChartColProfNum() {
		chart = collegeService.getBarChartColProfNum();
		return SUCCESS;
	}

	/**
	 * 学院学生数量比例饼状图
	 * 
	 * @return
	 */
	public String makePieChart3DColStuNum() {
		chart = studentService.getPieChart3DColStuNum();
		return SUCCESS;
	}

	/**
	 * 楼宇学生数量比例饼状图
	 * 
	 * @return
	 */
	public String makePieChart3DBuildStuNum() {
		chart = studentService.getPieChart3DBuildStuNum();
		return SUCCESS;
	}

}
