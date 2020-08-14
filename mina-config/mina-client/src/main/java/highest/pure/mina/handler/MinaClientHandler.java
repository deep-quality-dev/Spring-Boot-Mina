package highest.pure.mina.handler;

import highest.pure.mina.protocol.MessagePack;
import highest.pure.mina.protocol.MessageType;
import highest.pure.mina.session.SessionManager;
import highest.pure.mina.utils.SpringContextUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Component;

@Component
public class MinaClientHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);

        SessionManager.setSession(session);

        MessagePack messagePack = new MessagePack(MessageType.COMMAND, "HELLO MESSAGE");
        session.write(messagePack);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);

        SessionManager.closeSession();
        NioSocketConnector nioSocketConnector = (NioSocketConnector)SpringContextUtils.getBean("nioSocketConnector");
        if (nioSocketConnector != null) {
            nioSocketConnector.connect();
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }
}
