package test.chart;

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class TimeChartDemo {
	public static void main(String[] args) throws IOException {
		JFreeChart chart = ChartFactory.createTimeSeriesChart("图书销量统计图", // 图表标题
				"图书", // 目录轴的显示标签
				"销量",// 数值轴的显示标签
				getDateSet(), // 数据集
				true,// 是否显示图例（对于简单的柱状图必须是false）
				false,// 是否生成工具
				false// 是否生成url链接
				);
		// 重新设置图表标题，改变字体
		chart.setTitle(new TextTitle("图书销量统计图", new Font("黑体", Font.ITALIC, 22)));
		// 取得统计图表的第一个图例
		LegendTitle legend = chart.getLegend(0);
		// 修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14));
		// 取得时间顺序图的Plot对象
		XYPlot plot = (XYPlot) chart.getPlot();
		// 取得横轴
		ValueAxis valueAxis = plot.getDomainAxis();
		// 设置横轴显示标签的字体
		valueAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		valueAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 18));
		// 取得纵轴
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		// 设置纵轴显示标签的字体
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		// 输出到图片文件
		FileOutputStream fos = null;
		fos = new FileOutputStream("book1.jpg");
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
	private static XYDataset getDateSet() {
		// 创建一个timeseries对象
		TimeSeries spring = new TimeSeries("疯狂Java讲义", Month.class);
		spring.add(new Month(10, 2006), 3400);
		spring.add(new Month(11, 2006), 2700);
		spring.add(new Month(12, 2006), 3100);
		spring.add(new Month(1, 2007), 1800);
		spring.add(new Month(2, 2007), 2200);
		// 创建第二个Timeseries对象
		TimeSeries lightWeight = new TimeSeries("轻量级java ee企业应用实战", Month.class);
		lightWeight.add(new Month(10, 2006), 2800);
		lightWeight.add(new Month(11, 2006), 3700);
		lightWeight.add(new Month(12, 2006), 2600);
		lightWeight.add(new Month(1, 2007), 2100);
		lightWeight.add(new Month(2, 2007), 1100);
		// 将两个TimeSeries对象组合成TimeSeriesCollection
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(spring);
		dataset.addSeries(lightWeight);
		return dataset;
	}
}
