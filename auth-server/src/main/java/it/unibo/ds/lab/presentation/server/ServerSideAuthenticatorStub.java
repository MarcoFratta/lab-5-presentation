package it.unibo.ds.lab.presentation.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.unibo.ds.presentation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class ServerSideAuthenticatorStub extends Thread {

    private final Socket ephemeralSocket;
    private final Authenticator delegate;
    private final Gson gson = GsonUtils.createGson();

    public ServerSideAuthenticatorStub(Socket socket, Authenticator delegate) {
        this.ephemeralSocket = Objects.requireNonNull(socket);
        this.delegate = Objects.requireNonNull(delegate);
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
        } catch (JsonParseException e) {
            return null;
        } finally {
            socket.shutdownInput();
        }
    }

    private Response<?> computeResponse(Request<?> request) {
        try {
            if (request == null) return new EmptyResponse(Response.Status.BAD_CONTENT, "Malformed request");
            switch (request.getMethod()) {
                case "authorize":
                    var token = delegate.authorize((Credentials) request.getArgument());
                    return new AuthorizeResponse(Response.Status.OK, "ok", token);
                case "register":
                    delegate.register((User) request.getArgument());
                    return new EmptyResponse(Response.Status.OK, "ok");
                default:
                    return new EmptyResponse(Response.Status.BAD_CONTENT, "no such method: " + request.getMethod());
            }
        } catch (BadContentException e) {
            return new EmptyResponse(Response.Status.BAD_CONTENT, e.getMessage());
        } catch (WrongCredentialsException e) {
            return new EmptyResponse(Response.Status.WRONG_CREDENTIALS, e.getMessage());
        } catch (ConflictException e) {
            return new EmptyResponse(Response.Status.CONFLICT, e.getMessage());
        }
    }

    private void marshallResponse(Socket socket, Response<?> response) throws IOException {
        try {
            var writer = new OutputStreamWriter(socket.getOutputStream());
            gson.toJson(response, writer);
            writer.flush();
        } finally {
            socket.shutdownOutput();
        }
    }
}
