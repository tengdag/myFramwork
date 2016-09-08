package com.tengdag.myframwork.util;

import android.content.Context;
import android.database.Cursor;
import android.net.*;
import android.net.wifi.*;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

/**
 * 网络信息工具
 * 
 * <br>4.0以上限制了第三方应用对apn的设置权限，访问wap网络时通过设置代理而 非设置apn来实现。
 * @author cjx
 *
 */
public class NetworkUtil {
	/**
	 * 当前apn访问uri
	 */
	private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	//apn
	public static final String APN_CTNET = "ctnet";
	public static final String APN_CTWAP = "ctwap";
	public static final String APN_CMNET = "cmnet";
	public static final String APN_CMWAP = "cmwap";
	public static final String APN_UNIWAP = "uniwap";
	public static final String APN_UNINET = "uninet";
	public static final String APN_UNI3GWAP = "3gwap";
	public static final String APN_UNI3GNET = "3gnet";
	
	/**
	 * 网络类型
	 * @author cjx
	 *
	 */
	public static enum NetType {
		/**
		 * 未知
		 */
		UNKNOWN,
		/**
		 * wifi
		 */
		WIFI,
		/**
		 * 电信net
		 */
		CTNET,
		/**
		 * 电信wap
		 */
		CTWAP,
		/**
		 * 移动net
		 */
		CMNET,
		/**
		 * 移动wap
		 */
		CMWAP,
		/**
		 * 联通2gwap
		 */
		UNIWAP,
		/**
		 * 联通2gnet
		 */
		UNINET,
		/**
		 * 联通3gwap
		 */
		UNI3GWAP,
		/**
		 * 联通3gnet
		 */
		UNI3GNET;
	}
	
	/**
	 * 运营商
	 * @author cjx
	 *
	 */
	public static enum Operators {
		/**
		 * 未知
		 */
		Unknown,
		
		/**
		 * 电信
		 */
		Telecom,
		
		/**
		 * 联通
		 */
		Unicom,
		
		/**
		 * 移动
		 */
		Mobile;
	}
	
	/**
	 * 是否有可用的网络，判断网络是否已连接，使用{@link #isNetConnected(Context)}
	 * @param mContext
	 * @return
	 */
	public static boolean isNetAvailable(Context mContext) {
		NetworkInfo networkInfo = getActiveNetworkInfo(mContext);
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		} else {
			return false;
		}
	}

	/**
	 * 网络是否连接
	 * @param mContext
	 * @return
	 */
	public static boolean isNetConnected(Context mContext) {
		NetworkInfo networkInfo = getActiveNetworkInfo(mContext);
		if (networkInfo != null) {
			if (networkInfo.getState().compareTo(NetworkInfo.State.CONNECTED) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 网络是否连接中
	 * @param mContext
	 * @return
	 */
	public static boolean isNetConnecting(Context mContext) {
		NetworkInfo networkInfo = getActiveNetworkInfo(mContext);
		if (networkInfo != null) {
			if (networkInfo.getState().compareTo(NetworkInfo.State.CONNECTING) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取当前网络类型，如果没有可用的网络返回{@link NetType#UNKNOWN}
	 * 
	 * <p>以下是wifi和联通3gnet网络下的示例：
	 * <br>---------------------------wifi---------------------------
	 * <br>ExtraInfo:"Tenda"
	 * <br>SubtypeName:
	 * <br>TypeName:WIFI
	 * <br>---------------------------3gnet--------------------------
	 * <br>ExtraInfo:3gnet
	 * <br>SubtypeName:HSPA+
	 * <br>TypeName:mobile
	 * <br>----------------------------------------------------------
	 * 
	 * @param context
	 * @return
	 */
	public static NetType getNetType(Context context) {
		NetType type = NetType.UNKNOWN;
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if(networkInfo != null) {
			String typeName = networkInfo.getTypeName();
			String extraInfo = networkInfo.getExtraInfo();
			if(!TextUtils.isEmpty(typeName)) {
				if(typeName.toUpperCase(Locale.getDefault()).indexOf("wifi") >= 0) {
					type = NetType.WIFI;
				} else if(!TextUtils.isEmpty(extraInfo)) {
					extraInfo = extraInfo.toUpperCase(Locale.getDefault());
					if(extraInfo.indexOf(APN_CMNET) >= 0) {
						type = NetType.CMNET;
					} else if(extraInfo.indexOf(APN_CMWAP) >= 0) {
						type = NetType.CMWAP;
					} else if(extraInfo.indexOf(APN_CTNET) >= 0) {
						type = NetType.CTNET;
					} else if(extraInfo.indexOf(APN_CTWAP) >= 0) {
						type = NetType.CTWAP;
					} else if(extraInfo.indexOf(APN_UNI3GNET) >= 0) {
						type = NetType.UNI3GNET;
					} else if(extraInfo.indexOf(APN_UNI3GWAP) >= 0) {
						type = NetType.UNI3GWAP;
					} else if(extraInfo.indexOf(APN_UNINET) >= 0) {
						type = NetType.UNINET;
					} else if(extraInfo.indexOf(APN_UNIWAP) >= 0) {
						type = NetType.UNIWAP;
					}
				}
			}
		}
		return type;
	}

	private static NetworkInfo getActiveNetworkInfo(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * 当前网络是否wifi
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		try {
			ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * wap网络时获取代理ip。当前网络如果不是wap网络时返回的值为null，因此当返回值不为null时，可以确定当前是wap网络。
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getNetworkProxyUrl() {
    	String proxyHost = android.net.Proxy.getDefaultHost();
    	return proxyHost;
    }

	/**
	 * wap网络时获取代理端口
	 * @return
	 */
    @SuppressWarnings("deprecation")
	public static int getNetworkProxyPort() {
    	int proxyPort = android.net.Proxy.getDefaultPort();
    	return proxyPort;
    }
    
    public static String getOperatorsProxyUrl(Operators operators) {
    	switch (operators) {
		case Unicom:
		case Mobile:
			return "10.0.0.172";
			
		case Telecom:
			return "10.0.0.200";
			
		default:
			return null;
		}
    }
    
    public static int getOperatorsProxyPort(Operators operators) {
    	switch (operators) {
		case Unicom:
		case Mobile:
		case Telecom:
			return 80;
			
		default:
			return 0;
		}
    }

	private static WifiManager getWifiManager(Context context) {
		return  (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 获取连接的wifi信息
	 * @param context
	 * @return
	 */
	public static WifiInfo getConnectionWifi(Context context) {
		return getWifiManager(context).getConnectionInfo();
	}

	/**
	 * 获取可用的wifi列表
	 * @param context
	 * @return
	 */
	public static List<ScanResult> getWifiList(Context context) {
		return getWifiManager(context).getScanResults();
	}
	
	/**
	 * 获取当前卡的所属运营商
	 * 
	 * <p>permission：android.permission.READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static Operators getOperators(Context context){
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);   
		String imsi = telManager.getSubscriberId();   
		if(imsi != null){   
		    if(imsi.startsWith("46000") || imsi.startsWith("46002")){//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号   
		        //中国移动   
		    	return Operators.Mobile;
		    } else if(imsi.startsWith("46001")){   
		        //中国联通   
		    	return Operators.Unicom;
		    } else if(imsi.startsWith("46003") || imsi.startsWith("45502")){//455-02  中國電信澳門    
		        //中国电信   
		    	return Operators.Telecom;
		    }   
		}
		return Operators.Unknown;
	}

	
	/**
	 * 获取当前接入网络的手机卡apn。如果没有卡接入网络，则获取卡1的apn，wifi是否开启不影响。该方法不能用来获取当前连接的网络类型，要获取当前连接的网络类型使用
	 * {@link #getNetType(Context)}。
	 * 
	 * <p>permission：android.permission.READ_PHONE_STATE
	 * 
	 * <p>联通3gwap详情示例：
	 * <br>------------------------------------------------------------
	 * <br>  _id:1581
	 * <br>  name:中国联通 Wap 网络 (China Unicom)
	 * <br>  numeric:46001
	 * <br>  mcc:460
	 * <br>  mnc:01
	 * <br>  apn:3gwap
	 * <br>  user:null
	 * <br>  server:null
	 * <br>  password:null
	 * <br>  proxy:10.0.0.172
	 * <br>  port:80
	 * <br>  mmsproxy:null
	 * <br>  mmsport:null
	 * <br>  mmsc:null
	 * <br>  authtype:-1
	 * <br>  type:default,supl
	 * <br>  current:1
	 * <br>  sourcetype:0
	 * <br>  csdnum:null
	 * <br>  protocol:IP
	 * <br>  roaming_protocol:IP
	 * <br>  omacpid:null
	 * <br>  napid:null
	 * <br>  proxyid:null
	 * <br>  carrier_enabled:1
	 * <br>  bearer:0
	 * <br>  spn:null
	 * <br>  imsi:null
	 * <br>  pnn:null
	 * <br>  ppp:null
	 * <br>------------------------------------------------------------
	 * 
	 * @param context
	 * @return
	 */
	public static String getApn(Context context) {
		String apn = null;
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				apn = c.getString(c.getColumnIndex("apn"));
			}
			c.close();
			c = null;
		}

		return apn == null ? null : apn.toLowerCase(Locale.getDefault());
	}
}
