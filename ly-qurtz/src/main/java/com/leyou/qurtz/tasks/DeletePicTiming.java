package com.leyou.qurtz.tasks;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.leyou.common.constants.BaseRedisConstants;
import com.leyou.qurtz.config.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Huang Mingwang
 * @create 2021-05-25 3:41 下午
 */
@Component("delPicQurtz")
@Slf4j
public class DeletePicTiming {
    private final StringRedisTemplate stringRedisTemplate;
    private final AliOssProperties aliOssProperties;
    private final OSS ossClient;

    public DeletePicTiming(StringRedisTemplate stringRedisTemplate, AliOssProperties aliOssProperties, OSS ossClient) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.aliOssProperties = aliOssProperties;
        this.ossClient = ossClient;
    }

    public void deletePicTiming() {
        Set<String> diffImg = stringRedisTemplate.opsForSet().difference(BaseRedisConstants.BRAND_PIC_RESOURCE,
                BaseRedisConstants.BRAND_PIC_DB_RESOURCE);
        List<String> keys = new ArrayList<>(diffImg);
        List<String> list = keys.stream().map(s -> s.split(".com/")[1]).collect(Collectors.toList());
        if (list.size() != 0) {
            ossClient.deleteObjects(new DeleteObjectsRequest(aliOssProperties.getBucketName()).withKeys(list));
            for (String key : keys) {
                stringRedisTemplate.opsForSet().remove(BaseRedisConstants.BRAND_PIC_RESOURCE, key);
            }
            log.info("清理图片成功，时间：{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        } else {
            log.info("无需清理图片,时间:{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        }
    }
}
