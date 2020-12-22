package com.day1.demo.common.logback.request;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: LinHangHui
 * @Date: 2020/11/4 20:58
 */
public class RequestUtils {

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取 UA
     *
     * @return
     */
    public static String getUA() {
        HttpServletRequest request = getRequest();
        return request.getHeader("user-agent");
    }

    /**
     * 获取客户端IP地址，此方法用在proxy环境中
     *
     * @return
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = StringUtils.split(ip, ',');
            if (ips != null) {
                for (String tmpip : ips) {
                    if (StringUtils.isBlank(tmpip)) {
                        continue;
                    }
                    tmpip = tmpip.trim();
                    if (isIpAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)) {
                        return tmpip.trim();
                    }
                }
            }
        }
        ip = request.getHeader("x-real-ip");
        if (isIpAddr(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        if (ip.indexOf('.') == -1) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获取COOKIE
     *
     * @param name
     */
    public static Cookie getCookie(String name) {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck;
            }
        }
        return null;
    }

    /**
     * 获取COOKIE
     *
     * @param name
     */
    public static String getCookieValue(String name) {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck.getValue();
            }
        }
        return null;
    }

    /**
     * 设置COOKIE
     *
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
                                 String value, int maxAge) {
        setCookie(request, response, name, value, maxAge, true);
    }

    /**
     * 设置COOKIE
     *
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
                                 String value, int maxAge, boolean allSubDomain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (allSubDomain) {
            String serverName = request.getServerName();
            String domain = getDomainOfServerName(serverName);
            if (domain != null && domain.indexOf('.') != -1) {
                cookie.setDomain('.' + domain);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name, boolean allSubDomain) {
        setCookie(request, response, name, "", 0, allSubDomain);
    }

    /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     *
     * @param host
     * @return
     */
    public static String getDomainOfServerName(String host) {
        if (isIpAddr(host)) {
            return null;
        }
        String[] names = StringUtils.split(host, '.');
        int len = names.length;
        if (len == 1){
            return null;
        }

        if (len == 3) {
            return makeup(names[len - 2], names[len - 1]);
        }
        if (len > 3) {
            String dp = names[len - 2];
            if ("com".equalsIgnoreCase(dp) || "gov".equalsIgnoreCase(dp) || "net".equalsIgnoreCase(dp) || "edu".equalsIgnoreCase(dp) || "org".equalsIgnoreCase(dp)) {
                return makeup(names[len - 3], names[len - 2], names[len - 1]);
            } else {
                return makeup(names[len - 2], names[len - 1]);
            }
        }
        return host;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr
     * @return
     */
    public static boolean isIpAddr(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, '.');
        if (ips.length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
        }
        return false;
    }

    private static String makeup(String... ps) {
        StringBuilder s = new StringBuilder();
        for (int idx = 0; idx < ps.length; idx++) {
            if (idx > 0) {
                s.append('.');
            }
            s.append(ps[idx]);
        }
        return s.toString();
    }

    /**
     * 获取HTTP端口
     *
     * @return
     * @throws MalformedURLException
     */
    public static int getHttpPort() {
        try {
            HttpServletRequest req = getRequest();
            return new URL(req.getRequestURL().toString()).getPort();
        } catch (MalformedURLException excp) {
            return 80;
        }
    }


}
