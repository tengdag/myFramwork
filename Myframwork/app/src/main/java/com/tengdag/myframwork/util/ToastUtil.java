package com.tengdag.myframwork.util;

import android.content.Context;
import android.widget.Toast;

/**
 * toast工具
 * @author cjx
 *
 */
public class ToastUtil {
	public static void show(Context mContext, int resId) {
		Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context mContext, String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLong(Context mContext, int resId) {
		Toast.makeText(mContext, resId, Toast.LENGTH_LONG).show();
	}

	public static void showLong(Context mContext, String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}
}
