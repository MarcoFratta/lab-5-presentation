package it.unibo.ds.lab.presentation.client;

import it.unibo.ds.presentation.Calculator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSideCalculator implements Calculator {

    private final InetSocketAddress server;

    public ClientSideCalculator(String host, int port) {
        this.server = new InetSocketAddress(host, port);
    }

    @Override
    public double sum(double first, double... others) {
        return rpc("sum", first, others);
    }

    @Override
    public double subtract(double first, double... others) {
        return rpc("subtract", first, others);
    }

    @Override
    public double multiply(double first, double... others) {
        return rpc("multiply", first, others);
    }

    @Override
    public double divide(double first, double... others) {
        return rpc("divide", first, others);
    }

    private double rpc(String method, double first, double... others) {
        try(var socket = new Socket()) {
            socket.connect(server);
            marshallInvocation(socket, method, first, others);
            return unmarshallResult(socket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void marshallInvocation(Socket socket, String method, double first, double... others) throws IOException {
        try {
            var output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(method);
            output.writeInt(others.length + 1);
            output.writeDouble(first);
            for (var x : others) {
                output.writeDouble(x);
            }
            output.flush();
        } finally {
            socket.shutdownOutput();
        }
    }

    private double unmarshallResult(Socket socket) throws IOException {
        try {
            var input = new DataInputStream(socket.getInputStream());
            var status = input.readInt();
            if (status == 0) {
                return input.readDouble();
            } else if (status == 1) {
                throw new ArithmeticException("Cannot divide by 0.0");
            } else {
                throw new IllegalArgumentException("Invalid request");
            }
        } finally {
            socket.shutdownInput();
        }
    }
}
