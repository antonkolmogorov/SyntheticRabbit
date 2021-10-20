package syntheticrabbit.requesthandler.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import syntheticrabbit.requesthandler.dto.UserDto;

@Component
@RequiredArgsConstructor
public class MQController {

    private final TaskExecutor taskExecutor;

    private final AmqpTemplate template;

    @Value("${syntheticrabbit.queue.getuser}")
    private String getUserQueue;

    @Value("${syntheticrabbit.queue.createuser}")
    private String createUserQueue;

    private Flux<String> getFlux(Carrier carrier, String operation) {
        return Mono.<String>create(s -> {
            carrier.listener = s::success;
            carrier.carry(operation);
        }).repeat(1);
    }

    public Flux<String> getUserById(long id) {
        Carrier carrier = new Carrier() {
            @Override
            void finish() {
                Object user = template.convertSendAndReceive(getUserQueue, id);
                String result = user == null ? "user not found" : user.toString();
                listener.onResult(result);
            }
        };
        return getFlux(carrier, String.format("Searching for user with id %d: ", id));
    }

    public Flux<String> createUser(UserDto user) {
        Carrier carrier = new Carrier() {
            @Override
            void finish() {
                Object created = template.convertSendAndReceive(createUserQueue, user);
                String result = created == null ? "user not created" : created.toString();
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
            listener.onResult(string);
            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            });
        }

        abstract void finish();
    }

}
