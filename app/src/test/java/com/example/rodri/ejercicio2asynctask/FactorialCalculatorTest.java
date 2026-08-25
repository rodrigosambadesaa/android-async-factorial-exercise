package com.example.rodri.ejercicio2asynctask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.math.BigInteger;

public class FactorialCalculatorTest {

    @Test
    public void factorialOfZeroIsOne() {
        assertEquals(BigInteger.ONE, FactorialCalculator.factorial(0));
    }

    @Test
    public void factorialOfOneIsOne() {
        assertEquals(BigInteger.ONE, FactorialCalculator.factorial(1));
    }

    @Test
    public void factorialOfFiveIs120() {
        assertEquals(BigInteger.valueOf(120), FactorialCalculator.factorial(5));
    }

    @Test
    public void factorialOfTwentyIsCorrect() {
        assertEquals(new BigInteger("2432902008176640000"), FactorialCalculator.factorial(20));
    }

    @Test
    public void negativeInputIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> FactorialCalculator.factorial(-1));
    }

    @Test
    public void oversizedInputIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> FactorialCalculator.factorial(FactorialCalculator.MAX_INPUT + 1)
        );
    }
}
