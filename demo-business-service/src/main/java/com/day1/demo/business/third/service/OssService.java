package com.day1.demo.business.third.service;

import com.aliyun.oss.OSSClient;
import com.day1.demo.business.config.OssConfig;
import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.utils.UUIDUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.day1.demo.common.logback.DemoLoggerEnum.THIRD_EXCEPTION;


/**
 * @author: LinHangHui
 * @Date: 2020/9/16 16:48
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(OssConfig.class)
public class OssService {

    private final OssConfig ossConfig;

    private static final int BUFFER_SIZE = 4096;

    public String upload(InputStream inputStream, String fileType,OssPathEnum ossPathEnum) {
        OSSClient ossClient = null;
        String filePath;
        try {
            ossClient = new OSSClient(ossConfig.getEndpoint(),ossConfig.getKey(),ossConfig.getSecret());

            String fileName = UUIDUtils.uuid() + "." + fileType;
            String dateTime = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());

            filePath = String.format(ossPathEnum.getPath(),dateTime,fileName);

            ossClient.putObject(ossConfig.getBucket(),filePath,inputStream);
        } catch (Exception e) {
            log.error(THIRD_EXCEPTION + "error:",e);
            throw new BaseException(ExceptionType.SYSTEM_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return filePath;
    }

    @Getter
    private enum OssPathEnum {

        //时间分类
        PUBLIC_PATH("/demo/public/%s/%s","公开文件"),

        PRIVATE_PATH("/demo/private/%s/%s","私密文件");

        private String path;

        private String description;

        OssPathEnum(String path, String description) {
            this.path = path;
            this.description = description;
        }
    }

}
