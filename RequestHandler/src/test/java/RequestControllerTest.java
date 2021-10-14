import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import syntheticrabbit.requesthandler.RequestController;
import syntheticrabbit.requesthandler.UserDto;
import syntheticrabbit.requesthandler.mq.MQController;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RequestControllerTest {

    @Mock
    private MQController controller;

    @InjectMocks
    private RequestController requestController;

    @Captor
    private ArgumentCaptor<UserDto> userDtoCaptor;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @BeforeEach
    public void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateUser_expectReturnFlux() {
        Flux<String> expected = new Flux<String>() {
            @Override
            public void subscribe(CoreSubscriber<? super String> actual) {
            }
        };

        Mockito.when(controller.createUser(ArgumentMatchers.any(UserDto.class))).thenReturn(expected);
        Flux<String> result = requestController.createUser("", "", "", "", "");

        assertEquals(expected, result);
    }

    @Test
    public void whenCreateUser_expectMQControllerArguments() {
        UserDto user = new UserDto("login", "password", "name", "surname", "email");

        requestController.createUser(user.getLogin(), user.getPassword(), user.getName(), user.getSurname(), user.getEmail());

        Mockito.verify(controller).createUser(userDtoCaptor.capture());
        assertEquals(user, userDtoCaptor.getValue());
    }

    @Test
    public void whenGetUser_expectMQControllerArguments() {
        Long id = 1L;

        requestController.get(id);

        Mockito.verify(controller).getUserById(userIdCaptor.capture());
        assertEquals(id, userIdCaptor.getValue().longValue());
    }

    @Test
    public void whenGetUser_expectReturnFlux() {
        Flux<String> expected = new Flux<String>() {
            @Override
            public void subscribe(CoreSubscriber<? super String> actual) {
            }
        };

        Mockito.when(controller.getUserById(ArgumentMatchers.anyLong())).thenReturn(expected);
        Flux<String> result = requestController.get(1L);

        assertEquals(expected, result);
    }

}
