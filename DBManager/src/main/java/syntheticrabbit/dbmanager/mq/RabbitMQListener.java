package syntheticrabbit.dbmanager.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import syntheticrabbit.dbmanager.db.User;
import syntheticrabbit.dbmanager.db.UserRepository;

@Component
public class RabbitMQListener {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = "${syntheticrabbit.queue.user}")
    public Object listen(User user) {
        return userRepository.save(user);
    }

    @RabbitListener(queues = "${syntheticrabbit.queue.id}")
    public Object listen(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
