package syntheticrabbit.requesthandler.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import syntheticrabbit.requesthandler.User;

@Component
public class MQController {

    @Autowired
    private AmqpTemplate template;

    @Value("${syntheticrabbit.queue.id}")
    private String idQueue;

    @Value("${syntheticrabbit.queue.user}")
    private String userQueue;

    private Flux<String> get(String queue, Object o) {
        Carrier carrier = new Carrier();
        return Mono.<String>create(s -> {
            carrier.listener = data -> s.success(data);
            carrier.carry(queue, o);
        }).repeat(1);
    }

    public Flux<String> getUserById(long id) {
        Carrier carrier = new Carrier();
//        carrier.compute(template.convertSendAndReceive(idQueue, id));
//        Object user = template.convertSendAndReceive(idQueue, id);
//        return user == null ? "User not found" : user;
        return null;
    }

    public Flux<String> createUser(User user) {

        return null;
    }

    private interface Listener {
        void onResult(String result);
    }

    private class Carrier {
        boolean busy = false;
        Listener listener;

        void carry(String queue, Object o) {
            if (busy) {
                return;
            }
            busy = true;
            new Thread(() -> {
                listener.onResult("Doing something: ");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                String result = template.convertSendAndReceive(queue, o).toString(); // TODO:
                listener.onResult(result);
            }).start();
        }
    }

}
