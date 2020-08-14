package highest.pure.mina.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {

    private static ConcurrentHashMap<String, SessionContext> sessions = new ConcurrentHashMap<>();

    private static final AtomicInteger CONNECTIONS_COUNTER = new AtomicInteger(0);

    public static void addSession(String key, SessionContext session) {
        sessions.put(key, session);
        CONNECTIONS_COUNTER.incrementAndGet();
    }

    public static SessionContext getSession(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return sessions.get(key);
    }

    public static SessionContext getSession(IoSession ioSession) {
        SessionContext sessionContext = new SessionContext(ioSession);
        return getSession(sessionContext.getSessionKey());
    }

    public static void replaceSession(String key, SessionContext session) {
        sessions.put(key, session);
    }

    public static void removeSession(String key) {
        sessions.remove(key);
        CONNECTIONS_COUNTER.decrementAndGet();
    }

    public static void removeSession(SessionContext sessionContext) {
        removeSession(sessionContext.getSessionKey());
    }

    public static ConcurrentHashMap<String, SessionContext> getSessions() {
        return sessions;
    }
}
