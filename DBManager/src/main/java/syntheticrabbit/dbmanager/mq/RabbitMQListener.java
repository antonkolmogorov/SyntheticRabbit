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

    @RabbitListener(queues = "${syntheticrabbit.queue.createuser}")
    public Object listenCreateUserQueue(User user) {
        return userRepository.save(user);
    }

    @RabbitListener(queues = "${syntheticrabbit.queue.getuser}")
    public Object listenGetUserQueue(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
