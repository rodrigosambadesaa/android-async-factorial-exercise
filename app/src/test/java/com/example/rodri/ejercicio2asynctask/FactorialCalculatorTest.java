package com.example.rodri.ejercicio2asynctask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.CancellationException;

public class FactorialCalculatorTest {

    @Test
    public void primeSwingMatchesSequentialReferenceThroughOneThousand() {
        BigInteger expected = BigInteger.ONE;
        for (int n = 0; n <= 1_000; n++) {
            if (n >= 2) {
                expected = expected.multiply(BigInteger.valueOf(n));
            }
            assertEquals("Mismatch at n=" + n, expected, FactorialCalculator.factorial(n));
        }
    }

    @Test
    public void maximumInputHasKnownFingerprint() {
        String value = FactorialCalculator.factorial(FactorialCalculator.MAX_INPUT).toString();
        assertEquals(35_660, value.length());
        assertTrue(value.startsWith("2846259680917054518906413212119868890148"));
    }

    @Test
    public void negativeAndOversizedInputsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> FactorialCalculator.factorial(-1));
        assertThrows(IllegalArgumentException.class,
                () -> FactorialCalculator.factorial(FactorialCalculator.MAX_INPUT + 1));
    }

    @Test
    public void interruptedCalculationIsCancelled() throws InterruptedException {
        Throwable[] failure = new Throwable[1];
        Thread worker = new Thread(() -> {
            Thread.currentThread().interrupt();
            try {
                FactorialCalculator.factorial(FactorialCalculator.MAX_INPUT);
            } catch (Throwable throwable) {
                failure[0] = throwable;
            }
        });
        worker.start();
        worker.join();
        assertTrue(failure[0] instanceof CancellationException);
    }
}
