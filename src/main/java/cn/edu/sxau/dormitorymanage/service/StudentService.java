package cn.edu.sxau.dormitorymanage.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import cn.edu.sxau.dormitorymanage.bean.BedBean;
import cn.edu.sxau.dormitorymanage.bean.DataGrid;
import cn.edu.sxau.dormitorymanage.bean.StudentBean;
import cn.edu.sxau.dormitorymanage.model.Student;

public interface StudentService extends BaseService<Student> {
	/**
	 * 添加学生
	 * 
	 * @param studentBean
	 * @throws Exception
	 */
	StudentBean save(StudentBean studentBean) throws Exception;

	/**
	 * 删除单个
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 删除对象单个或者批量
	 * 
	 * @param ids
	 */
	void remove(String ids);

	/**
	 * 
	 * @param studentBean
	 * @return
	 * @throws Exception
	 */
	StudentBean edit(StudentBean studentBean) throws Exception;

	/**
	 * 获得学生对象
	 * 
	 * @param id
	 * @return
	 */
	StudentBean get(String id);

	/**
	 * 为数据表格准备数据
	 * 
	 * @param studentBean
	 * @return
	 */
	DataGrid datagrid(StudentBean studentBean);

	/**
	 * 根据学号模糊查询
	 * 
	 * @param bedBean
	 * @return
	 */
	List<StudentBean> getStudents(BedBean bedBean);

	/**
	 * 学校男女比例饼状图
	 * 
	 * @return
	 */
	JFreeChart getPieChart3DManWoman();

	/**
	 * 学院学生数量饼图
	 * 
	 * @return
	 */
	JFreeChart getPieChart3DColStuNum();

	/**
	 * 楼宇学生数量比例饼状图
	 * 
	 * @return
	 */
	JFreeChart getPieChart3DBuildStuNum();

	List<StudentBean> getStudentData(StudentBean studentBean);
}
