package highest.pure.mina.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;

public class SessionContext {

    private final static String TIMEOUT = "TIMEOUT";

    private final static String SESSION = "SESSION";

    public final static int MAX_TIMEOUT_COUNT = 5;

    private IoSession session;

    public SessionContext(IoSession session) {
        this.session = session;
    }

    public IoSession getSession() {
        return session;
    }

    public Object getData(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return session.getAttribute(key);
    }

    public void setData(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        session.setAttribute(key, value);
    }

    public Integer getTimeoutCount() {
        return (Integer) getData(TIMEOUT);
    }

    public void setTimeoutCount(int count) {
        setData(TIMEOUT, new Integer(count));
    }

    public String getSessionKey() {
        return (String) getData(SESSION);
    }

    public void setSessionKey(String sessionKey) {
        setData(SESSION, sessionKey);
    }
}
