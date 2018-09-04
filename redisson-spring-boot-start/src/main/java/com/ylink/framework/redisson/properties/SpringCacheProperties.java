package com.ylink.framework.redisson.properties;

/**
 * spring cache  配置
 * @author zhanglinfeng
 */
public class SpringCacheProperties {

    /**
     * cacheName 名称
     */
    private String cacheName;

    /**
     * ttl 存活时间
     */
    private Long ttl;
    /**
     * 最大等待时间
     */
    private Long maxIdleTime;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Long getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }
}
