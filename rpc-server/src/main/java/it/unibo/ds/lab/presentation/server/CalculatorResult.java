package it.unibo.ds.lab.presentation.server;

import java.util.Objects;

public class CalculatorResult {
    private final int status;
    private final Double result;

    private CalculatorResult(int status, Double result) {
        this.status = status;
        this.result = result;
    }

    public static CalculatorResult success(double result) {
        return new CalculatorResult(0, result);
    }

    public static CalculatorResult divideByZero() {
        return new CalculatorResult(1, null);
    }

    public static CalculatorResult malformedRequest() {
        return new CalculatorResult(2, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculatorResult that = (CalculatorResult) o;
        return status == that.status && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, result);
    }

    @Override
    public String toString() {
        return "CalculatorResult{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public Double getResult() {
        return result;
    }
}
