package cn.edu.sxau.dormitorymanage.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;


@SuppressWarnings("rawtypes")
public class MemcachedSerch {

	public static MemcachedClient memcache = null;

	private static String ipAddress;
	private static int port;

	static {
		ipAddress = PropertiesUtils.getProValue("IP_ADDRESS");
		port = new Integer(PropertiesUtils.getProValue("PORT"));
		try {
			if (memcache == null) {
				memcache = new MemcachedClient(new InetSocketAddress[] { new InetSocketAddress(ipAddress, port) });
			}
			// memcache = new MemcachedClient(new InetSocketAddress[] { new InetSocketAddress("192.168.0.21", 11211) });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List getMemcacheKey(String key) {
		List list = new ArrayList();
		if (memcache != null) {
			list = (List) memcache.get(key);
		}
		return list;
	}

	public static void delete(String key) {
		memcache.delete(key);
	}

	public static void getfflush() {
		memcache.flush();

	}

	public static List getMemcacheKey(String key, String version) {
		List list = new ArrayList();
		if (memcache != null) {
			list = (List) memcache.get(key);
		}
		return list;
	}

	public static void setMapObj(String key, Map map, int time) {
		memcache.set(key, time, map);
	}

	public static Map getMapObj(String key) {
		Map map = new HashMap();
		if (memcache != null) {
			map = (Map) memcache.get(key);
		}
		return map;
	}

	public static void setDriverScore(String key, String value, int time) {
		memcache.set(key, time, value);
	}

	public static Future<Boolean> setDriverScoreWithFuture(String key, String value, int time) {
		return memcache.set(key, time, value);
	}

	public static String getDriverScore(String key) {
		String st = "";
		if (memcache != null) {
			st = (String) memcache.get(key);
		}
		return st;
	}

	/**
	 * 删除指定key
	 * 
	 * @param key
	 */
	public static void DelDriverScore(String key) {
		if (memcache != null) {
			memcache.delete(key);
		}
	}

	/**
	 * 增加缓存设置方法
	 * 
	 * @param key
	 *            存储key searckey+"."+业务名称 java 包格式
	 * @param obj
	 *            可以序列化的任意java对象
	 * @param time
	 *            缓存时间 以秒为单位
	 */
	public static void setMemObj(String key, Object obj, int time) {
		memcache.set(key, time, obj);
	}

	/**
	 * 增加缓存取数据方法
	 * 
	 * @param key
	 *            存储key
	 * @return 返回Object，类型为存入的类型 缓存为空则返回null
	 */
	public static Object getMemObj(String key) {
		Object obj = null;
		if (memcache != null) {
			obj = memcache.get(key);
		}
		return obj;
	}

	public static void setMemcacheKey(String key, List list, int timelimit) {
		memcache.set(key, timelimit, list);
	}

	public static void setMemcacheKey(String key, Object obj, int timelimit) {
		memcache.set(key, timelimit, obj);
	}

}
