package com.wzf.baseutils.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * ================================================
 * 描    述：获取设备基础信息
 * 作    者：wzf
 * 创建日期：2016/7/20
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class DevicUtil {

    /**
     * 所需权限
     * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> getMac方法
     **/

    private static String deviceId;    // 获取设备编号
    private static String mac; // 获取Mac地址
    private static String version; // 获取系统平台版本
    private static String yysNum;  // 获取网络运营商代号
    private static String yysName; // 获取网络运营商名称
    private static int networkType; // 获取手机网络类型
    private static String countryType; // 获取SIM卡的国别
    private static String xlh; //  获取SIM卡的序列号
    private static int simMode; // 获取SIM卡的状态
    private static String deviceInfo;   // 手机信息，如系统

    public static String getDeviceId(Context context) {
        if (!TextUtils.isEmpty(deviceId))
            return deviceId;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = manager.getDeviceId();
        return deviceId;
    }

    public static String getMac(Context context) {
        if (!TextUtils.isEmpty(mac))
            return mac;
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mac = manager.getConnectionInfo().getMacAddress();
        return mac;
    }

    public static String getSysVersion(Context context) {
        if (!TextUtils.isEmpty(version))
            return version;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        version = manager.getDeviceSoftwareVersion() != null ? manager.getDeviceSoftwareVersion() : "未知";
        return version;
    }

    public static String getYysNum(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        yysNum = manager.getNetworkOperator();
        return yysNum;
    }

    public static String getYysName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        yysName = manager.getNetworkOperatorName();
        return yysName;
    }

    public static int getNetworkType(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        networkType = manager.getPhoneType();
        return networkType;
    }

    public static String getSimCountry(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        countryType = manager.getSimCountryIso();
        return countryType;
    }

    public static String getSimNum(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        xlh = manager.getSimSerialNumber();
        return xlh;
    }

    public static int getSimState(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        simMode = manager.getSimState();
        return simMode;
    }

    public static String getDeviceInfo(Context context) {
        if (!TextUtils.isEmpty(deviceInfo))
            return deviceInfo;
        deviceInfo = android.os.Build.MODEL + "\n"
                + android.os.Build.VERSION.SDK + "\n"
                + android.os.Build.VERSION.RELEASE;
        return deviceInfo;
    }
}
