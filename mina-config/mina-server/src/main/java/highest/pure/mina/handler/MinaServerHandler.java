package highest.pure.mina.handler;

import highest.pure.mina.protocol.MessagePack;
import highest.pure.mina.session.SessionContext;
import highest.pure.mina.session.SessionManager;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
public class MinaServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);

        SessionContext sessionContext = SessionManager.getSession(session);

        System.out.println("sessionIdle: " + (session == null ? "null" : (session.getRemoteAddress().toString())));

        if (sessionContext == null || sessionContext.getTimeoutCount() == null) {
            session.closeNow();
            return;
        }

        try {
            int timeoutCount = sessionContext.getTimeoutCount();
            timeoutCount++;

            if (timeoutCount < SessionContext.MAX_TIMEOUT_COUNT) {
                sessionContext.setTimeoutCount(timeoutCount);
            } else {
                SessionManager.removeSession(sessionContext);
                sessionContext.getSession().closeOnFlush();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            session.closeNow();
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        SocketAddress remoteAddress = session.getRemoteAddress();
        if (remoteAddress == null) {
            System.out.println("RECEIVED: null address");
            return;
        }

        String key = remoteAddress.toString();

        System.out.println("RECEIVED: " + key);

        SessionContext sessionContext = null;
        synchronized (this) {
            sessionContext = SessionManager.getSession(key);
            if (sessionContext != null && !sessionContext.getSession().equals(session)) {
                SessionManager.removeSession(key);

                sessionContext.getSession().closeOnFlush();
            }
            if (sessionContext == null) {
                sessionContext = new SessionContext(session);
                sessionContext.setSessionKey(key);
                sessionContext.setTimeoutCount(0);
                SessionManager.addSession(key, sessionContext);

                System.out.println("ADDED SESSION: " + key);
            }
        }

        sessionContext.setTimeoutCount(0);

        MessagePack messagePack = (MessagePack) message;
        messagePack.setBody(messagePack.getBody() + " REPLY");
        session.write(messagePack);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        SessionContext sessionContext = SessionManager.getSession(session);
        sessionContext.setTimeoutCount(0);
    }
}
