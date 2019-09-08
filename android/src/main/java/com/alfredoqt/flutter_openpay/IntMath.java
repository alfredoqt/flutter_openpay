package com.alfredoqt.flutter_openpay;

/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigInteger;
import java.math.RoundingMode;

public final class IntMath {
    // NOTE: Whenever both tests are cheap and functional, it's faster to use &, | instead of &&, ||

    static final int MAX_SIGNED_POWER_OF_TWO = 1 << (Integer.SIZE - 2);


    /**
     * Returns 1 if {@code x < y} as unsigned integers, and 0 otherwise. Assumes that x - y fits into
     * a signed int. The implementation is branch-free, and benchmarks suggest it is measurably (if
     * narrowly) faster than the straightforward ternary expression.
     */
    static int lessThanBranchFree(int x, int y) {
        // The double negation is optimized away by normal Java, but is necessary for GWT
        // to make sure bit twiddling works as expected.
        return ~~(x - y) >>> (Integer.SIZE - 1);
    }

    @SuppressWarnings("fallthrough")
    public static int log2(int x, RoundingMode mode) {
        switch (mode) {
            case UNNECESSARY:
                // fall through
            case DOWN:
            case FLOOR:
                return (Integer.SIZE - 1) - Integer.numberOfLeadingZeros(x);
            case UP:
            case CEILING:
                return Integer.SIZE - Integer.numberOfLeadingZeros(x - 1);

            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN:
                // Since sqrt(2) is irrational, log2(x) - logFloor cannot be exactly 0.5
                int leadingZeros = Integer.numberOfLeadingZeros(x);
                int cmp = MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros;
                // floor(2^(logFloor + 0.5))
                int logFloor = (Integer.SIZE - 1) - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);

            default:
                throw new AssertionError();
        }
    }

    /** The biggest half power of two that can fit in an unsigned int. */
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = 0xB504F333;

    @SuppressWarnings("fallthrough")
    public static int divide(int p, int q, RoundingMode mode) {
        if (q == 0) {
            throw new ArithmeticException("/ by zero"); // for GWT
        }
        int div = p / q;
        int rem = p - q * div; // equal to p % q

        if (rem == 0) {
            return div;
        }

        /*
         * Normal Java division rounds towards 0, consistently with RoundingMode.DOWN. We just have to
         * deal with the cases where rounding towards 0 is wrong, which typically depends on the sign of
         * p / q.
         *
         * signum is 1 if p and q are both nonnegative or both negative, and -1 otherwise.
         */
        int signum = 1 | ((p ^ q) >> (Integer.SIZE - 1));
        boolean increment;
        switch (mode) {
            case UNNECESSARY:
                // fall through
            case DOWN:
                increment = false;
                break;
            case UP:
                increment = true;
                break;
            case CEILING:
                increment = signum > 0;
                break;
            case FLOOR:
                increment = signum < 0;
                break;
            case HALF_EVEN:
            case HALF_DOWN:
            case HALF_UP:
                int absRem = abs(rem);
                int cmpRemToHalfDivisor = absRem - (abs(q) - absRem);
                // subtracting two nonnegative ints can't overflow
                // cmpRemToHalfDivisor has the same sign as compare(abs(rem), abs(q) / 2).
                if (cmpRemToHalfDivisor == 0) { // exactly on the half mark
                    increment = (mode == HALF_UP || (mode == HALF_EVEN & (div & 1) != 0));
                } else {
                    increment = cmpRemToHalfDivisor > 0; // closer to the UP value
                }
                break;
            default:
                throw new AssertionError();
        }
        return increment ? div + signum : div;
    }

    private IntMath() {}
}