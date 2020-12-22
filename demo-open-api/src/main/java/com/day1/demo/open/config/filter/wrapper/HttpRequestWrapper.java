package com.day1.demo.open.config.filter.wrapper;

import jodd.io.StreamUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 10:08
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 解决request.getInputStream()或request.getReader()方法只能被调用一次的问题
     * @RequestBody 会再次调用流，导致获取的request为空
     * 解决方案：将读取的数据保存起来
     */
    private byte[] body;

    public HttpRequestWrapper(HttpServletRequest request) throws IOException {

        super(request);
        //读取请求的数据保存到本类当中
        body = StreamUtil.readBytes(request.getReader(), "UTF-8");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return !isReady();
            }

            @Override
            public boolean isReady() {
                return bais.available() > 0;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    public byte[] getBody() {
        return body;
    }
}
