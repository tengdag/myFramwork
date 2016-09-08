package com.tengdag.myframwork.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Logcat
 * @author cjx
 *
 */
public class Logcat {
	private String mFilePath;
	private GetLogListener mGetLogListener;
	private boolean sustained = false;
	private Process mLogcatProc;
	
	/**
	 * @param filePath 保存LOG的文件，文件必须已存在
	 * @param listener 获取LOG结束的监听器
	 */
	public Logcat(String filePath, GetLogListener listener) {
		mFilePath = filePath;
		mGetLogListener = listener;
	}
	
	/**
	 * 获取当前已存在的所有LOG一次性输出
	 */
	public void get() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String prog = String.format("logcat -d -f %s -v time", mFilePath);
					Runtime.getRuntime().exec(prog);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(mGetLogListener != null) {
					mGetLogListener.onGetLogComplete(mFilePath);
				}
			}
		}).start();
	}
	
	/**
	 * 持续输出LOG，关闭调用{@link #stop()}
	 */
	public void start() {
		if(sustained) {
			return;
		}
		sustained = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mLogcatProc = Runtime.getRuntime().exec("logcat -v time");
				} catch (Exception e) {
					e.printStackTrace();
				}

				BufferedReader reader = null;
				FileWriter fileWriter = null;
				try {
					reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()), 1024);
					String line;
					fileWriter = new FileWriter(mFilePath, true);
					while((line = reader.readLine()) != null) {
						fileWriter.append(line + "\n");
						fileWriter.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(reader != null) {
						try {
							reader.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					if(fileWriter != null) {
						try {
							fileWriter.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					stop();
					
					if(mGetLogListener != null) {
						mGetLogListener.onGetLogComplete(mFilePath);
					}
				}
			}
		}).start();
	}
	
	/**
	 * 关闭LOG输出，{@link #start()}
	 */
	public void stop() {
		sustained = false;
		if (mLogcatProc != null) {
			mLogcatProc.destroy();
		    mLogcatProc = null;
		}
	}
	
	public interface GetLogListener {
		public void onGetLogComplete(String filePath);
	}
}
