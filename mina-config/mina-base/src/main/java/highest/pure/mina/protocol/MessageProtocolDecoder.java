package highest.pure.mina.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

public class MessageProtocolDecoder extends CumulativeProtocolDecoder {

    private final Charset charset;

    public MessageProtocolDecoder() {
        this(Charset.defaultCharset());
    }

    public MessageProtocolDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        // 拆包时，如果可读数据的长度小于包头的长度，就不进行读取
        if (ioBuffer.remaining() < MessagePack.PACK_HEADER_LEN) {
            return false;

        } else {
            // 标记当前的位置，以便以后的reset操作能恢复位置
            ioBuffer.mark();

            try {
                // 获取总长度
                int length = ioBuffer.getInt();
                if (ioBuffer.remaining() < length - 4) {
                    // 如果可读取数据的长度不够，则结束拆包，等待下一次
                    ioBuffer.reset();
                    return false;
                }

                byte filter = ioBuffer.get();
                if (filter != MessagePack.MESSAGE_FILTER) {
                    clearData(ioBuffer);
                    return false;
                }

                // 获取信息类型
                int messageType = ioBuffer.getInt();
                if (ioBuffer.remaining() < length - MessagePack.PACK_HEADER_LEN) {
                    ioBuffer.reset();
                    return false;
                }

                String body = ioBuffer.getString(length - MessagePack.PACK_HEADER_LEN, charset.newDecoder());
                MessagePack messagePack = new MessagePack(MessageType.fromInt(messageType), body);
                protocolDecoderOutput.write(messagePack);
                return ioBuffer.remaining() > 0;

            } catch (Exception ex) {
                ex.printStackTrace();

                ioBuffer.reset();
                clearData(ioBuffer);
                return false;
            }
        }
    }

    private void clearData(IoBuffer ioBuffer) {
        byte [] data = new byte[ioBuffer.remaining()];
        ioBuffer.get(data);
        data = null;
    }
}
