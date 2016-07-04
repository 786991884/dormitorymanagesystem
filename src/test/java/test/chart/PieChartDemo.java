package test.chart;

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartDemo {
	public static void main(String[] args) throws IOException {
		DefaultPieDataset data = getDateSet();
		// 生成普通饼图
		JFreeChart chart = ChartFactory.createPieChart("图书销量统计图", // 图表标题
				getDateSet(), // 数据
				true,// 是否显示图例
				false, // 是否显示工具提示
				false// 是否生成URL
				);
		// 重新设置图表标题，改变字体
		chart.setTitle(new TextTitle("图书销量统计图", new Font("黑体", Font.ITALIC, 22)));
		// 取得统计图表的第一个图例
		LegendTitle legend = chart.getLegend(0);
		// 修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14));
		// 修改饼图的plot对象
		PiePlot plot = (PiePlot) chart.getPlot();
		// 设置饼图各部分的标签字体
		plot.setLabelFont(new Font("隶书", Font.BOLD, 18));
		// 设置背景透明度
		plot.setBackgroundAlpha(0.9f);
		FileOutputStream fos = new FileOutputStream("book.jpg");
		ChartUtilities.writeChartAsJPEG(fos,// 输出到哪个输出流
				1,// jpeg图片的质量
				chart,// 统计图表对象
				800,// 宽
				600,// 高
				null// ChartRenderingInfo信息
				);
		fos.close();
	}

	private static DefaultPieDataset getDateSet() {
		// 提供生成饼图的数据
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("疯狂java讲义", 47000);
		dataset.setValue("轻量级java ee企业实战", 38000);
		dataset.setValue("疯狂ajax讲义", 31000);
		dataset.setValue("struts2权威指南", 29000);
		dataset.setValue("疯狂xml讲义", 27000);
		dataset.setValue("疯狂javascript讲义", 24000);
		return dataset;
	}
}
