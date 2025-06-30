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

    public ServerSideCalculatorStub(final Socket socket) {
        this.ephemeralSocket = Objects.requireNonNull(socket);
    }

    @Override
    public void run() {
        try (this.ephemeralSocket) {
            final var invocation = this.unmarshallInvocation(this.ephemeralSocket);
            final var result = this.computeResult(invocation);
            this.marshallResult(this.ephemeralSocket, result);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private CalculatorInvocation unmarshallInvocation(final Socket socket) throws IOException {
        try {
            final var input = new DataInputStream(socket.getInputStream());
            final String methodName = input.readUTF();
            final double firstArg = input.readDouble();
            final int realLength = input.readInt();
            final double[] otherArgs = new double[realLength];
            for (int i = 0; i < realLength; i++) {
                otherArgs[i] = input.readDouble();
            }
            return new CalculatorInvocation(methodName, firstArg, otherArgs);
        } finally {
            socket.shutdownInput();
        }
    }

    private CalculatorResult computeResult(final CalculatorInvocation invocation) {
        double res = 0;
        try {
            switch (invocation.getMethod()) {
                case "sum":
                    res = this.delegate.sum(invocation.getFirstArg(), invocation.getOtherArgs());
                    break;
                case "subtract":
                    res = this.delegate.subtract(invocation.getFirstArg(), invocation.getOtherArgs());
                    break;
                case "multiply":
                    res = this.delegate.multiply(invocation.getFirstArg(), invocation.getOtherArgs());
                    break;
                case "divide":
                    res = this.delegate.divide(invocation.getFirstArg(), invocation.getOtherArgs());
                    break;
                default:
                    return new CalculatorResult(CalculatorResult.BAD_CONTENT, null);
            }
            return new CalculatorResult(CalculatorResult.SUCCESS, res);
        } catch (final ArithmeticException e) {
            return new CalculatorResult(CalculatorResult.DIVIDE_BY_ZERO, null);
        } catch (final Exception e) {
            return new CalculatorResult(CalculatorResult.INTERNAL_SERVER_ERROR, null);
        }
    }

    private void marshallResult(final Socket socket, final CalculatorResult result) throws IOException {
        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(result.getStatus());
        if (result.getStatus() == CalculatorResult.SUCCESS) {
            output.writeDouble(result.getResult());
        }
        socket.shutdownOutput();
    }
}
