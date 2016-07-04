package cn.edu.sxau.dormitorymanage.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 集合(List,Map,Set)辅助类。
 * 
 * @author 徐冰浩
 */
@SuppressWarnings("rawtypes")
public class CollectionUtil {
	private static final Logger logger = Logger.getLogger(CollectionUtil.class);

	/**
	 * 功能：从List中随机取出一个元素。
	 * 
	 * @param objs
	 *            源List
	 * @return T List的一个元素
	 */
	public static <T> T randomOne(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}
		return list.get(MathUtil.randomNumber(0, list.size()));
	}

	/**
	 * 功能：从数组中随机取出一个元素。
	 * 
	 * @param objs
	 *            源数组
	 * @return T 数组的一个元素
	 */
	public static <T> T randomOne(T[] objs) {
		if (isEmpty(objs)) {
			return null;
		}
		return objs[MathUtil.randomNumber(0, objs.length)];
	}

	/**
	 * 功能：数组中是否存在这个元素。
	 * 
	 * @param objArr
	 *            数组
	 * @param compare
	 *            元素
	 * @return 存在返回true，否则返回false。
	 */
	public static <T> boolean arrayContain(T[] objArr, T compare) {
		if (isEmpty(objArr)) {
			return false;
		}
		for (T obj : objArr) {
			if (obj.equals(compare)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 功能：向list中添加数组。
	 * 
	 * @param list
	 *            List
	 * @param array
	 *            数组
	 */
	public static <T> void addArrayToList(List<T> list, T[] array) {
		if (isEmpty(list)) {
			return;
		}
		for (T t : array) {
			list.add(t);
		}
	}

	/**
	 * 功能：将数组进行反转，倒置。
	 * 
	 * @param objs
	 *            源数组
	 * @return T[] 反转后的数组
	 */
	public static <T> T[] reverseArray(T[] objs) {
		if (isEmpty(objs)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T[] res = (T[]) Array.newInstance(objs[0].getClass(), objs.length);
		// 新序号
		int k = 0;
		for (int i = objs.length - 1; i >= 0; i--) {
			res[k++] = objs[i];
		}
		return res;
	}

	/**
	 * 功能：将数组转为list。
	 * 
	 * @param objs
	 *            源数组
	 * @return List
	 */
	public static <T> List<T> arrayToList(T[] objs) {
		if (isEmpty(objs)) {
			return null;
		}
		List<T> list = new LinkedList<T>();
		for (T obj : objs) {
			list.add(obj);
		}
		return list;
	}

	/**
	 * 功能：将list转为数组。
	 * 
	 * @param list
	 *            源list
	 * @return T[]
	 */
	public static <T> T[] listToArray(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}
		@SuppressWarnings("unchecked")
		T[] objs = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
		int i = 0; // 数组下标。
		for (T obj : list) {
			objs[i++] = obj;
		}
		return objs;
	}

	/**
	 * 将一个字符串数组的内容全部添加到另外一个数组中，并返回一个新数组。
	 * 
	 * @param array1
	 *            第一个数组
	 * @param array2
	 *            第二个数组
	 * @return T[] 拼接后的新数组
	 */
	public static <T> T[] concatenateArrays(T[] array1, T[] array2) {
		if (isEmpty(array1)) {
			return array2;
		}
		if (isEmpty(array2)) {
			return array1;
		}
		@SuppressWarnings("unchecked")
		T[] resArray = (T[]) Array.newInstance(array1[0].getClass(), array1.length + array2.length);
		System.arraycopy(array1, 0, resArray, 0, array1.length);
		System.arraycopy(array2, 0, resArray, array1.length, array2.length);
		return resArray;
	}

	/**
	 * 将一个object添加到一个数组中，并返回一个新数组。
	 * 
	 * @param array被添加到的数组
	 * @param object
	 *            被添加的object
	 * @return T[] 返回的新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addObjectToArray(T[] array, T obj) {
		// 结果数组
		T[] resArray = null;
		if (isEmpty(array)) {
			resArray = (T[]) Array.newInstance(obj.getClass(), 1);
			resArray[0] = obj;
			return resArray;
		}
		// 原数组不为空时。
		resArray = (T[]) Array.newInstance(array[0].getClass(), array.length + 1);
		System.arraycopy(array, 0, resArray, 0, array.length);
		resArray[array.length] = obj;
		return resArray;
	}

	/**
	 * 功能：判断数组是不是空。（null或者length==0）
	 * 
	 * @param array
	 *            数组
	 * @return boolean 空返回true，否则返回false。
	 */
	public static <T> boolean isEmpty(T[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * 功能：集合是否为空。如果传入的值为null或者集合不包含元素都认为为空。
	 * 
	 * @param collection
	 *            集合
	 * @return boolean 为空返回true，否则返回false。
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * 功能：Map是否为空。如果传入的值为null或者集合不包含元素都认为为空。
	 * 
	 * @param map
	 *            Map
	 * @return boolean 为空返回true，否则返回false。
	 */
	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param keyPropertyName
	 *            要提取为Map中的Key值的属性名.
	 * @param valuePropertyName
	 *            要提取为Map中的Value值的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static Map extractToMap(final Collection collection, final String keyPropertyName, final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName), PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素 ，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		// 当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		// 其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}

		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();

		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
	/**
	 * 将集合中的内容写入CSV文件
	 * @param collection 要写入文件的集合集合
	 * @param pathWithName CSV文件路径，带文件名
	 * @param charset 字符集
	 */
	public static void toCSV(Collection<String> collection, String pathWithName, String charset){
		try {
			PrintWriter writer = FileUtil.getPrintWriter(pathWithName, charset, false);
			for (String line : collection) {
				logger.debug("写入：" + line);
				writer.println(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}