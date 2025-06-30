package it.unibo.ds.lab.presentation.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import it.unibo.ds.presentation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class ServerSideAuthenticatorStub extends Thread {

    private final Socket ephemeralSocket;
    private final Authenticator delegate;
    private final Gson gson;

    public ServerSideAuthenticatorStub(final Socket socket, final Authenticator delegate) {
        this.ephemeralSocket = Objects.requireNonNull(socket);
        this.delegate = Objects.requireNonNull(delegate);
        this.gson = GsonUtils.createGson();
    }

    @Override
    public void run() {
        try {
            final Request<?> request = this.unmarshallRequest(this.ephemeralSocket);
            final Response<?> response = this.computeResponse(request);
            this.marshallResponse(this.ephemeralSocket, response);
        } catch (final IOException | BadContentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!this.ephemeralSocket.isClosed()) {
                    this.ephemeralSocket.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Request<?> unmarshallRequest(final Socket socket) throws IOException, BadContentException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final String jsonLine = reader.readLine();
        if (jsonLine == null) {
            throw new IOException("No data received from client");
        }
        try {
            return this.gson.fromJson(jsonLine, Request.class);
        } catch (final Exception e) {
            return null; // Return null if parsing fails, which will be handled in computeResponse
        }
    }

    private Response<?> computeResponse(final Request<?> request) {
        if (request == null) {
            return new EmptyResponse(Response.Status.BAD_CONTENT, "Invalid request format");
        }
        switch (request.getMethod()){
            case "authorize":
                if (request.getArgument() instanceof Credentials) {
                    Credentials credentials = (Credentials) request.getArgument();
                    try {
                        return new AuthorizeResponse(Response.Status.OK, "auth success", this.delegate.authorize(credentials));
                    } catch (final BadContentException e) {
                        return new EmptyResponse(Response.Status.BAD_CONTENT, "Bad content in request");
                    } catch (final WrongCredentialsException e) {
                        return new EmptyResponse(Response.Status.WRONG_CREDENTIALS, "Wrong credentials");
                    }
                } else {
                    return new EmptyResponse(Response.Status.WRONG_CREDENTIALS, "Invalid argument for authorize method");
                }
            case "register":
                if (request.getArgument() instanceof User) {
                    User user = (User) request.getArgument();
                    try {
                        this.delegate.register(user);
                        return new EmptyResponse(Response.Status.OK, "Register success");
                    } catch (final BadContentException e) {
                        return new EmptyResponse(Response.Status.BAD_CONTENT, "Bad content in request");
                    } catch (final ConflictException e) {
                        return new EmptyResponse(Response.Status.CONFLICT, "Conflict in request");
                    }
                } else {
                    return new EmptyResponse(Response.Status.BAD_CONTENT, "Invalid argument for register method");
                }
            default:
                return new EmptyResponse(Response.Status.BAD_CONTENT, "Method not implemented: " + request.getMethod());
        }
    }

    private void marshallResponse(final Socket socket, final Response<?> response) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.gson.toJson(response, writer);
        writer.newLine();
        writer.flush();
    }
}
