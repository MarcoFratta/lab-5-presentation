package it.unibo.ds.lab.presentation.server;

import com.google.gson.Gson;
import it.unibo.ds.presentation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

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

    private Request<?> unmarshallRequest(Socket socket) throws IOException {
        try {
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return gson.fromJson(reader, Request.class);
        } finally {
            socket.shutdownInput();
        }
    }

    private Response<?> computeResponse(Request<?> invocation) {
        try {
            switch (invocation.getMethod()) {
                case "authorize":
                    var token = delegate.authorize((Credentials) invocation.getArgument());
                    return new Response<>(Response.Status.OK, "ok", token);
                case "register":
                    delegate.register((User) invocation.getArgument());
                    return new Response<>(Response.Status.OK, "ok");
                default:
                    return new Response<>(Response.Status.BAD_CONTENT, "no such method: " + invocation.getMethod());
            }
        } catch (BadContentException e) {
            return new Response<>(Response.Status.BAD_CONTENT, e.getMessage());
        } catch (WrongCredentialsException e) {
            return new Response<>(Response.Status.WRONG_CREDENTIALS, e.getMessage());
        } catch (ConflictException e) {
            return new Response<>(Response.Status.CONFLICT, e.getMessage());
        }
    }

    private void marshallResponse(Socket socket, Response<?> response) throws IOException {
        try {
            var writer = new OutputStreamWriter(socket.getOutputStream());
            gson.toJson(response, writer);
        } finally {
            socket.shutdownOutput();
        }
    }
}
