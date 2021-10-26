package syntheticrabbit.requesthandler.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String email;
    private Date datetime;
}
