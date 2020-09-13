package highest.pure.mina.service;

import highest.pure.mina.protocol.MessagePack;
import highest.pure.mina.protocol.MessageType;
import highest.pure.mina.session.SessionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ClientService {

    public void start() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);

                        if (SessionManager.getSession() != null) {
                            MessagePack messagePack = new MessagePack(MessageType.COMMAND, "SAMPLE MESSAGE");
                            SessionManager.getSession().write(messagePack);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        new Thread(runnable).start();
    }
}
