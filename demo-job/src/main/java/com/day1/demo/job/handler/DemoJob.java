package com.day1.demo.job.handler;

import com.day1.demo.common.logback.log.DemoLogger;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

/**
 * @author: LinHangHui
 * @Date: 2020/11/25 21:04
 */
@Component
@JobHandler(value = "DemoJob")
public class DemoJob extends IJobHandler {

    private DemoLogger logger = new DemoLogger(DemoJob.class);

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        logger.info("==DemoJob==>begin****");
        logger.info("==DemoJob==>result******"+s);
        return ReturnT.SUCCESS;
    }
}
