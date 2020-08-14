package highest.pure.mina.protocol;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MessagePack {

    // 数据总长度
    private int len;

    // 数据传输类型
    private byte filter = 0x0A;

    // 模块代码
    private int messageType;

    // 包体
    private String body;

    public static final int PACK_HEADER_LEN = 9; // sizeof(len) + sizeof(filter) + sizeof(messageType)

    /**
     * 表示本项目传输的模块
     */
    public static final byte MESSAGE_FILTER = 0x0A;

    public MessagePack(MessageType messageType, String body) {
        this.messageType = messageType.getMessageType();
        this.body = body;

        this.len = PACK_HEADER_LEN + (StringUtils.isBlank(body) ? 0 : body.getBytes().length);
    }

    public int getLen() {
        return PACK_HEADER_LEN + (StringUtils.isBlank(body) ? 0 : body.getBytes().length);
    }
}
