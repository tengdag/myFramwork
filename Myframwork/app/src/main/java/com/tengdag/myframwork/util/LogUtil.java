package com.tengdag.myframwork.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具
 * @author cjx
 *
 */
public class LogUtil {
	private static boolean isDebug = false;
	private static String LOG_TAG = "mylog";
	private static boolean writeToFile = false;
	private static boolean first = true;
	
	private static String filePath = null;
	
	
	/**
	 * 初始化
	 * @param isDebug	 是否debug版本
	 * @param defTag 默认标签，如果没有设置，默认为{@link #LOG_TAG}
	 * @param writeToFile 是否将log写到文件
	 */
	public static void init(boolean isDebug, String defTag, boolean writeToFile) {
		LogUtil.isDebug = isDebug;
		if(!TextUtils.isEmpty(defTag)) {
			LOG_TAG = defTag; 
		}
		LogUtil.writeToFile = writeToFile;
	}
	
	public static void init(boolean isDebug, String defTag) {
		LogUtil.init(isDebug, defTag, false);
	}
	
	public static void init(boolean isDebug) {
		LogUtil.init(isDebug, null, false);
	}
	
	public synchronized static void stack(Class<?> class1, String log) {
		stack(class1 != null ? class1.getSimpleName() : null, log);
	}
	
	public synchronized static void stack(String className, String log) {
		String subTag = className;
		Log.v(LOG_TAG, getLog(subTag, log));
		writeToFile(subTag, log);
	}
	
	public synchronized static void log(Class<?> class1, String log) {
		log(class1 != null ? class1.getSimpleName() : null, log);
	}
	
	public synchronized static void log(String className, String log) {
		String subTag = className;
		Log.i(LOG_TAG, getLog(subTag, log));
		writeToFile(subTag, log);
	}
	
	public synchronized static void log(String log) {
		log("", log);
	}
	
	public synchronized static void error(Class<?> class1, String log, Throwable t) {
		error(class1 != null ? class1.getSimpleName() : null, log, t);
	}
	
	public synchronized static void error(String className, String log, Throwable t) {
		String subTag = className;
		Log.e(LOG_TAG, getLog(subTag, log), t);
		writeToFile(subTag, TextUtils.isEmpty(log) ? getThrowableMsg(t) : log + "\n" + getThrowableMsg(t));
	}
	
	public synchronized static void error(String log, Throwable t) {
		error("", log, t);
	}
	
	public synchronized static void error(Class<?> class1, String log) {
		error(class1 != null ? class1.getSimpleName() : null, log);
	}
	
	public synchronized static void error(String className, String log) {
		String subTag = className;
		Log.e(LOG_TAG, getLog(subTag, log));
		writeToFile(subTag, log);
	}
	
	public synchronized static void error(String log) {
		error("", log);
	}
	
	private static String getLog(String subTag, String log) {
		return TextUtils.isEmpty(subTag) ? log : "【" + subTag  + "】 "  + log;
	}
	
	@SuppressLint("SimpleDateFormat")
	private synchronized static void writeToFile(String className, String log) {
		if(!isDebug || !writeToFile) {
			return;
		}
		try {
			if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				return;
			}
			
			File file;
			if(filePath == null) {
				String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lenovoim";
				File dirFile = new File(dir);
				if(!dirFile.exists()) {
					dirFile.mkdirs();
				}
				
				filePath = dir + "/" + LOG_TAG + ".log";
				file = new File(filePath);
				if(!file.exists()) {
					file.createNewFile();
				}
			} else {
				file = new File(filePath);
			}

			StringBuilder sb = new StringBuilder();
			if(first) {
				first = false;
				sb.append("\n\n--------------------------【新的篇章】--------------------------\n\n");
			}
			
			sb.append(new SimpleDateFormat("【MM-dd HH:mm:ss SSS  ").format(new Date()));
			sb.append(className);
			sb.append("】");
			if(log.indexOf("\n") >= 0) {
				sb.append("\n");
			} else {
				sb.append("  ");
			}
			sb.append(log);
			sb.append("\n");
			FileWriter fileWriter;
			if(file.length() > 1 * 1024 * 1024) {
				fileWriter = new FileWriter(filePath, false);
			} else {
				fileWriter = new FileWriter(filePath, true);
			}
			fileWriter.append(sb.toString());
			fileWriter.flush();
			fileWriter.close();
			sb = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static String getThrowableMsg(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String content = info.toString();
        printWriter.close();
        
        return content;
    }
}
