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

    private Flux<String> getFlux(Carrier carrier, String operation) {
        return Mono.<String>create(s -> {
            carrier.listener = data -> s.success(data);
            carrier.carry(operation);
        }).repeat(1);
    }

    public Flux<String> getUserById(long id) {
        Carrier carrier = new Carrier() {
            @Override
            void finish() {
                Object user = template.convertSendAndReceive(idQueue, id);
                String result = user == null ? "user not found" : user.toString();
                listener.onResult(result);
            }
        };
        return getFlux(carrier, String.format("Searching for user with id %d: ", id));
    }

    public Flux<String> createUser(User user) {
        Carrier carrier = new Carrier() {
            @Override
            void finish() {
                Object created = template.convertSendAndReceive(userQueue, user);
                String result = created == null ? "user was not created" : created.toString();
                listener.onResult(result);
            }
        };
        return getFlux(carrier, String.format("Creating user %s: ", user.toString()));
    }

    private interface Listener {
        void onResult(String result);
    }

    private abstract class Carrier {
        boolean busy = false;
        Listener listener;

        void carry(String string) {
            if (busy) {
                return;
            }
            busy = true;
            new Thread(() -> {
                listener.onResult(string);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                finish();
            }).start();
        }

        abstract void finish();
    }

}
