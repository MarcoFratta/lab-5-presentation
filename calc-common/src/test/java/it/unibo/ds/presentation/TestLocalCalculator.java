package it.unibo.ds.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestLocalCalculator {

    private Calculator calculator;
    private static final double[] numbers = new double[] { 5, 4, 3, 2, 1 };

    @BeforeEach
    public void setup() {
        calculator = new LocalCalculator();
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
