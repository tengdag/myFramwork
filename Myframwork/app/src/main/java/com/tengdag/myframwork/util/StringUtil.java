package com.tengdag.myframwork.util;

import android.text.TextUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author cjx
 *
 */
public class StringUtil {
	/**
	 * 是否数字
	 * @param text
	 * @return
	 */
	public static boolean isNumeric(String text) {
		if(TextUtils.isEmpty(text)) {
			return false;
		} else if(text.matches("^[0-9]+$")) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 是否小数(包含整数)
	 * @param text
	 * @return
	 */
	public static boolean isDecimal(String text) {
		if(TextUtils.isEmpty(text)) {
			return false;
		} else if(text.matches("^[0-9]+(\\.[0-9]+){0,1}$")) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 日志组装
	 * @param tip
	 * @param fields
	 * @return
	 */
	public static String generationLog(String tip, Map<String, String> fields) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n" + tip + ":\n");
		Set<String> keySet = fields.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			String value = fields.get(key);
			sb.append("   [" + key + "]" + value + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		String NUM = "+86";
		boolean flag = false;
		if (TextUtils.isEmpty(str)) {
			return flag;
		} else {
			if (str.indexOf(NUM) > -1) {
				str = str.substring(NUM.length(), str.length());
			}
			if (str.charAt(0) == '0') {
				str = str.substring(1, str.length());
			}
			String rex = "^1[3,5,8]\\d{9}$";
			str = removeBlanks(str);
			if (str.matches(rex)) {
				flag = true;
			}
			return flag;
		}
	}

	/**
	 * 是否是固话+手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTelephoneNumber(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		if (!str.matches("^\\d{3,}$")) {
			return false;
		}
		if (str.matches("^1[358]\\d*")) {
			return str.length() == 11;
		}
		return str.charAt(0) == '0' && str.length() >= 10 && str.length() <= 12;
	}
	
	/**
	 * 是否联通号码
	 * @param num
	 * @return
	 */
	public static boolean isUnicomMobile(String num) {
		if(!isMobile(num)) {
			return false;
		}
		String phone = num.substring(num.length() - 11, num.length());
		String regex = "^(130|131|132|155|156|186|185)[0-9]{8}$";
		if(phone.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否移动号码
	 * @param num
	 * @return
	 */
	public static boolean isCmccMobile(String num) {
		if(!isMobile(num)) {
			return false;
		}
		String phone = num.substring(num.length() - 11, num.length());
		String regex = "^(134|135|136|137|138|139|150|151|152|157|158|159|182|183|187|188)[0-9]{8}$";
		if(phone.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * 删除字符串中的空白符
     * @param content
     * @return String
     */
    public static String removeBlanks(String content){
        if(content==null){              
            return null;
        }
        StringBuffer buff = new StringBuffer();
        buff.append(content);
        for(int i = buff.length() - 1; i >= 0; i--){
            if(' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i)) || ('\r' == buff.charAt(i))) {
                buff.deleteCharAt(i);
            }
        }
        return buff.toString();
    }
    
    /**
	 * 判断邮箱格式
	 */
	public static boolean isEmail(String str) {
		String check = "\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String joinStrings(List<String> strings, String divider) {
		if(strings == null || strings.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Object str : strings) {
			if(first) {
				first = false;
			} else {
				sb.append(divider);
			}
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static <T> List<String> toStrList(List<T> srcs) {
		if(CollectionUtil.isEmpty(srcs)) {
			return null;
		}
		List<String> strs = new ArrayList<String>();
		for(T i : srcs) {
			strs.add(String.valueOf(i));
		}
		return strs;
	}
}
