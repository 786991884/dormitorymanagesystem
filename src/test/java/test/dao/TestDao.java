package test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.sxau.dormitorymanage.dao.BedDao;
import cn.edu.sxau.dormitorymanage.dao.DormitoryDao;
import cn.edu.sxau.dormitorymanage.dao.StudentDao;
import cn.edu.sxau.dormitorymanage.model.Bed;
import cn.edu.sxau.dormitorymanage.model.Dormitory;
import cn.edu.sxau.dormitorymanage.model.Student;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml", "classpath:spring-hibernate.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback=false)
public class TestDao {

	@Autowired
	private StudentDao studentDao;
	@Autowired
	private DormitoryDao dormitoryDao;
	@Autowired
	private BedDao bedDao;

	@Test
	@Transactional
	public void test() {
		List<Student> s = studentDao.find("from Student s ", 1, 10);
		System.out.println(JSON.toJSONString(s));
	}

	@Test
	@Transactional
	public void updateCS() {
		List<Bed> beds = bedDao.find("from Bed b order by id", 5, 5000);
		for (Bed bed : beds) {
			String name = bed.getName();
			String s = name.trim().substring(0, 6);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("number", s + "%");
			String hql = "from Dormitory d where d.number like:number";
			Dormitory dr = dormitoryDao.getByHql(hql, params);
			Dormitory d = new Dormitory();
			if (dr != null && dr.getId() != null) {
				d.setId(dr.getId());
				bed.setDormitory(d);
			}
			bedDao.update(bed);
		}
	}
}
