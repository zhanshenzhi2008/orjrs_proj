package com.orjrs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "girl")
public class GirlProperties {
    private String cupSize;
    private String age;

    public String getAge() {
        return age;
    }

    public String getCupSize() {

        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
