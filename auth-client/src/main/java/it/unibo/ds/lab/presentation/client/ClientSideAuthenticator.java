package it.unibo.ds.lab.presentation.client;

import com.google.gson.Gson;
import it.unibo.ds.presentation.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSideAuthenticator implements Authenticator {

    private final InetSocketAddress server;
    private final Gson gson  = GsonUtils.createGson();

    public ClientSideAuthenticator(final String host, final int port) {
        this.server = new InetSocketAddress(host, port);
    }

    @Override
    public void register(final User user) throws BadContentException, ConflictException {
        try {
            this.rpc(new RegisterRequest(user), EmptyResponse.class);
        } catch (final WrongCredentialsException e) {
            throw new IllegalStateException("Wrong credentials: ", e);
        }
    }

    @Override
    public Token authorize(final Credentials credentials) throws BadContentException, WrongCredentialsException {
        try {
            return this.rpc(new AuthorizeRequest(credentials), AuthorizeResponse.class);
        } catch (final ConflictException e) {
            throw new IllegalStateException("Authorization failed:", e);
        }
    }

    private <T, R> R rpc(final Request<T> request, final Class<? extends Response<R>> responseType) throws BadContentException, ConflictException, WrongCredentialsException {
        try (final Socket socket = new Socket()) {
            socket.connect(this.server);
            try (
                    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                this.marshallRequest(writer, request);
                return this.unmarshallResponse(reader, responseType);
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> void marshallRequest(final BufferedWriter writer, final Request<T> request) throws IOException {
        this.gson.toJson(request, writer);
        writer.newLine(); // Ensure server sees end of JSON message
        writer.flush();
    }

    private <T> T unmarshallResponse(final BufferedReader reader, final Class<? extends Response<T>> responseType)
            throws IOException, BadContentException, ConflictException, WrongCredentialsException {
        final String jsonLine = reader.readLine();
        if (jsonLine == null) {
            throw new IOException("No response received from server");
        }
        final Response<?> response = this.gson.fromJson(jsonLine, Response.class);

        if (response instanceof EmptyResponse) {
            switch (response.getStatus()) {
                case CONFLICT:
                    throw new ConflictException();
                case BAD_CONTENT:
                    throw new BadContentException();
                case WRONG_CREDENTIALS:
                    throw new WrongCredentialsException();
            }
        }
        
        if (!responseType.isInstance(response)) {
            throw new IllegalStateException("Unexpected response type: " + response.getClass());
        }
        
        final Response<T> typedResponse = responseType.cast(response);
        switch (typedResponse.getStatus()) {
            case OK:
                return typedResponse.getResult();
            default:
                throw new IllegalStateException("Unexpected status: " + typedResponse.getStatus());
        }
    }

}
