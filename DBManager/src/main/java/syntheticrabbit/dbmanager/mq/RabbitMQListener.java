package syntheticrabbit.dbmanager.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import syntheticrabbit.dbmanager.db.User;
import syntheticrabbit.dbmanager.db.UserRepository;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final UserRepository userRepository;

    @RabbitListener(queues = "${syntheticrabbit.queue.createuser}")
    public User listenCreateUserQueue(User user) {
        return userRepository.save(user);
    }

    @RabbitListener(queues = "${syntheticrabbit.queue.getuser}")
    public User listenGetUserQueue(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}