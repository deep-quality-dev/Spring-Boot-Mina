package highest.pure.mina.entity;

import lombok.Data;

@Data
public class ConfigurationMessage extends MessageBase {

    /**
     * 机器码
     */
    private String machineCode;

    /**
     * true：rpc calling
     */
    private boolean isRpcMode;

    /**
     * true: 服务端调用时动作为rpc，false：客户端调用时动作为rpc
     */
    private boolean toClient;

    public ConfigurationMessage() {
        super("Configuration");
    }

}
