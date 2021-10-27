package it.unibo.ds.presentation;

public class EmptyResponse extends AuthResponse<Void> {
    public EmptyResponse() {
    }

    public EmptyResponse(Status status, String message) {
        super(status, message);
    }
}
