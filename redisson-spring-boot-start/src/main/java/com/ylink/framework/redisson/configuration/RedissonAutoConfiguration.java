package com.ylink.framework.redisson.configuration;

import com.ylink.framework.redisson.properties.RedissonProperties;
import com.ylink.framework.redisson.properties.SpringCacheProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Redisson注入配置
 * @author zhanglinfeng
 */
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(RedissonAutoConfiguration.class);
    @Autowired
    private RedissonProperties redissonProperties;


    public Config configJson() throws IOException {
        File file = ResourceUtils.getFile(redissonProperties.getJson());
        return Config.fromJSON(file);
    }

    public Config configYaml() throws IOException {
        File file = ResourceUtils.getFile(redissonProperties.getYaml());
        return Config.fromYAML(file);
    }

    @Bean
    @ConditionalOnMissingBean
    public Config config() throws IOException {
        if (!StringUtils.isEmpty(redissonProperties.getJson())) {
            return configJson();
        } else if (!StringUtils.isEmpty(redissonProperties.getYaml())) {
            return configYaml();
        } else if(!StringUtils.isEmpty(redissonProperties.getUrl())){
            Config config = new  Config();
            SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(redissonProperties.getUrl());
            if(!StringUtils.isEmpty(redissonProperties.getPassWord())){
                singleServerConfig.setPassword(redissonProperties.getPassWord());
            }
            if(redissonProperties.getCodec()!=null){
                try {
                    config.setCodec((Codec) redissonProperties.getCodec().newInstance());
                } catch (Exception e) {
                    logger.warn("序列化编码无法解析！传入:[{}]",redissonProperties.getCodec());
                }

            }
            config.setThreads(redissonProperties.getThreads());
            config.setNettyThreads(redissonProperties.getNettyThreads());

            return config;
        }else {
            throw new RuntimeException("please offer the config file by json/yaml");
        }
    }

    @Bean(destroyMethod="shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(Config config) throws IOException {
        logger.info("创建RedissonClient, config : {}", config.toJSON());
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        if(CollectionUtils.isEmpty(redissonProperties.getSpringCaches())){
            logger.info("创建springcache失败,没有配置相应的cache ");
            return null;
        }
        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>(10);
        for (SpringCacheProperties springCacheProperties : redissonProperties.getSpringCaches()) {
            CacheConfig cacheConfig = config.get(springCacheProperties.getCacheName());
            if(cacheConfig == null){
                cacheConfig = new CacheConfig();
                config.put(springCacheProperties.getCacheName(),cacheConfig);
            }
            cacheConfig.setTTL(springCacheProperties.getTtl());
            cacheConfig.setMaxIdleTime(springCacheProperties.getMaxIdleTime());
        }

        return new RedissonSpringCacheManager(redissonClient, config);
    }


}
