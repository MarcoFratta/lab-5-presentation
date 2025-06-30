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
        try (var socket = new Socket()) {
            socket.connect(server);
            marshallInvocation(socket, method, first, others);
            return unmarshallResult(socket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void marshallInvocation(Socket socket, String method, double first, double... others) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF(method);
        output.writeDouble(first);
        output.writeInt(others.length);
        for(double o : others){
            output.writeDouble(o);
        }
        socket.shutdownOutput();
    }

    private double unmarshallResult(Socket socket) throws IOException {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            int status = input.readInt();
            switch (status){
                case 0:
                    return input.readDouble();
                case 1:
                    throw new Error("Bad client implementation");
                case 3:
                    throw new ArithmeticException();
                default:
                    throw new IllegalStateException();
            }
        }finally {
            socket.shutdownInput();
        }
    }
}
