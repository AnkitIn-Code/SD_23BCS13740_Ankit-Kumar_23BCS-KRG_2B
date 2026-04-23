package com.example.finalprojectspringboot.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {
    @Value("${app.redis.url:rediss://localhost:6379}")
    private String url;
    @Value("${app.redis.host:localhost}")
    private String host;
    @Value("${app.redis.port:6379}")
    private int port;
    @Value("${app.redis.username:}")
    private String username;
    @Value("${app.redis.password:}")
    private String password;
    @Value("${app.redis.ssl:false}")
    private boolean ssl;
    @Value("${app.redis.timeout:2s}")
    private Duration timeout;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isSsl() { return ssl; }
    public void setSsl(boolean ssl) { this.ssl = ssl; }

    public Duration getTimeout() { return timeout; }
    public void setTimeout(Duration timeout) { this.timeout = timeout; }
}