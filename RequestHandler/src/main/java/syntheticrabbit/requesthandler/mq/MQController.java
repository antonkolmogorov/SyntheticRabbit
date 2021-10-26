package syntheticrabbit.requesthandler.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import syntheticrabbit.requesthandler.dto.UserDto;

@Component
@RequiredArgsConstructor
public class MQController {

    private static final ParameterizedTypeReference<UserDto> RESPONSE_TYPE =
            ParameterizedTypeReference.forType(UserDto.class);

    private final AmqpTemplate template;

    @Value("${syntheticrabbit.queue.getuser}")
    private String getUserQueue;

    @Value("${syntheticrabbit.queue.createuser}")
    private String createUserQueue;

    public UserDto getUserById(Long id) {
        return template.convertSendAndReceiveAsType(getUserQueue, id, RESPONSE_TYPE);
    }

    public UserDto createUser(UserDto user) {
        return template.convertSendAndReceiveAsType(createUserQueue, user, RESPONSE_TYPE);
    }
}