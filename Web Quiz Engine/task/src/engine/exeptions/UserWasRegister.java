package engine.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserWasRegister extends ResponseStatusException {
    public UserWasRegister() {
        super(HttpStatus.BAD_REQUEST);
    }
}
