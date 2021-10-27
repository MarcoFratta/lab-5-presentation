package it.unibo.ds.lab.presentation.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unibo.ds.presentation.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSideAuthenticator implements Authenticator {

    private final InetSocketAddress server;
    private final Gson gson = GsonUtils.createGson();

    public ClientSideAuthenticator(String host, int port) {
        this.server = new InetSocketAddress(host, port);
    }

    @Override
    public void register(User user) throws BadContentException, ConflictException {
        try {
            rpc(new TypeToken<AuthResponse<User>>(){}, "register", user);
        } catch (WrongCredentialsException e) {
            throw new IllegalStateException("Inconsistent behaviour of server: unexpected WrongCredentialsException", e);
        }
    }

    @Override
    public Token authorize(Credentials credentials) throws BadContentException, WrongCredentialsException {
        try {
            return rpc(new TypeToken<>(){}, "authorize", credentials);
        } catch (ConflictException e) {
            throw new IllegalStateException("Inconsistent behaviour of server: unexpected ConflictException", e);
        }
    }

    private <T> T rpc(TypeToken<AuthResponse<T>> typeToken, String method, Object... args) throws BadContentException, ConflictException, WrongCredentialsException {
        try (var socket = new Socket()) {
            socket.connect(server);
            marshallRequest(socket, method, args);
            return unmarshallResponse(socket, typeToken);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void marshallRequest(Socket socket, String method, Object... args) throws IOException {
        try {
            var writer = new OutputStreamWriter(socket.getOutputStream());
            var request = new AuthRequest(method, args);
            gson.toJson(request, writer);
        } finally {
            socket.shutdownOutput();
        }
    }

    private <T> T unmarshallResponse(Socket socket, TypeToken<AuthResponse<T>> typeToken) throws IOException, BadContentException, ConflictException, WrongCredentialsException {
        try {
            var reader = new InputStreamReader(socket.getInputStream());
            AuthResponse<T> response = gson.fromJson(reader, typeToken.getType());
            switch (response.getStatus()) {
                case OK:
                    return response.getResult();
                case BAD_CONTENT:
                    throw new BadContentException(response.getMessage());
                case CONFLICT:
                    throw new ConflictException(response.getMessage());
                case WRONG_CREDENTIALS:
                    throw new WrongCredentialsException(response.getMessage());
                default:
                    throw new IllegalStateException("This should never happen");
            }
        } finally {
            socket.shutdownInput();
        }
    }
}
