package highest.pure.mina.protocol;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class MessageProtocolEncoder extends ProtocolEncoderAdapter {

    private final Charset charset;

    public MessageProtocolEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        MessagePack messagePack = (MessagePack)o;
        IoBuffer ioBuffer = IoBuffer.allocate(messagePack.getLen()).setAutoExpand(true);
        ioBuffer.putInt(messagePack.getLen());
        ioBuffer.put(messagePack.getFilter());
        ioBuffer.putInt(messagePack.getMessageType());
        if (StringUtils.isNotBlank(messagePack.getBody())) {
            ioBuffer.putString(messagePack.getBody(), charset.newEncoder());
        }
        ioBuffer.flip();
        protocolEncoderOutput.write(ioBuffer);
    }
}
