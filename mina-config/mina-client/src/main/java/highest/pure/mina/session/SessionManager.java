package highest.pure.mina.session;

import org.apache.mina.core.session.IoSession;

public class SessionManager {

    private static IoSession session = null;

    public static IoSession getSession() {
        return session;
    }

    public static void setSession(IoSession session) {
        SessionManager.session = session;
    }

    public static void closeSession() {
        if (session != null) {
            session.closeOnFlush();
            session = null;
        }
    }
}
