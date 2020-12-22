package com.day1.demo.common.logback.request;

import java.util.regex.Pattern;

/**
 * @author: LinHangHui
 * @Date: 2020/11/4 20:59
 */
public class BrowserTypeUtils {
    private static Pattern sQQClict = Pattern.compile("qq/\\d+\\.\\d+\\.\\d+\\.\\d+");

    /**
     * PC UA
     */
    private static final String[] PC_UA = new String[]{"windows nt",
            "windows vista", "windows xp", "windows 98", "windows 95", "win32",
            "windows me", "macintosh", "linux i686", "linux i386",
            "linux i586", "linux x86", "konqueror", "ubuntu", "freebsd",
            "solaris", "sunos", "mac_powerpc", "baiduspider", "googlebot",
            "msnbot", "infoseek", "yahoo", "sogou"};

    /**
     * 查询当前访问用户浏览器类型
     *
     * @param
     * @return
     */
    public static BrowserType getBrowserType() {
        if (isWeixin()) {
            return BrowserType.WECHAT;
        } else if (isPC()) {
            return BrowserType.PC;
        } else if (isQQClient()) {
            return BrowserType.QQ;
        } else if (isAndroidBrowser()) {
            return BrowserType.ANDROID_BROWSER;
        } else if (isIphoneBrowser()) {
            return BrowserType.IPHONE_BROWSER;
        } else if (isAlipayClient()) {
            return BrowserType.ALICLIENT;
        }
        return BrowserType.OTHERS;
    }


    private static boolean isQQClient() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        return sQQClict.matcher(ua.toLowerCase()).find();
    }

    private static boolean isWeixin() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        return ua.toLowerCase().contains("micromessenger");
    }

    public static boolean isAlipayClient() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        return ua.toLowerCase().contains("alipayclient");
    }


    public static boolean isPC() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        for (String item : PC_UA) {
            if (ua.toLowerCase().contains(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAndroidBrowser() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        return ua.toLowerCase().contains("android");
    }

    public static boolean isIphoneBrowser() {
        String ua = RequestUtils.getUA();
        if(ua==null){
            return false;
        }
        return ua.toLowerCase().contains("iphone");
    }
}
