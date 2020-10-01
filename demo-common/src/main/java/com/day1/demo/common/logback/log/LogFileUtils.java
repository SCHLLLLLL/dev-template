package com.day1.demo.common.logback.log;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author: LinHangHui
 * @Date: 2020/10/1 16:42
 */
public class LogFileUtils {

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
    }
}
