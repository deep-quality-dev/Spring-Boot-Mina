package highest.pure.mina.service;

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

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        new Thread(runnable).start();
    }
}
