package highest.pure.mina.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mina.server")
public class MinaServerProperty {

    /**
     * 服务器监听端口
     */
    private Integer port = 9993;

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    private String ip = "127.0.0.1";

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private Integer readBufferSize = 2048;

    public Integer getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    private Integer idleTimeout = 30;

    public Integer getIdleTimeout() {
        return this.idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    private Integer heartBeatRate = 15;

    public Integer getHeartBeatRate() {
        return heartBeatRate;
    }

    public void setHeartBeatRate(Integer heartBeatRate) {
        this.heartBeatRate = heartBeatRate;
    }

    private Integer corePoolSize = 10;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    private Integer maximumPoolSize = 20;

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
}
