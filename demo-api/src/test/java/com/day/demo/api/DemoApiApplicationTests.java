package com.day.demo.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: LinHangHui
 * @Date: 2020/9/16 15:41
 * @Description:
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc   // 不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@SpringBootTest
public class DemoApiApplicationTests {

    @Before
    public void init() {
        // 必须,否则注解无效
        MockitoAnnotations.initMocks(this);
    }

    protected void fillingThreadUserInfo() {
    }
}
