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
        throw new Error("not implemented");
    }

    private Response<?> computeResponse(Request<?> request) {
        throw new Error("not implemented");
    }

    private void marshallResponse(Socket socket, Response<?> response) throws IOException {
        throw new Error("not implemented");
    }
}
