package com.day1.demo.common.logback.log;

import com.day1.demo.common.logback.DemoLoggerEnum;
import com.day1.demo.common.logback.utils.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author: LinHangHui
 * @Date: 2020/10/28 20:33
 */
public class DemoLogger {

    private Logger logger;

    private String className;

    public DemoLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(DemoLogger.class);
        this.className = clazz.getName();
    }

    public void info(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogFileUtils.MsgLogBean msgLogBean = LogFileUtils.MsgLogBean.ofInfo(DemoLoggerEnum.INFO.title, ft.getMessage(), className);
        logger.info(msgLogBean.toString());
    }

    public void infoNotUser(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogFileUtils.MsgLogBean msgLogBean = LogFileUtils.MsgLogBean.ofInfoNotUser(DemoLoggerEnum.INFO.title, ft.getMessage(), className);
        logger.info(msgLogBean.toString());
    }

    public void warn(String format, Object... arguments) {
        int i = 0;
        for (Object argument : arguments) {
            if (argument instanceof Exception) {
                Exception e = (Exception) argument;
                String errorStackMsg = ExceptionUtils.getExceptionAllinformation_01(e);
                arguments[i] = errorStackMsg;
            }
            i++;
        }

        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogFileUtils.MsgLogBean msgLogBean = LogFileUtils.MsgLogBean.ofWarn(DemoLoggerEnum.WARN.title, ft.getMessage(), className);
        logger.warn(msgLogBean.toString());
    }

    public void error(DemoLoggerEnum dle, String format, Object... arguments) {
        int i = 0;
        for (Object argument : arguments) {
            if (argument instanceof Exception) {
                Exception e = (Exception) argument;
                String errorStackMsg = ExceptionUtils.getExceptionAllinformation_01(e);
                arguments[i] = errorStackMsg;
            }
            i++;
        }
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogFileUtils.MsgLogBean msgLogBean = LogFileUtils.MsgLogBean.ofWarn(dle.title, ft.getMessage(), className);
        logger.warn(msgLogBean.toString());
    }
}
