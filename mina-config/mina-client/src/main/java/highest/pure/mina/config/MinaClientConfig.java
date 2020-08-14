package highest.pure.mina.config;

import highest.pure.mina.handler.MinaClientHandler;
import highest.pure.mina.protocol.MessagePack;
import highest.pure.mina.protocol.MessageProtocolCodecFactory;
import highest.pure.mina.protocol.MessageType;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(MinaClientProperty.class)
public class MinaClientConfig {

    @Resource
    private MinaClientProperty config;

    @Bean(initMethod = "connect")
    public NioSocketConnector nioSocketConnector(MinaClientHandler minaClientHandler, DefaultIoFilterChainBuilder defaultIoFilterChainBuilder) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(config.getIp(), config.getPort());

        NioSocketConnector nioSocketConnector = new NioSocketConnector();

        nioSocketConnector.getSessionConfig().setReadBufferSize(config.getReadBufferSize());
        nioSocketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, config.getIdleTimeout());

        nioSocketConnector.setFilterChainBuilder(defaultIoFilterChainBuilder);
        nioSocketConnector.setHandler(minaClientHandler);

        nioSocketConnector.setDefaultRemoteAddress(inetSocketAddress);
//        nioSocketConnector.connect();

        return nioSocketConnector;
    }

    @Bean
    public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder(
            ExecutorFilter executorFilter,
            LoggingFilter loggingFilter,
            ProtocolCodecFilter protocolCodecFilter,
            KeepAliveFilter keepAliveFilter) {
        DefaultIoFilterChainBuilder defaultIoFilterChainBuilder = new DefaultIoFilterChainBuilder();
        Map<String, IoFilter> filters = new LinkedHashMap<>();
        filters.put("executorFilter", executorFilter);
        filters.put("logging", loggingFilter);
        filters.put("codec", protocolCodecFilter);
        filters.put("heartBeat", keepAliveFilter);
        defaultIoFilterChainBuilder.setFilters(filters);
        return defaultIoFilterChainBuilder;
    }

    @Bean
    public ExecutorFilter executorFilter() {
        return new ExecutorFilter(config.getCorePoolSize(), config.getMaximumPoolSize());
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public ProtocolCodecFilter protocolCodecFilter() {
        return new ProtocolCodecFilter(new MessageProtocolCodecFactory());
    }

    @Bean
    public KeepAliveFilter keepAliveFilter() {
        KeepAliveMessageFactory keepAliveMessageFactory = new KeepAliveMessageFactory() {
            @Override
            public boolean isRequest(IoSession ioSession, Object o) {
                return false;
            }

            @Override
            public boolean isResponse(IoSession ioSession, Object o) {
                if (o instanceof MessagePack) {
                    MessagePack messagePack = (MessagePack) o;
                    if (messagePack.getMessageType() == MessageType.HEARTBEAT.getMessageType()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Object getRequest(IoSession ioSession) {
                return new MessagePack(MessageType.HEARTBEAT, "HEARTBEAT");
            }

            @Override
            public Object getResponse(IoSession ioSession, Object o) {
                return null;
            }
        };

        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(keepAliveMessageFactory);
        keepAliveFilter.setForwardEvent(true);
        keepAliveFilter.setRequestInterval(config.getHeartBeatRate());
        return keepAliveFilter;
    }
}
