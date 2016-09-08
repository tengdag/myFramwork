package com.tengdag.myframwork.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 设备信息相关工具类,包括分辨率,imei
 */
public class DevUtil {

	private static String TAG = DevUtil.class.getSimpleName();

	private static Context mContext;
	private static DisplayMetrics dm;
	private static int mStatusHeight = 0;

	/**
	 * 初始化，必须先调用此方法，才能调用{@link DevUtil}的其他方法
	 * @param context
	 */
	public static void init(Context context) {
		if(mContext == null) {
			mContext = context;
			dm = mContext.getResources().getDisplayMetrics();
		}
	}

	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static int getWidth() {
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * @return
	 */
	public static int getHeight() {
		return dm.heightPixels;
	}

	/**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight() {
		if(mStatusHeight <= 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object obj = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = Integer.parseInt(field.get(obj).toString());
				int sbar = mContext.getResources().getDimensionPixelSize(x);
				mStatusHeight = sbar;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mStatusHeight;
	}

	/**
	 * 获取状态栏高度
	 * @param activity
	 * @return
	 */
	public static int getStatusBarHeight(Activity activity) {
		return getHeight() - getActivityHeight(activity);
	}

	/**
	 * 获取Activity高度(屏幕高度-状态栏高度)
	 * @param activity
	 * @return
	 */
	public static int getActivityHeight(Activity activity) {
		Rect rect= new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.height();
	}

	/**
	 * 获取Activity高度
	 * @return
	 */
	public static int getActivityHeight() {
		return getWidth() - getStatusBarHeight();
	}

	/**
	 * dip转px
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		return (int)(dipValue * dm.density + 0.5f);
	}

	/**
	 * px转dip
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(float pxValue){
		return (int)((pxValue - 0.5) / dm.density);
	}

	/**
	 * 判断锁屏
	 * @param context
	 * @return
	 */
	public static boolean isScreenLocked(Context context) {
		KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
		return !mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	/**
	 * 保持亮屏
	 * 高版本系统不可用
	 */
	public static void keepScreenOn(Activity activity) {
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * 当前设备是否模拟器
	 * @return
	 */
	public static boolean isEmulator() {
		TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.equals("000000000000000")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取imsi号
	 * <br>IMSI:国际移动用户识别码(International Mobile SubscriberIdentification Number)，是区别移动用户的标志，
	 * 储存在SIM卡中，可用于区别移动用户的有效信息。其总长度不超过15位，同样使用0～9的数字。其中MCC是移动用户所
	 * 属国家代号，占3位数字，中国的MCC规定为460；MNC是移动网号码，最多由两位数字组成，用于识别移动用户所归属的
	 * 移动通信网；MSIN是移动用户识别码，用以识别某一移动通信网中的移动用户。
	 * @return
	 */
	public static String getImsi() {
		TelephonyManager mTelephonyMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getSubscriberId();
	}

	/**
	 * 获取imei号
	 * <br>IMEI:国际移动设备识别码(International Mobile Equipment Identity)，是区别移动设备的标志，
	 * 是由15位数字组成的"电子串号"，它与每台手机一一对应，而且该码是全世界唯一的。每一只手机在组装完
	 * 成后都将被赋予一个全球唯一的一组号码，这个号码从生产到交付使用都将被制造生产的厂商所记录。
	 * @return
	 */
	public static String getImei() {
		TelephonyManager mTelephonyMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}

	/**
	 * 设置Activity全屏，在setContentView()之前调用
	 * <br>AndroidManifest.xml中配置采用：android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
	 * @param activity
	 */
	public static void setFullScreen(Activity activity) {
		//去除title
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

}
