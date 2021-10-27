package it.unibo.ds.lab.presentation;

import it.unibo.ds.lab.presentation.client.ClientSideCalculator;
import it.unibo.ds.lab.presentation.server.ServerSideCalculatorService;
import it.unibo.ds.presentation.Calculator;
import it.unibo.ds.presentation.LocalCalculator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDistributedCalculator {

    private ServerSideCalculatorService service;
    private Calculator calculator;
    private static final double[] numbers = new double[] { 5, 4, 3, 2, 1 };
    private static final int port = 10000;

    @BeforeEach
    public void setup() throws IOException {
        service = new ServerSideCalculatorService(port);
        service.start();

        calculator = new ClientSideCalculator("localhost", port);
    }

    @AfterEach
    public void teardown() throws InterruptedException {
        service.terminate();
        service.join(1000);
    }

    @Test
    public void testSum() {
        for (var x : numbers) {
            assertEquals(x, calculator.sum(0, x));
            assertEquals(x + 1, calculator.sum(1, x));
        }
        assertEquals(15, calculator.sum(0, numbers));
        assertEquals(16, calculator.sum(1, numbers));
    }

    @Test
    public void testSubtract() {
        for (var x : numbers) {
            assertEquals(-x, calculator.subtract(0, x));
            assertEquals(1 - x, calculator.subtract(1, x));
        }
        assertEquals(-15, calculator.subtract(0, numbers));
        assertEquals(-14, calculator.subtract(1, numbers));
    }

    @Test
    public void testMultiply() {
        for (var x : numbers) {
            assertEquals(x, calculator.multiply(1, x));
        }
        assertEquals(120, calculator.multiply(1, numbers));
        assertEquals(0, calculator.multiply(0, numbers));
    }

    @Test
    public void testDivide() {
        for (var x : numbers) {
            assertEquals(1, calculator.divide(x, x));
        }
        assertEquals(1, calculator.divide(120, numbers));
        assertEquals(0, calculator.divide(0, numbers));
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
    }
}
