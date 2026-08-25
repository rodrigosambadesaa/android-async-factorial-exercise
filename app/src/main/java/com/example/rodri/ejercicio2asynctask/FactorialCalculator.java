package com.example.rodri.ejercicio2asynctask;

import java.math.BigInteger;
import java.util.concurrent.CancellationException;

/** Pure Java factorial implementation, independent from Android UI classes. */
public final class FactorialCalculator {

    public static final int MAX_INPUT = 10_000;

    private FactorialCalculator() {
        // Utility class.
    }

    public static BigInteger factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be non-negative");
        }
        if (number > MAX_INPUT) {
            throw new IllegalArgumentException("number exceeds MAX_INPUT");
        }

        BigInteger result = BigInteger.ONE;
        for (int factor = 2; factor <= number; factor++) {
            if ((factor & 0x7F) == 0 && Thread.currentThread().isInterrupted()) {
                throw new CancellationException("factorial calculation cancelled");
            }
            result = result.multiply(BigInteger.valueOf(factor));
        }
        return result;
    }
}
