package com.ylink.framework.redisson.properties;

import org.redisson.client.codec.Codec;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Redisson 配置信息
 *
 * @author zhanglinfeng
 */
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonProperties {

    /**
     * json格式配置文件
     */
    private String json;

    /**
     * yaml格式配置文件
     */
    private String yaml;

    /**
     * host
     */
    private  String url;


    /**
     * passWord
     */
    private String passWord;

    /**
     * 编码
     */
    private Codec codec;

    /**
     * springCaches
     */
    private List<SpringCacheProperties> springCaches;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getYaml() {
        return yaml;
    }

    public void setYaml(String yaml) {
        this.yaml = yaml;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public List<SpringCacheProperties> getSpringCaches() {
        return springCaches;
    }

    public void setSpringCaches(List<SpringCacheProperties> springCaches) {
        this.springCaches = springCaches;
    }
}
