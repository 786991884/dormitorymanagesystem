package cn.edu.sxau.dormitorymanage.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sxau.dormitorymanage.dao.BaseDao;
import cn.edu.sxau.dormitorymanage.service.BaseService;

@Service
public class BaseServiceImpl<T> implements BaseService<T> {
	@Autowired
	private BaseDao<T> baseDao;

	@Override
	public Serializable save(T o) {
		return baseDao.save(o);
	}

	@Override
	public void delete(T o) {
		baseDao.delete(o);
	}

	@Override
	public void update(T o) {
		baseDao.update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		baseDao.saveOrUpdate(o);
	}

	@Override
	public T getById(Serializable id) {
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return baseDao.getById(c, id);
	}

	@Override
	public T getByHql(String hql) {
		return baseDao.getByHql(hql);
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		return baseDao.getByHql(hql, params);
	}

	@Override
	public List<T> find(String hql) {
		return baseDao.find(hql);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		return baseDao.find(hql, params);
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		return baseDao.find(hql, page, rows);
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		return baseDao.find(hql, params, page, rows);
	}

	@Override
	public Integer count(String hql) {
		return baseDao.count(hql);
	}

	@Override
	public Integer count(String hql, Map<String, Object> params) {
		return baseDao.count(hql, params);
	}

	@Override
	public Integer executeHql(String hql) {
		return baseDao.executeHql(hql);
	}

	@Override
	public Integer executeHql(String hql, Map<String, Object> params) {
		return baseDao.executeHql(hql, params);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sql) {
		return baseDao.findBySql(sql);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sql, int page, int rows) {
		return baseDao.findBySql(sql, page, rows);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sql, Map<String, Object> params) {
		return baseDao.findBySql(sql, params);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findBySql(String sql, Map<String, Object> params, int page, int rows) {
		return baseDao.findBySql(sql, params, page, rows);
	}

	@Override
	public Integer executeSql(String sql) {
		return baseDao.executeSql(sql);
	}

	@Override
	public Integer executeSql(String sql, Map<String, Object> params) {
		return baseDao.executeSql(sql, params);
	}

	@Override
	public Integer countBySql(String sql) {
		return baseDao.countBySql(sql);
	}

	@Override
	public Integer countBySql(String sql, Map<String, Object> params) {
		return baseDao.countBySql(sql, params);
	}

}
