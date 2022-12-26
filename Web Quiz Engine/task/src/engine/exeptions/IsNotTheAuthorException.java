package engine.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IsNotTheAuthorException extends ResponseStatusException {
    public IsNotTheAuthorException() {
        super(HttpStatus.FORBIDDEN);
    }
}
