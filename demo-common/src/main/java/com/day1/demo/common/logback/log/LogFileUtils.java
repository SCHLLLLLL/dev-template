package com.day1.demo.common.logback.log;


import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author: LinHangHui
 * @Date: 2020/10/1 16:42
 */
public class LogFileUtils {

    @Value("${env:dev}")
    private static String env;

    private static final Logger log = LoggerFactory.getLogger(LogFileUtils.class);

    @Accessors(chain = true)
    @Data
    public static class MsgLogBean{
        public static final String LEVEL_ERROR = "ERROR";

        public static final String LEVEL_INFO = "INFO";

        public static final String LEVEL_WARN = "WARN";

        private String url;
        private String browserType;
        private String clazz;
        private String level;
        private String ip;
        private String browsertype;
        private String targetUrl;
        private String pathUrl;
        private String message;
        private String time;
        private String traceId;
        private String ua;
        private String title;
        private Long userId;

        public static MsgLogBean of(String title, String msg, String level, boolean userFlag) {
            MsgLogBean msgLogBean = new MsgLogBean();
            msgLogBean.setTitle(title);
            msgLogBean.setMessage(msg);
            msgLogBean.setLevel(level);
            msgLogBean.setTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            msgLogBean.setTraceId(MDC.get("traceid"));

            if (null != RequestContextHolder.getRequestAttributes()) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String url = request.getRequestURL().toString() + (StringUtils.isNotEmpty(request.getQueryString()) ? request.getQueryString() : "");
                msgLogBean.setUrl(url);
            }
            return msgLogBean;
        }

        public static MsgLogBean ofInfo(String title, String msg) {
            return of(title, msg, LEVEL_INFO, true);
        }

        public static MsgLogBean ofInfo(String title, String msg, String clazz) {
            MsgLogBean msgLogBean = of(title, msg, LEVEL_INFO, true);
            msgLogBean.setClazz(clazz);
            return msgLogBean;
        }

        public static MsgLogBean ofInfoNotUser(String title, String msg, String clazz) {
            MsgLogBean msgLogBean = of(title, msg, LEVEL_INFO, false);
            msgLogBean.setClazz(clazz);
            return msgLogBean;
        }

        public static MsgLogBean ofWarn(String title, String message, String className) {
            MsgLogBean msgLogBean = of(title, message, LEVEL_WARN, true);
            msgLogBean.setClazz(className);
            return msgLogBean;
        }

        public static MsgLogBean ofWarnNotUser(String title, String message, String className) {
            return of(title, message, LEVEL_WARN, false);
        }

        @Override
        public String toString() {
            ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
            tsb.append("{")
                    .append("\"url\": \"" + getUrl() + "\", ")
                    .append("\"level\": \"" + getLevel() + "\", ")
                    .append("\"userId\": \"" + getUserId() + "\", ")
                    .append("\"ip\": \"" + getIp() + "\", ")
                    .append("\"clazz\": \"" + getClazz() + "\", ")
                    .append("\"browsertype\": \"" + getBrowsertype() + "\", ")
                    .append("\"time\": \"" + getTime() + "\", ")
                    .append("\"ua\": \"" + getUa() + "\", ")
                    .append("\"targetUrl\": \"" + getTargetUrl() + "\", ")
                    .append("\"pathUrl\": \"" + getPathUrl() + "\", ")
                    .append("\"traceId\": \"" + getTraceId() + "\", ")
                    .append("\"title\": \"" + getTitle() + "\", ");
            if (env.equals("pro")) {
                //阿里云日志需要特殊处理
                tsb.append("\"message\": \"" + StringEscapeUtils.escapeJson(getMessage()) + "\"");
            } else {
                tsb.append("\"message\": \"" + getMessage() + "\"");
            }
            return tsb.toString();
        }
    }
}
