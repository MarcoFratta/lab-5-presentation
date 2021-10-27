package it.unibo.ds.presentation;

public class RegisterRequest extends AuthRequest<User> {
    public RegisterRequest() {
        this(null);
    }

    public RegisterRequest(User argument) {
        super("register", argument);
    }
}
