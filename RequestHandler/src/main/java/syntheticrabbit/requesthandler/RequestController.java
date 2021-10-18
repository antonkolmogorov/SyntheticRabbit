package syntheticrabbit.requesthandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import syntheticrabbit.requesthandler.mq.MQController;

@RestController
public class RequestController {

    @Autowired
    private MQController controller;

    @RequestMapping("/get")
    public Flux<String> get(@RequestParam Long id) {
        return controller.getUserById(id);
    }

    @RequestMapping("/create")
    public Flux<String> createUser(@RequestParam String login,
                                   @RequestParam String password,
                                   @RequestParam String name,
                                   @RequestParam String surname,
                                   @RequestParam String email) {
        return controller.createUser(new UserDto(login, password, name, surname, email));
    }

    @RequestMapping("/create0")
    public Flux<String> createDefault() {
        return createUser("login", "password", "name", "surname", "email");
    }

}
