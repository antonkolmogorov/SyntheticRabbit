package syntheticrabbit.requesthandler;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    @Autowired
    private AmqpTemplate template;

    @Value("${syntheticrabbit.queue.id}")
    private String idQueue;

    @Value("${syntheticrabbit.queue.user}")
    private String userQueue;

    @RequestMapping("/get")
    public Object createUser(@RequestParam(required = true) Long id) {
        Object user = template.convertSendAndReceive(idQueue, id);
        return user == null ? "no user found" : user;
    }

    @RequestMapping("/create")
    public Object createUser(@RequestParam(required = true) String login,
                             @RequestParam(required = true) String password,
                             @RequestParam(required = true) String name,
                             @RequestParam(required = true) String surname,
                             @RequestParam(required = true) String email) {
        return template.convertSendAndReceive(userQueue, new User(login, password, name, surname, email));
    }

    @RequestMapping("/create0")
    public Object send() {
        return createUser("login", "password", "name", "surname", "email");
    }

}
