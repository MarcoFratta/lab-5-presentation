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
        try {
            var input = new DataInputStream(socket.getInputStream());
            var method = input.readUTF();
            var argsLength = input.readInt();

            if (argsLength <= 0) return null;

            var first = input.readDouble();
            var others = new double[argsLength - 1];
            for (int i = 1; i < argsLength; i++) {
                others[i - 1] = input.readDouble();
            }

            return new CalculatorInvocation(method, first, others);
        } finally {
            socket.shutdownInput();
        }
    }

    private CalculatorResult computeResult(CalculatorInvocation invocation) {
        if (invocation == null) return CalculatorResult.malformedRequest();
        try {
            switch (invocation.getMethod()) {
                case "sum":
                    return CalculatorResult.success(delegate.sum(invocation.getFirstArg(), invocation.getOtherArgs()));
                case "subtract":
                    return CalculatorResult.success(delegate.subtract(invocation.getFirstArg(), invocation.getOtherArgs()));
                case "multiply":
                    return CalculatorResult.success(delegate.multiply(invocation.getFirstArg(), invocation.getOtherArgs()));
                case "divide":
                    return CalculatorResult.success(delegate.divide(invocation.getFirstArg(), invocation.getOtherArgs()));
                default:
                    return CalculatorResult.malformedRequest();
            }
        } catch (ArithmeticException e) {
            return CalculatorResult.divideByZero();
        }
    }

    private void marshallResult(Socket socket, CalculatorResult result) throws IOException {
        try {
            var output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(result.getStatus());
            if (result.getResult() != null) {
                output.writeDouble(result.getResult());
            }
            output.flush();
        } finally {
            socket.shutdownOutput();
        }
    }
}
