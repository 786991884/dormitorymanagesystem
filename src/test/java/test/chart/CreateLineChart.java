package test.chart;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

 
public class CreateLineChart { 
    /** 
     * 获得数据集。 
     * @return org.jfree.data.category.CategoryDataset 
     */ 
    private static CategoryDataset getDataset() { 
        double[][] data = new double[][] { { 751, 800, 260, 600, 200 }, 
                { 400, 560, 240, 300, 150 }, { 600, 450, 620, 220, 610 } }; 
        String[] rowKeys = { "CPU", "硬盘", "内存" }; 
        String[] columnKeys = { "北京", "上海", "广州", "南京", "深圳" }; 
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset( 
                rowKeys, columnKeys, data); 
        return dataset; 
    } 
    /** 
     * 生成折线图。 
     */ 
    public static void makeLineChart() { 
        String title = "电脑配件三月销量"; 
        // 获得数据集 
        CategoryDataset dataset = getDataset(); 
        JFreeChart chart = ChartFactory.createLineChart(title, // 图表标题 
                "配件",                    // 目录轴的显示标签 
                "销量",                    // 数值轴的显示标签 
                dataset,                   // 数据集 
                PlotOrientation.VERTICAL,  // 图表方向：水平、垂直 
                true,                      // 是否显示图例 
                true,                      // 是否生成工具（提示） 
                false                      // 是否生成URL链接 
                ); 
        chart.setTextAntiAlias(false); 
        // 设置背景色 
        chart.setBackgroundPaint(Color.WHITE); 
        // 设置图标题的字体 
        Font font = new Font("宋体", Font.BOLD, 20); 
        TextTitle textTitle = new TextTitle(title); 
        textTitle.setFont(font); 
        chart.setTitle(textTitle); 
        // 设置X轴Y轴的字体 
        Font labelFont = new Font("宋体", Font.BOLD, 16); 
        chart.setBackgroundPaint(Color.WHITE); 
        // 设置图例字体 
        LegendTitle legend = chart.getLegend(0); 
        legend.setItemFont(new Font("宋体", Font.TRUETYPE_FONT, 14)); 
        // 获得plot 
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot(); 
        // x轴 分类轴网格是否可见 
        categoryplot.setDomainGridlinesVisible(true); 
        // y轴 数据轴网格是否可见 
        categoryplot.setRangeGridlinesVisible(true); 
        // 虚线色彩 
        categoryplot.setRangeGridlinePaint(Color.WHITE); 
        // 虚线色彩 
        categoryplot.setDomainGridlinePaint(Color.WHITE); 
        // 设置背景色 
        categoryplot.setBackgroundPaint(Color.lightGray); 
        // 设置轴和面板之间的距离 
        CategoryAxis domainAxis = categoryplot.getDomainAxis(); 
        // 设置横轴标签标题字体 
        domainAxis.setLabelFont(labelFont); 
        // 设置横轴数值标签字体 
        domainAxis.setTickLabelFont(new Font("宋体", Font.TRUETYPE_FONT, 14)); 
        // 横轴上的 
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD); 
        // 设置距离图片左端距离 
        domainAxis.setLowerMargin(0.0); 
        // 设置距离图片右端距离 
        domainAxis.setUpperMargin(0.0); 
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis(); 
        // 设置纵轴显示标签的字体 
        numberaxis.setLabelFont(labelFont); 
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
        numberaxis.setAutoRangeIncludesZero(true); 
        // 获得renderer 
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot 
                .getRenderer(); 
        // series 点（即数据点）可见 
        lineandshaperenderer.setBaseShapesVisible(true); 
        // series 点（即数据点）间有连线可见 
        lineandshaperenderer.setBaseLinesVisible(true); 
        /*******************************************************/ 
        ChartFrame frame = new ChartFrame(title, chart, true); 
        frame.pack(); 
        frame.setVisible(true); 
    } 
    /** 
     * @param args 
     */ 
    public static void main(String[] args) { 
        // 曲线图 
        makeLineChart(); 
    } 
}