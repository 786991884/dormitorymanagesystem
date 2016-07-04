package test.chart;

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart3DDemo {
	public static void main(String[] args) throws IOException {
		// 生成普通饼图
		// JFreeChart chart = ChartFactory.createBarChart3D("图书销量统计图", // 图表标题
		// 生成普通拆线图
		JFreeChart chart = ChartFactory.createLineChart3D("图书销量统计图", // 图表标题
				"图书", // 目录轴的显示标签
				"销量",// 数值轴的显示标签
				getDateSet(), // 数据集
				// PlotOrientation.HORIZONTAL,//图表方向：水平
				PlotOrientation.VERTICAL,// 图表方向：垂直
				false,// 是否显示图例（对于简单的柱状图必须是false）
				false,// 是否生成工具
				false// 是否生成url链接
				);
		// 重新设置图表标题，改变字体
		chart.setTitle(new TextTitle("图书销量统计图", new Font("黑体", Font.ITALIC, 22)));
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		// 取得横轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		// 设置横轴显示标签的字体
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		// 分类标签以45度角倾斜
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 18));
		// 取得纵轴
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		// 设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		// 输出到图片文件
		FileOutputStream fos = null;
		fos = new FileOutputStream("book.jpg");
		// 将统计图表输出成JPG文件
		ChartUtilities.writeChartAsJPEG(fos,// 输出到哪个输出流
				1,// jpeg图片的质量
				chart,// 统计图表对象
				800,// 宽
				600,// 高
				null// ChartRenderingInfo信息
				);
		fos.close();
	}

	// 返回一个CategoryDataset的实例
	private static CategoryDataset getDateSet() {
		// 提供生成统计图表的数据
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(47000, "", "疯狂java讲义");
		dataset.addValue(38000, "", "轻量级java ee企业实战");
		dataset.addValue(31000, "", "疯狂ajax讲义");
		dataset.addValue(29000, "", "struts2权威指南");
		dataset.addValue(27000, "", "疯狂xml讲义");
		dataset.addValue(24000, "", "疯狂javascript讲义");
		return dataset;
	}
}
