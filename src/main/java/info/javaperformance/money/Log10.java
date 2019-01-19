/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.javaperformance.money;

/**
 *
 * @author ratcash
 */
public class Log10 {

    public static int log10(long num) {
        return log10Quick(num);
    }

    // this is the fastest implementation Taken from https://www.baeldung.com/java-number-of-digits-in-int
    public static int log10Quick(long number) {
        int res = 0;
        if (number >= 10000000000000000l) {
            res += 16;
            number /= 10000000000000000l;
        }
        if (number >= 100000000) {
            res += 8;
            number /= 100000000;
        }
        if (number >= 10000) {
            res += 4;
            number /= 10000;
        }
        if (number >= 100) {
            res += 2;
            number /= 100;
        }
        if (number >= 10) {
            res += 1;
        }
        return res;
    }

    static int log10Slow(long num) {
        if (num == 0) {
            return 0;
        }
        return (int) (Math.log10(num));
    }

    // taken from GUAVA.LongMath class
    public static int log10Floor(long x) {
        /*
        * Based on Hacker's Delight Fig. 11-5, the two-table-lookup, branch-free implementation.
        *
        * The key idea is that based on the number of leading zeros (equivalently, floor(log2(x))), we
        * can narrow the possible floor(log10(x)) values to two. For example, if floor(log2(x)) is 6,
        * then 64 <= x < 128, so floor(log10(x)) is either 1 or 2.
         */
        int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        /*
        * y is the higher of the two possible values of floor(log10(x)). If x < 10^y, then we want the
        * lower of the two possible values, or y - 1, otherwise, we want y.
         */
        return y - lessThanBranchFree(x, powersOf10[y]);
    }

    // maxLog10ForLeadingZeros[i] == floor(log10(2^(Long.SIZE - i)))
    static final byte[] maxLog10ForLeadingZeros = {
        19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12,
        12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3,
        3, 2, 2, 2, 1, 1, 1, 0, 0, 0
    };

    static final long[] powersOf10 = {
        1L,
        10L,
        100L,
        1000L,
        10000L,
        100000L,
        1000000L,
        10000000L,
        100000000L,
        1000000000L,
        10000000000L,
        100000000000L,
        1000000000000L,
        10000000000000L,
        100000000000000L,
        1000000000000000L,
        10000000000000000L,
        100000000000000000L,
        1000000000000000000L
    };

    static int lessThanBranchFree(long x, long y) {
        // Returns the sign bit of x - y.
        return (int) (~ ~(x - y) >>> (Long.SIZE - 1));
    }
}
