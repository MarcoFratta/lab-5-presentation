package it.unibo.ds.lab.presentation.server;

import it.unibo.ds.presentation.Authenticator;
import it.unibo.ds.presentation.LocalAuthenticator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class ServerSideAuthenticatorService extends Thread {

    private final ServerSocket serverSocket;
    private volatile boolean shouldTerminate = false;
    private final Authenticator localAuthenticator = new LocalAuthenticator();

    public ServerSideAuthenticatorService(final int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (!this.shouldTerminate) {
            try {
                final var socket = this.serverSocket.accept();
                final var handler = new ServerSideAuthenticatorStub(socket, this.localAuthenticator);
                handler.start();
            } catch (final SocketException e) {
                if (e.getMessage().contains("closed")) {
                    // silently ignores
                } else {
                    e.printStackTrace();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        this.shouldTerminate = true;
        try {
            this.serverSocket.close();
        } catch (final IOException e) {
            // silently ignores
        }
    }
}
