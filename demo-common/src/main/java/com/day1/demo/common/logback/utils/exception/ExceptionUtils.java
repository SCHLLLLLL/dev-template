package com.day1.demo.common.logback.utils.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author: LinHangHui
 * @Date: 2020/10/28 22:02
 */
public class ExceptionUtils extends org.apache.tomcat.util.ExceptionUtils {

    public static String getExceptionAllinformation_01(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        ex.printStackTrace(ps);
        String ret = new String(out.toByteArray());
        ps.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}
