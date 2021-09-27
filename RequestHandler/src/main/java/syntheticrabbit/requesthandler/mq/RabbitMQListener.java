package syntheticrabbit.requesthandler.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import syntheticrabbit.requesthandler.User;

//@Component
public class RabbitMQListener {

    @RabbitListener(queues = "${syntheticrabbit.queue}")
    public void listen(User o) {
        System.out.println("receiving: " + o);
    }

}
