package cn.edu.sxau.dormitorymanage.action;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class CreateBarChartAction extends BaseAction {
	private static final long serialVersionUID = 6058400369615051327L;
	/**
	 * 此类为jfreechart的核心类， 在web项目中使用通过struts2的chart类型返回柱状图形时其实就是返回的此对象， 所有此对象必须有get/set方法
	 */
	JFreeChart chart = null;

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	/**
	 * 获得数据集。
	 * 
	 * @return org.jfree.data.category.CategoryDataset
	 */
	private static CategoryDataset getDataset() {
		double[][] data = new double[][] { { 751, 800, 260, 600, 200 }, { 400, 560, 240, 300, 150 }, { 600, 450, 620, 220, 610 } };
		String[] rowKeys = { "CPU", "硬盘", "内存" };
		String[] columnKeys = { "北京", "上海", "广州", "南京", "深圳" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
		return dataset;
	}

	/**
	 * 生成柱状图。
	 */
	public String makeBarChart3D() {
		String title = "电脑配件三月销量";
		// 获得数据集
		CategoryDataset dataset = getDataset();
		chart = ChartFactory.createBarChart3D(title, // 图表标题
				"配件", // 目录轴的显示标签
				"销量", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例
				true, // 是否生成工具（提示）
				true // 是否生成URL链接
				);
		// 设置标题字体
		Font font = new Font("宋体", Font.BOLD, 18);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		chart.setTextAntiAlias(false);
		// 设置背景色
		chart.setBackgroundPaint(new Color(255, 255, 255));
		// 设置图例字体
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", Font.TRUETYPE_FONT, 13));

		// 获得柱状图的Plot对象
		CategoryPlot plot = chart.getCategoryPlot();
		// 取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		// 设置横轴显示标签的字体
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
		// 设置横轴标记的字体
		categoryAxis.setTickLabelFont(new Font("宋休", Font.TRUETYPE_FONT, 16));
		// 取得纵轴
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		// 设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));
		/**************************************************************/
		/*
		 * ChartFrame frame = new ChartFrame(title, chart, true); //调整此窗口的大小，以适合其子组件的首选大小和布局。 frame.pack(); //隐藏或显示此windows窗口 frame.setVisible(true);
		 */
		return SUCCESS;
	}
}
