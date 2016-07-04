package cn.edu.sxau.dormitorymanage.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * String工具类.
 */
public class StringUtil {
	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private StringUtil() {
	}

	private static final int INDEX_NOT_FOUND = -1;
	private static final String EMPTY = "";
	private static final int PAD_LIMIT = 8192;
	private static final char SEPARATOR = '_';
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 功能：将半角的符号转换成全角符号.(即英文字符转中文字符)
	 * 
	 * @param str
	 *            源字符串
	 * @return String
	 */
	public static String changeToFull(String str) {
		String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
		String[] decode = { "１", "２", "３", "４", "５", "６", "７", "８", "９", "０", "！", "＠", "＃", "＄", "％", "︿", "＆", "＊", "（", "）", "ａ", "ｂ", "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ", "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；", "：", "'",
				"\"", "，", "〈", "。", "〉", "／", "？" };
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int pos = source.indexOf(str.charAt(i));
			if (pos != -1) {
				result += decode[pos];
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 功能：cs串中是否一个都不包含字符数组searchChars中的字符。
	 * 
	 * @param cs
	 *            字符串
	 * @param searchChars
	 *            字符数组
	 * @return boolean 都不包含返回true，否则返回false。
	 */
	public static boolean containsNone(CharSequence cs, char... searchChars) {
		if (cs == null || searchChars == null) {
			return true;
		}
		int csLen = cs.length();
		int csLast = csLen - 1;
		int searchLen = searchChars.length;
		int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like
							// String.indexOf(String)
							return false;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return false;
						}
					} else {
						// ch is in the Basic Multilingual Plane
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 编码为Unicode，格式 '\u0020'.
	 * 
	 * 
	 * <pre>
	 *   CharUtils.unicodeEscaped(' ') = "\u0020"
	 *   CharUtils.unicodeEscaped('A') = "\u0041"
	 * </pre>
	 * 
	 * @param ch
	 *            源字符串
	 * @return 转码后的字符串
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}

	/**
	 * 进行tostring操作，如果传入的是null，返回空字符串。
	 * 
	 * <pre>
	 * ObjectUtils.toString(null)         = ""
	 * ObjectUtils.toString("")           = ""
	 * ObjectUtils.toString("bat")        = "bat"
	 * ObjectUtils.toString(Boolean.TRUE) = "true"
	 * </pre>
	 * 
	 * @param obj
	 *            源
	 * @return String
	 */
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 进行tostring操作，如果传入的是null，返回指定的默认值。
	 * 
	 * <pre>
	 * ObjectUtils.toString(null, null)           = null
	 * ObjectUtils.toString(null, "null")         = "null"
	 * ObjectUtils.toString("", "null")           = ""
	 * ObjectUtils.toString("bat", "null")        = "bat"
	 * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
	 * </pre>
	 * 
	 * @param obj
	 *            源
	 * @param nullStr
	 *            如果obj为null时返回这个指定值
	 * @return String
	 */
	public static String toString(Object obj, String nullStr) {
		return obj == null ? nullStr : obj.toString();
	}

	/**
	 * 只从源字符串中移除指定开头子字符串.
	 * 
	 * <pre>
	 * StringUtil.removeStart(null, *)      = null
	 * StringUtil.removeStart("", *)        = ""
	 * StringUtil.removeStart(*, null)      = *
	 * StringUtil.removeStart("www.domain.com", "www.")   = "domain.com"
	 * StringUtil.removeStart("domain.com", "www.")       = "domain.com"
	 * StringUtil.removeStart("www.domain.com", "domain") = "www.domain.com"
	 * StringUtil.removeStart("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param remove
	 *            将要被移除的子字符串
	 * @return String
	 */
	public static String removeStart(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * 只从源字符串中移除指定结尾的子字符串.
	 * 
	 * <pre>
	 * StringUtil.removeEnd(null, *)      = null
	 * StringUtil.removeEnd("", *)        = ""
	 * StringUtil.removeEnd(*, null)      = *
	 * StringUtil.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
	 * StringUtil.removeEnd("www.domain.com", ".com")   = "www.domain"
	 * StringUtil.removeEnd("www.domain.com", "domain") = "www.domain.com"
	 * StringUtil.removeEnd("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param remove
	 *            将要被移除的子字符串
	 * @return String
	 */
	public static String removeEnd(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * 将一个字符串重复N次
	 * 
	 * <pre>
	 * StringUtil.repeat(null, 2) = null
	 * StringUtil.repeat("", 0)   = ""
	 * StringUtil.repeat("", 2)   = ""
	 * StringUtil.repeat("a", 3)  = "aaa"
	 * StringUtil.repeat("ab", 2) = "abab"
	 * StringUtil.repeat("a", -2) = ""
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param repeat
	 *            重复的次数
	 * @return String
	 */
	public static String repeat(String str, int repeat) {
		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return EMPTY;
		}
		int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str;
		}
		if (inputLength == 1 && repeat <= PAD_LIMIT) {
			return repeat(str.charAt(0), repeat);
		}

		int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			return repeat(str.charAt(0), repeat);
		case 2:
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			StringBuilder buf = new StringBuilder(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	/**
	 * 将一个字符串重复N次，并且中间加上指定的分隔符
	 * 
	 * <pre>
	 * StringUtil.repeat(null, null, 2) = null
	 * StringUtil.repeat(null, "x", 2)  = null
	 * StringUtil.repeat("", null, 0)   = ""
	 * StringUtil.repeat("", "", 2)     = ""
	 * StringUtil.repeat("", "x", 3)    = "xxx"
	 * StringUtil.repeat("?", ", ", 3)  = "?, ?, ?"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param separator
	 *            分隔符
	 * @param repeat
	 *            重复次数
	 * @return String
	 */
	public static String repeat(String str, String separator, int repeat) {
		if (str == null || separator == null) {
			return repeat(str, repeat);
		} else {
			// given that repeat(String, int) is quite optimized, better to rely
			// on it than try and splice this into it
			String result = repeat(str + separator, repeat);
			return removeEnd(result, separator);
		}
	}

	/**
	 * 将某个字符重复N次.
	 * 
	 * @param ch
	 *            某个字符
	 * @param repeat
	 *            重复次数
	 * @return String
	 */
	public static String repeat(char ch, int repeat) {
		char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * 字符串长度达不到指定长度时，在字符串右边补指定的字符.
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *, *)     = null
	 * StringUtil.rightPad("", 3, 'z')     = "zzz"
	 * StringUtil.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtil.rightPad("bat", 5, 'z')  = "batzz"
	 * StringUtil.rightPad("bat", 1, 'z')  = "bat"
	 * StringUtil.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            指定的长度
	 * @param padChar
	 *            进行补充的字符
	 * @return String
	 */
	public static String rightPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(repeat(padChar, pads));
	}

	/**
	 * 扩大字符串长度，从左边补充指定字符
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *, *)      = null
	 * StringUtil.rightPad("", 3, "z")      = "zzz"
	 * StringUtil.rightPad("bat", 3, "yz")  = "bat"
	 * StringUtil.rightPad("bat", 5, "yz")  = "batyz"
	 * StringUtil.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * StringUtil.rightPad("bat", 1, "yz")  = "bat"
	 * StringUtil.rightPad("bat", -1, "yz") = "bat"
	 * StringUtil.rightPad("bat", 5, null)  = "bat  "
	 * StringUtil.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            扩大后的长度
	 * @param padStr
	 *            在右边补充的字符串
	 * @return String
	 */
	public static String rightPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * 扩大字符串长度，从左边补充空格
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *)   = null
	 * StringUtil.leftPad("", 3)     = "   "
	 * StringUtil.leftPad("bat", 3)  = "bat"
	 * StringUtil.leftPad("bat", 5)  = "  bat"
	 * StringUtil.leftPad("bat", 1)  = "bat"
	 * StringUtil.leftPad("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            扩大后的长度
	 * @return String
	 */
	public static String leftPad(String str, int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * 扩大字符串长度，从左边补充指定的字符
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *, *)     = null
	 * StringUtil.leftPad("", 3, 'z')     = "zzz"
	 * StringUtil.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtil.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtil.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtil.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            扩大后的长度
	 * @param padStr
	 *            补充的字符
	 * @return String
	 */
	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str);
	}

	/**
	 * 扩大字符串长度，从左边补充指定的字符
	 * 
	 * <pre>
	 * StringUtil.leftPad(null, *, *)      = null
	 * StringUtil.leftPad("", 3, "z")      = "zzz"
	 * StringUtil.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtil.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtil.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtil.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtil.leftPad("bat", -1, "yz") = "bat"
	 * StringUtil.leftPad("bat", 5, null)  = "  bat"
	 * StringUtil.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            扩大后的长度
	 * @param padStr
	 *            补充的字符串
	 * @return String
	 */
	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * 扩大字符串长度并将现在的字符串居中，被扩大部分用空格填充。
	 * 
	 * <pre>
	 * StringUtil.center(null, *)   = null
	 * StringUtil.center("", 4)     = "    "
	 * StringUtil.center("ab", -1)  = "ab"
	 * StringUtil.center("ab", 4)   = " ab "
	 * StringUtil.center("abcd", 2) = "abcd"
	 * StringUtil.center("a", 4)    = " a  "
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            扩大后的长度
	 * @return String
	 */
	public static String center(String str, int size) {
		return center(str, size, ' ');
	}

	/**
	 * 将字符串长度修改为指定长度，并进行居中显示。
	 * 
	 * <pre>
	 * StringUtil.center(null, *, *)     = null
	 * StringUtil.center("", 4, ' ')     = "    "
	 * StringUtil.center("ab", -1, ' ')  = "ab"
	 * StringUtil.center("ab", 4, ' ')   = " ab"
	 * StringUtil.center("abcd", 2, ' ') = "abcd"
	 * StringUtil.center("a", 4, ' ')    = " a  "
	 * StringUtil.center("a", 4, 'y')    = "yayy"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            指定的长度
	 * @param padStr
	 *            长度不够时补充的字符串
	 * @return String
	 * @throws IllegalArgumentException
	 *             如果被补充字符串为 null或者 empty
	 */
	public static String center(String str, int size, char padChar) {
		if (str == null || size <= 0) {
			return str;
		}
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padChar);
		str = rightPad(str, size, padChar);
		return str;
	}

	/**
	 * 将字符串长度修改为指定长度，并进行居中显示。
	 * 
	 * <pre>
	 * StringUtil.center(null, *, *)     = null
	 * StringUtil.center("", 4, " ")     = "    "
	 * StringUtil.center("ab", -1, " ")  = "ab"
	 * StringUtil.center("ab", 4, " ")   = " ab"
	 * StringUtil.center("abcd", 2, " ") = "abcd"
	 * StringUtil.center("a", 4, " ")    = " a  "
	 * StringUtil.center("a", 4, "yz")   = "yayz"
	 * StringUtil.center("abc", 7, null) = "  abc  "
	 * StringUtil.center("abc", 7, "")   = "  abc  "
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            指定的长度
	 * @param padStr
	 *            长度不够时补充的字符串
	 * @return String
	 * @throws IllegalArgumentException
	 *             如果被补充字符串为 null或者 empty
	 */
	public static String center(String str, int size, String padStr) {
		if (str == null || size <= 0) {
			return str;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		str = leftPad(str, strLen + pads / 2, padStr);
		str = rightPad(str, size, padStr);
		return str;
	}

	/**
	 * 检查字符串是否全部为小写.
	 * 
	 * <pre>
	 * StringUtil.isAllLowerCase(null)   = false
	 * StringUtil.isAllLowerCase("")     = false
	 * StringUtil.isAllLowerCase("  ")   = false
	 * StringUtil.isAllLowerCase("abc")  = true
	 * StringUtil.isAllLowerCase("abC") = false
	 * </pre>
	 * 
	 * @param cs
	 *            源字符串
	 * @return String
	 */
	public static boolean isAllLowerCase(String cs) {
		if (cs == null || isEmpty(cs)) {
			return false;
		}
		int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLowerCase(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查是否都是大写.
	 * 
	 * <pre>
	 * StringUtil.isAllUpperCase(null)   = false
	 * StringUtil.isAllUpperCase("")     = false
	 * StringUtil.isAllUpperCase("  ")   = false
	 * StringUtil.isAllUpperCase("ABC")  = true
	 * StringUtil.isAllUpperCase("aBC") = false
	 * </pre>
	 * 
	 * @param cs
	 *            源字符串
	 * @return String
	 */
	public static boolean isAllUpperCase(String cs) {
		if (cs == null || StringUtil.isEmpty(cs)) {
			return false;
		}
		int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isUpperCase(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 反转字符串.
	 * 
	 * <pre>
	 * StringUtil.reverse(null)  = null
	 * StringUtil.reverse("")    = ""
	 * StringUtil.reverse("bat") = "tab"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @return String
	 */
	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * 字符串达不到一定长度时在右边补空白.
	 * 
	 * <pre>
	 * StringUtil.rightPad(null, *)   = null
	 * StringUtil.rightPad("", 3)     = "   "
	 * StringUtil.rightPad("bat", 3)  = "bat"
	 * StringUtil.rightPad("bat", 5)  = "bat  "
	 * StringUtil.rightPad("bat", 1)  = "bat"
	 * StringUtil.rightPad("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param size
	 *            指定的长度
	 * @return String
	 */
	public static String rightPad(String str, int size) {
		return rightPad(str, size, ' ');
	}

	/**
	 * 从右边截取字符串.</p>
	 * 
	 * <pre>
	 * StringUtil.right(null, *)    = null
	 * StringUtil.right(*, -ve)     = ""
	 * StringUtil.right("", *)      = ""
	 * StringUtil.right("abc", 0)   = ""
	 * StringUtil.right("abc", 2)   = "bc"
	 * StringUtil.right("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param len
	 *            长度
	 * @return String
	 */
	public static String right(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(str.length() - len);
	}

	/**
	 * 截取一个字符串的前几个.
	 * 
	 * <pre>
	 * StringUtil.left(null, *)    = null
	 * StringUtil.left(*, -ve)     = ""
	 * StringUtil.left("", *)      = ""
	 * StringUtil.left("abc", 0)   = ""
	 * StringUtil.left("abc", 2)   = "ab"
	 * StringUtil.left("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param len
	 *            截取的长度
	 * @return the String
	 */
	public static String left(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 得到tag字符串中间的子字符串，只返回第一个匹配项。
	 * 
	 * <pre>
	 * StringUtil.substringBetween(null, *)            = null
	 * StringUtil.substringBetween("", "")             = ""
	 * StringUtil.substringBetween("", "tag")          = null
	 * StringUtil.substringBetween("tagabctag", null)  = null
	 * StringUtil.substringBetween("tagabctag", "")    = ""
	 * StringUtil.substringBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串。
	 * @param tag
	 *            标识字符串。
	 * @return String 子字符串, 如果没有符合要求的，返回{@code null}。
	 */
	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag);
	}

	/**
	 * 得到两个字符串中间的子字符串，只返回第一个匹配项。
	 * 
	 * <pre>
	 * StringUtil.substringBetween("wx[b]yz", "[", "]") = "b"
	 * StringUtil.substringBetween(null, *, *)          = null
	 * StringUtil.substringBetween(*, null, *)          = null
	 * StringUtil.substringBetween(*, *, null)          = null
	 * StringUtil.substringBetween("", "", "")          = ""
	 * StringUtil.substringBetween("", "", "]")         = null
	 * StringUtil.substringBetween("", "[", "]")        = null
	 * StringUtil.substringBetween("yabcz", "", "")     = ""
	 * StringUtil.substringBetween("yabcz", "y", "z")   = "abc"
	 * StringUtil.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param open
	 *            起字符串。
	 * @param close
	 *            末字符串。
	 * @return String 子字符串, 如果没有符合要求的，返回{@code null}。
	 */
	public static String substringBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			int end = str.indexOf(close, start + open.length());
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * 得到两个字符串中间的子字符串，所有匹配项组合为数组并返回。
	 * 
	 * <pre>
	 * StringUtil.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
	 * StringUtil.substringsBetween(null, *, *)            = null
	 * StringUtil.substringsBetween(*, null, *)            = null
	 * StringUtil.substringsBetween(*, *, null)            = null
	 * StringUtil.substringsBetween("", "[", "]")          = []
	 * </pre>
	 * 
	 * @param str
	 *            源字符串
	 * @param open
	 *            起字符串。
	 * @param close
	 *            末字符串。
	 * @return String 子字符串数组, 如果没有符合要求的，返回{@code null}。
	 */
	public static String[] substringsBetween(String str, String open, String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		int strLen = str.length();
		if (strLen == 0) {
			return new String[0];
		}
		int closeLen = close.length();
		int openLen = open.length();
		List<String> list = new ArrayList<String>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 功能：切换字符串中的所有字母大小写。<br/>
	 * 
	 * <pre>
	 * StringUtil.swapCase(null)                 = null
	 * StringUtil.swapCase("")                   = ""
	 * StringUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 * 
	 * 
	 * @param str
	 *            源字符串
	 * @return String
	 */
	public static String swapCase(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		char[] buffer = str.toCharArray();

		boolean whitespace = true;

		for (int i = 0; i < buffer.length; i++) {
			char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
				whitespace = false;
			} else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
				whitespace = false;
			} else if (Character.isLowerCase(ch)) {
				if (whitespace) {
					buffer[i] = Character.toTitleCase(ch);
					whitespace = false;
				} else {
					buffer[i] = Character.toUpperCase(ch);
				}
			} else {
				whitespace = Character.isWhitespace(ch);
			}
		}
		return new String(buffer);
	}

	/**
	 * 功能：截取出最后一个标志位之后的字符串.<br/>
	 * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
	 * 如果expr长度为0，直接返回sourceStr。<br/>
	 * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
	 * 
	 * @param sourceStr
	 *            被截取的字符串
	 * @param expr
	 *            分隔符
	 * @return String
	 */
	public static String substringAfterLast(String sourceStr, String expr) {
		if (isEmpty(sourceStr) || expr == null) {
			return sourceStr;
		}
		if (expr.length() == 0) {
			return sourceStr;
		}
		int pos = sourceStr.lastIndexOf(expr);
		if (pos == -1) {
			return sourceStr;
		}
		return sourceStr.substring(pos + expr.length());
	}

	/**
	 * 功能：截取出最后一个标志位之前的字符串.<br/>
	 * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
	 * 如果expr长度为0，直接返回sourceStr。<br/>
	 * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
	 * 
	 * @param sourceStr
	 *            被截取的字符串
	 * @param expr
	 *            分隔符
	 * @return String
	 */
	public static String substringBeforeLast(String sourceStr, String expr) {
		if (isEmpty(sourceStr) || expr == null) {
			return sourceStr;
		}
		if (expr.length() == 0) {
			return sourceStr;
		}
		int pos = sourceStr.lastIndexOf(expr);
		if (pos == -1) {
			return sourceStr;
		}
		return sourceStr.substring(0, pos);
	}

	/**
	 * 功能：截取出第一个标志位之后的字符串.<br/>
	 * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
	 * 如果expr长度为0，直接返回sourceStr。<br/>
	 * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
	 * 
	 * @param sourceStr
	 *            被截取的字符串
	 * @param expr
	 *            分隔符
	 * @return String
	 */
	public static String substringAfter(String sourceStr, String expr) {
		if (isEmpty(sourceStr) || expr == null) {
			return sourceStr;
		}
		if (expr.length() == 0) {
			return sourceStr;
		}

		int pos = sourceStr.indexOf(expr);
		if (pos == -1) {
			return sourceStr;
		}
		return sourceStr.substring(pos + expr.length());
	}

	/**
	 * 功能：截取出第一个标志位之前的字符串.<br/>
	 * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
	 * 如果expr长度为0，直接返回sourceStr。<br/>
	 * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
	 * 如果expr在sourceStr中存在不止一个，以第一个位置为准。
	 * 
	 * @param sourceStr
	 *            被截取的字符串
	 * @param expr
	 *            分隔符
	 * @return String
	 */
	public static String substringBefore(String sourceStr, String expr) {
		if (isEmpty(sourceStr) || expr == null) {
			return sourceStr;
		}
		if (expr.length() == 0) {
			return sourceStr;
		}
		int pos = sourceStr.indexOf(expr);
		if (pos == -1) {
			return sourceStr;
		}
		return sourceStr.substring(0, pos);
	}

	/**
	 * 功能：检查这个字符串是不是空字符串。<br/>
	 * 如果这个字符串为null或者trim后为空字符串则返回true，否则返回false。
	 * 
	 * @param chkStr
	 *            被检查的字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String chkStr) {
		if (chkStr == null) {
			return true;
		} else {
			return "".equals(chkStr.trim()) ? true : false;
		}
	}

	/**
	 * 如果字符串没有超过最长显示长度返回原字符串，否则从开头截取指定长度并加...返回。
	 * 
	 * @param str
	 *            原字符串
	 * @param length
	 *            字符串最长显示的长度
	 * @return 转换后的字符串
	 */
	public static String trimString(String str, int length) {
		if (str == null) {
			return "";
		} else if (str.length() > length) {
			return str.substring(0, length - 3) + "...";
		} else {
			return str;
		}
	}

	/**
	 * 替换字符串
	 * 
	 * @param from
	 *            String 原始字符串
	 * @param to
	 *            String 目标字符串
	 * @param source
	 *            String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}

	/**
	 * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
	 * 
	 * @param str
	 *            String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlencode(String str) {
		if (str == null) {
			return null;
		}
		return replace("\"", "&quot;", replace("<", "&lt;", str));
	}

	/**
	 * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String htmldecode(String str) {
		if (str == null) {
			return null;
		}
		return replace("&quot;", "\"", replace("&lt;", "<", str));
	}

	private static final String _BR = "<br/>";

	/**
	 * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
	 * 
	 * @param str
	 *            String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlshow(String str) {
		if (str == null) {
			return null;
		}
		str = replace("<", "&lt;", str);
		str = replace(" ", "&nbsp;", str);
		str = replace("\r\n", _BR, str);
		str = replace("\n", _BR, str);
		str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
		return str;
	}

	/**
	 * 返回指定字节长度的字符串
	 * 
	 * @param str
	 *            String 字符串
	 * @param length
	 *            int 指定长度
	 * @return String 返回的字符串
	 */
	public static String toLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		try {
			if (str.getBytes("GBK").length <= length) {
				return str;
			}
		} catch (Exception ex) {
		}
		StringBuffer buff = new StringBuffer();

		int index = 0;
		char c;
		length -= 3;
		while (length > 0) {
			c = str.charAt(index);
			if (c < 128) {
				length--;
			} else {
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	/**
	 * 判断是不是合法字符 c 要判断的字符
	 */
	public static boolean isLetter(String c) {
		boolean result = false;

		if (c == null || c.length() < 0) {
			return false;
		}
		// a-z
		if (c.compareToIgnoreCase("a") >= 0 && c.compareToIgnoreCase("z") <= 0) {
			return true;
		}
		// 0-9
		if (c.compareToIgnoreCase("0") >= 0 && c.compareToIgnoreCase("9") <= 0) {
			return true;
		}
		// . - _
		if (c.equals(".") || c.equals("-") || c.equals("_")) {
			return true;
		}
		return result;
	}

	/**
	 * 
	 * @param source
	 *            源字符
	 * @param charset
	 *            源字符编码
	 * @return
	 */
	public static String decodeUTF8(String source, String charset) {
		try {
			return new String(source.getBytes(charset), "UTF-8");
		} catch (Exception E) {
		}
		return source;
	}

	/**
	 * 人民币转成大写
	 * 
	 * @param value
	 * @return String
	 */
	public static String hangeToBig(double value) {
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串

		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // 转化该数字表示
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
			}
		}

		if (prefix.length() > 0)
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}

	// 过滤特殊字符
	public static String encoding(String src) {
		if (src == null)
			return "";
		StringBuilder result = new StringBuilder();
		if (src != null) {
			src = src.trim();
			for (int pos = 0; pos < src.length(); pos++) {
				switch (src.charAt(pos)) {
				case '\"':
					result.append("&quot;");
					break;
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '%':
					result.append("&pc;");
					break;
				case '_':
					result.append("&ul;");
					break;
				case '#':
					result.append("&shap;");
					break;
				case '?':
					result.append("&ques;");
					break;
				default:
					result.append(src.charAt(pos));
					break;
				}
			}
		}
		return result.toString();
	}

	// 反过滤特殊字符
	public static String decoding(String src) {
		if (src == null)
			return "";
		String result = src;
		result = result.replace("&quot;", "\"").replace("&apos;", "\'");
		result = result.replace("&lt;", "<").replace("&gt;", ">");
		result = result.replace("&amp;", "&");
		result = result.replace("&pc;", "%").replace("&ul", "_");
		result = result.replace("&shap;", "#").replace("&ques", "?");
		return result;
	}

	/**
	 * 转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] getBytes(String str) {
		if (str != null) {
			try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param str
	 *            验证字符串
	 * @param strs
	 *            字符串组
	 * @return 包含返回true
	 */
	public static boolean inString(String str, String... strs) {
		if (str != null) {
			for (String s : strs) {
				if (str.equals(StringUtils.trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (StringUtils.isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html) {
		if (html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt) {
		if (txt == null) {
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * 
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>", "");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(StringUtils.trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 如果不为空，则设置值
	 * 
	 * @param target
	 * @param source
	 */
	public static void setValueIfNotBlank(String target, String source) {
		if (StringUtils.isNotBlank(source)) {
			target = source;
		}
	}

	/**
	 * 转换为JS获取对象值，生成三目运算返回结果
	 * 
	 * @param objectString
	 *            对象串 例如：row.user.id 返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
	 */
	public static String jsGetVal(String objectString) {
		StringBuilder result = new StringBuilder();
		StringBuilder val = new StringBuilder();
		String[] vals = StringUtils.split(objectString, ".");
		for (int i = 0; i < vals.length; i++) {
			val.append("." + vals[i]);
			result.append("!" + (val.substring(1)) + "?'':");
		}
		result.append(val.substring(1));
		return result.toString();
	}

}