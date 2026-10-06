package com.example.rodri.ejercicio2asynctask;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.CancellationException;

/**
 * Pure Java, cancellation-aware Prime Swing factorial implementation.
 * Products are balanced so BigInteger can use fast large-number arithmetic.
 */
public final class FactorialCalculator {

    public static final int MAX_INPUT = 10_000;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final int CANCELLATION_MASK = 0x7F;

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
        checkCancelled();
        if (number < 2) {
            return ONE;
        }

        int[] primes = oddPrimesUpTo(number);
        BigInteger oddPart = oddFactorial(number, primes);
        return oddPart.shiftLeft(number - Integer.bitCount(number));
    }

    private static BigInteger oddFactorial(int number, int[] primes) {
        checkCancelled();
        if (number < 2) {
            return ONE;
        }

        BigInteger lower = oddFactorial(number >>> 1, primes);
        BigInteger swing = swing(number, primes);
        return lower.multiply(lower).multiply(swing);
    }

    private static BigInteger swing(int number, int[] primes) {
        BigInteger[] factors = new BigInteger[primes.length];
        int count = 0;

        for (int index = 0; index < primes.length; index++) {
            if ((index & CANCELLATION_MASK) == 0) {
                checkCancelled();
            }

            int prime = primes[index];
            if (prime > number) {
                break;
            }

            int exponent = 0;
            long power = prime;
            while (power <= number) {
                if (((number / power) & 1L) != 0L) {
                    exponent++;
                }
                if (power > number / prime) {
                    break;
                }
                power *= prime;
            }

            if (exponent != 0) {
                factors[count++] = BigInteger.valueOf(prime).pow(exponent);
            }
        }

        return balancedProduct(Arrays.copyOf(factors, count), 0, count);
    }

    private static BigInteger balancedProduct(BigInteger[] factors, int from, int to) {
        checkCancelled();
        int length = to - from;
        if (length <= 0) {
            return ONE;
        }
        if (length == 1) {
            return factors[from];
        }

        int middle = from + (length >>> 1);
        return balancedProduct(factors, from, middle)
                .multiply(balancedProduct(factors, middle, to));
    }

    private static int[] oddPrimesUpTo(int limit) {
        boolean[] compositeOdd = new boolean[(limit >>> 1) + 1];
        int squareRoot = (int) Math.sqrt(limit);

        for (int prime = 3; prime <= squareRoot; prime += 2) {
            checkCancelled();
            if (!compositeOdd[prime >>> 1]) {
                long step = (long) prime << 1;
                for (long multiple = (long) prime * prime;
                     multiple <= limit;
                     multiple += step) {
                    compositeOdd[(int) (multiple >>> 1)] = true;
                }
            }
        }

        int count = 0;
        for (int value = 3; value <= limit; value += 2) {
            if (!compositeOdd[value >>> 1]) {
                count++;
            }
        }

        int[] primes = new int[count];
        int index = 0;
        for (int value = 3; value <= limit; value += 2) {
            if (!compositeOdd[value >>> 1]) {
                primes[index++] = value;
            }
        }
        return primes;
    }

    private static void checkCancelled() {
        if (Thread.currentThread().isInterrupted()) {
            throw new CancellationException("factorial calculation cancelled");
        }
    }
}
