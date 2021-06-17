package com.yijiinfo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class MainActionApplication {

//    static {
//        ArtemisConfig.host = "10.6.2.100";// 代理API网关nginx服务器ip端口
//        ArtemisConfig.appKey = "28059416";// 秘钥appkey
//        ArtemisConfig.appSecret = "c7TGyEmPyfR42bSxVJmZ";// 秘钥appSecret
//    }
    static {
        ArtemisConfig.host = "10.123.1.166";// 代理API网关nginx服务器ip端口
        ArtemisConfig.appKey = "23732415";// 秘钥appkey
        ArtemisConfig.appSecret = "gldkaPEjryqRl1cvxpfd";// 秘钥appSecret
    }
//    static {
//        ArtemisConfig.host = "192.168.1.138";// 代理API网关nginx服务器ip端口
//        ArtemisConfig.appKey = "23732415";// 秘钥appkey
//        ArtemisConfig.appSecret = "gldkaPEjryqRl1cvxpfd";// 秘钥appSecret
//    }

    public static final String ARTEMIS_PATH = "/artemis";

    public static void main(String[] args) {
        SpringApplication.run(MainActionApplication.class, args);
    }
}