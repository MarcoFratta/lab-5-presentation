package it.unibo.ds.lab.presentation.client;

import com.google.gson.Gson;
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
            rpc(new RegisterRequest(user), EmptyResponse.class);
        } catch (WrongCredentialsException e) {
            throw new IllegalStateException("Inconsistent behaviour of server: unexpected WrongCredentialsException", e);
        }
    }

    @Override
    public Token authorize(Credentials credentials) throws BadContentException, WrongCredentialsException {
        try {
            return rpc(new AuthorizeRequest(credentials), AuthorizeResponse.class);
        } catch (ConflictException e) {
            throw new IllegalStateException("Inconsistent behaviour of server: unexpected ConflictException", e);
        }
    }

    private <T, R> R rpc(AuthRequest<T> request, Class<? extends AuthResponse<R>> responseType) throws BadContentException, ConflictException, WrongCredentialsException {
        try (var socket = new Socket()) {
            socket.connect(server);
            marshallRequest(socket, request);
            return unmarshallResponse(socket, responseType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> void marshallRequest(Socket socket, AuthRequest<T> request) throws IOException {
        try {
            var writer = new OutputStreamWriter(socket.getOutputStream());
//            var all = gson.toJson(request);
//            writer.append(all);
            gson.toJson(request, writer);
            writer.flush();
        } finally {
            socket.shutdownOutput();
        }
    }

    private <T> T unmarshallResponse(Socket socket, Class<? extends AuthResponse<T>> responseType) throws IOException, BadContentException, ConflictException, WrongCredentialsException {
        try {
            var reader = new InputStreamReader(socket.getInputStream());
            var response = gson.fromJson(reader, responseType);
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
