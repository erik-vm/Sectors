package vm.erik.sectors.exceptions;

public class UserNotFoundException extends ServiceException {

    public UserNotFoundException(String message) {
        super(message);
    }
}