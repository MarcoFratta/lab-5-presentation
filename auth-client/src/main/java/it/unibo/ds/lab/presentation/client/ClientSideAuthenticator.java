package it.unibo.ds.lab.presentation.client;

import com.google.gson.Gson;
import it.unibo.ds.presentation.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSideAuthenticator implements Authenticator {

    private final InetSocketAddress server;

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

    private <T, R> R rpc(Request<T> request, Class<? extends Response<R>> responseType) throws BadContentException, ConflictException, WrongCredentialsException {
        try (var socket = new Socket()) {
            socket.connect(server);
            marshallRequest(socket, request);
            return unmarshallResponse(socket, responseType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> void marshallRequest(Socket socket, Request<T> request) throws IOException {
        throw new Error("not implemented");
    }

    private <T> T unmarshallResponse(Socket socket, Class<? extends Response<T>> responseType) throws IOException, BadContentException, ConflictException, WrongCredentialsException {
        throw new Error("not implemented");
    }
}
