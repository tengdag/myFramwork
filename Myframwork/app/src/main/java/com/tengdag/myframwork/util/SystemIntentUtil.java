package com.tengdag.myframwork.util;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.List;

/**
 * 提供打开系统Activity的方法
 * 
 * <p>每个方法的第一个参数都是Object类型的对象，该对象只能是Activity、Fragment和Context的其中一种类型，
 * 如果类型是Context或当requestCode==0时，调用startActivity()
 * @author cjx
 *
 */
public class SystemIntentUtil {
	/**
	 * 无线设置
	 * @param context
	 * @param requestCode
	 */
	public static void openWirelessSettingActivity(Object context, int requestCode) {
		Intent mIntent = new Intent();
		ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		mIntent.setComponent(comp);
		startActivityForResult(context, mIntent, requestCode);
	}
	
	/**
	 * 通话
	 * @param context
	 * @param number
	 * @param requestCode
	 */
	public static void openCallPhoneActivity(Object context, String number, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		startActivityForResult(context, intent, requestCode);
	}
	
	/**
	 * 发送短信
	 * @param context
	 * @param address
	 * @param content
	 * @param requestCode
	 */
	public static void openSendSmsActivity(Object context, String address, String content, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", address, null));
		intent.putExtra("sms_body", content);
		startActivityForResult(context, intent, requestCode);
	}
	
	/**
	 * 打开系统图库
	 * @param context
	 * @param requestCode
	 */
	public static void openGalleryActivity(Object context, int requestCode) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(context, intent, requestCode);
	}
	
	/**
	 * 获取系统图库图片路径，一般在onActivityResult()中调用
	 * @param data
	 * @return
	 */
	public static String getPhotoPath(Context context, Intent data) {
		if(data == null || data.getData() == null) {
			return null;
		}
		Uri pickedUri = data.getData();
		String imgPath = "";
		String[] medData = { MediaStore.Images.Media.DATA };
		Cursor picCursor = context.getContentResolver().query(pickedUri, medData, null, null, null);
		if(picCursor != null) {
			int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if(picCursor.moveToFirst()) {
				imgPath = picCursor.getString(index);
			}
		}
		if(TextUtils.isEmpty(imgPath)) {
			imgPath = pickedUri.getPath();
		}
		return imgPath;
	}
	
	/**
	 * 打开谷歌地图app
	 * @param context
	 * @param longitude
	 * @param latitude
	 * @return 没有安装谷歌地图app则返回false
	 */
	public static boolean openGoogleMapActivity(Object context, double longitude, double latitude, int requestCode) {
		List<PackageInfo> apps = getPackageManager(context).getInstalledPackages(0);
		for(PackageInfo app : apps) {
			if(app.packageName.equals("com.google.android.apps.maps")) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + latitude + "," + longitude));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"); 
				startActivityForResult(context, intent, requestCode);
				return true;
			}
		}
		return false;
	}
	
	
	private static void startActivityForResult(Object object, Intent intent, int requestCode) {
		if(object instanceof Activity) {
			Activity activity = (Activity)object;
			if(requestCode != 0) {
				activity.startActivityForResult(intent, requestCode);
			} else {
				activity.startActivity(intent);
			}
		} else if(object instanceof Fragment) {
			Fragment fragment = (Fragment)object;
			if(requestCode != 0) {
				fragment.startActivityForResult(intent, requestCode);
			} else {
				fragment.startActivity(intent);
			}
		} else if(object instanceof Context) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Context context = (Context)object;
			context.startActivity(intent);
		}
	}
	
	private static PackageManager getPackageManager(Object object) {
		if(object instanceof Context) {
			return ((Context)object).getPackageManager();
		} else if(object instanceof Fragment) {
			return ((Fragment)object).getActivity().getPackageManager();
		} else {
			return null;
		}
	}
}
