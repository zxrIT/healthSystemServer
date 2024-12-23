package com.ZengXiangRui.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@EnableConfigurationProperties({AuthenticationProperties.class})
@Component
@ConfigurationProperties(prefix = "health.auth")
public class AuthenticationProperties {
    private List<String> excludePaths;
}
