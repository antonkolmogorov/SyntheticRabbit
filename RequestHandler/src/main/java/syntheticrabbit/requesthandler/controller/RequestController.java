package syntheticrabbit.requesthandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import syntheticrabbit.requesthandler.dto.UserDto;
import syntheticrabbit.requesthandler.mq.MQController;

@RestController
public class RequestController {

    @Autowired
    private MQController controller;

    @GetMapping("/get")
    public UserDto get(@RequestParam Long id) {
        return controller.getUserById(id);
    }

    @GetMapping("/create")
    public UserDto createUser(@RequestParam String login,
                              @RequestParam String password,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String email) {
        return controller.createUser(getUserDto(login, password, name, surname, email));
    }

    @GetMapping("/create0")
    public UserDto createDefault() {
        return createUser("login", "password", "name", "surname", "email");
    }

    private UserDto getUserDto(String login, String password, String name, String surname, String email) {
        UserDto user = new UserDto();
        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        return user;
    }
}