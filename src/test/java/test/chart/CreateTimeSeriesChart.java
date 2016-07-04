package test.chart;

import java.awt.Font;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class CreateTimeSeriesChart {
	public static void makeTimeSeriesChart() {
		// 实例化TimeSeries对象
		TimeSeries timeseries = new TimeSeries("Data");
		// 实例化Day
		Day day = new Day(1, 1, 2009);
		double d = 3000D;
		// 添加一年365天的数据
		for (int i = 0; i < 365; i++) {
			// 创建随机数据
			d = d + (Math.random() - 0.5) * 10;
			// 向数据集合中添加数据
			timeseries.add(day, d);
			day = (Day) day.next();
		}
		// 创建TimeSeriesCollection集合对象
		TimeSeriesCollection dataset = new TimeSeriesCollection(timeseries);
		// 生成时序图
		JFreeChart chart = ChartFactory.createTimeSeriesChart("上证指数时序图",// 标题
				"日期", // 时间轴标签
				"指数", // 数据轴标签
				dataset, // 数据集合
				true, // 是否显示图例标识
				true, // 是否显示tooltips
				false); // 是否支持超链接
		String title = "上证指数时序图";
		// 设置图例字体
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("宋体", Font.TRUETYPE_FONT, 15));
		// 设置标题字体
		Font font = new Font("宋体", Font.BOLD, 20);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		// Plot 对象的获取操作
		XYPlot plot = chart.getXYPlot();
		// X 轴对象的获取操作
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		// 设置日期显示格式
		axis.setDateFormatOverride(new SimpleDateFormat("MM-dd-yyyy"));
		// 设置X轴标签字体
		axis.setLabelFont(new Font("宋体", Font.BOLD, 14));
		// Y 轴对象的获取操作
		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
		// 设置Y轴标签字体
		numAxis.setLabelFont(new Font("宋体", Font.BOLD, 14));
		/***************************************************************/
		ChartFrame cf = new ChartFrame("时序图", chart);
		cf.pack();
		cf.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 时序图
		makeTimeSeriesChart();
	}
}