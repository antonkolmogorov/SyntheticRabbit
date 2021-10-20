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
    public String listenCreateUserQueue(User user) {
        return userRepository.save(user).toString();
    }

    @RabbitListener(queues = "${syntheticrabbit.queue.getuser}")
    public String listenGetUserQueue(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? "User not found" : user.toString();
    }

}
