package exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserAlreadyExistsException  extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
