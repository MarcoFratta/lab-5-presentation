package it.unibo.ds.lab.presentation.server;

import com.google.gson.Gson;
import it.unibo.ds.presentation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerSideAuthenticatorStub extends Thread {

    private final Authenticator delegate = new LocalAuthenticator();
    private final Socket ephemeralSocket;
    private final Gson gson = GsonUtils.createGson();

    public ServerSideAuthenticatorStub(Socket socket) {
        this.ephemeralSocket = Objects.requireNonNull(socket);
    }

    @Override
    public void run() {
        try (ephemeralSocket) {
            var request = unmarshallRequest(ephemeralSocket);
            var response = computeResponse(request);
            marshallResponse(ephemeralSocket, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AuthRequest<?> unmarshallRequest(Socket socket) throws IOException {
        try {
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return gson.fromJson(reader, AuthRequest.class);
        } finally {
            socket.shutdownInput();
        }
    }

    private AuthResponse<?> computeResponse(AuthRequest<?> invocation) {
        try {
            switch (invocation.getMethod()) {
                case "authorize":
                    var token = delegate.authorize((Credentials) invocation.getArgument());
                    return new AuthResponse<>(AuthResponse.Status.OK, "ok", token);
                case "register":
                    delegate.register((User) invocation.getArgument());
                    return new AuthResponse<>(AuthResponse.Status.OK, "ok");
                default:
                    return new AuthResponse<>(AuthResponse.Status.BAD_CONTENT, "no such method: " + invocation.getMethod());
            }
        } catch (BadContentException e) {
            return new AuthResponse<>(AuthResponse.Status.BAD_CONTENT, e.getMessage());
        } catch (WrongCredentialsException e) {
            return new AuthResponse<>(AuthResponse.Status.WRONG_CREDENTIALS, e.getMessage());
        } catch (ConflictException e) {
            return new AuthResponse<>(AuthResponse.Status.CONFLICT, e.getMessage());
        }
    }

    private void marshallResponse(Socket socket, AuthResponse<?> response) throws IOException {
        try {
            var writer = new OutputStreamWriter(socket.getOutputStream());
            gson.toJson(response, writer);
        } finally {
            socket.shutdownOutput();
        }
    }
}
