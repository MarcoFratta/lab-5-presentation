package it.unibo.ds.lab.presentation.server;

import it.unibo.ds.presentation.Calculator;
import it.unibo.ds.presentation.LocalCalculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ServerSideCalculatorStub extends Thread {

    private final Calculator delegate = new LocalCalculator();
    private final Socket ephemeralSocket;

    public ServerSideCalculatorStub(Socket socket) {
        this.ephemeralSocket = Objects.requireNonNull(socket);
    }

    @Override
    public void run() {
        try (ephemeralSocket) {
            var invocation = unmarshallInvocation(ephemeralSocket);
            var result = computeResult(invocation);
            marshallResult(ephemeralSocket, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CalculatorInvocation unmarshallInvocation(Socket socket) throws IOException {
        throw new Error("not implemented");
    }

    private CalculatorResult computeResult(CalculatorInvocation invocation) {
        throw new Error("not implemented");
    }

    private void marshallResult(Socket socket, CalculatorResult result) throws IOException {
        throw new Error("not implemented");
    }
}
