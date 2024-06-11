package org.apache.commons.cli.test; // Change the package name if necessary

import com.google.common.base.*;
import com.google.common.annotations.*;
import java.math.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

@GwtCompatible(emulated = true)
public final class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(64)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(17)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(25)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(27)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(29)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.DOWN));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.DOWN));
        assertEquals(7, BigIntegerMath.log2(BigInteger.valueOf(128), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(3), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(17), RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(33), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(65), RoundingMode.UP));
        assertEquals(7, BigIntegerMath.log2(BigInteger.valueOf(128), RoundingMode.UP));
        assertEquals(7, BigIntegerMath.log2(BigInteger.valueOf(129), RoundingMode.UP));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf((long) 1 << 32), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf((long) 1 << 32), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf((long) 1 << 64), RoundingMode.DOWN));
        assertEquals(64, BigIntegerMath.log2(BigInteger.valueOf((long) 1 << 64), RoundingMode.UP));

        try {
            BigIntegerMath.log2(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException expected) {
        }

        try {
            BigIntegerMath.log2(BigInteger.ONE, null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void testLog10() {
        if (!BigIntegerMath.isInitialized()) {
            fail("BigIntegerMath not initialized");
            return;
        }

        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(100000), RoundingMode.DOWN));
        assertEquals(5, BigIntegerMath.log10(BigInteger.valueOf(1000000), RoundingMode.DOWN));
        assertEquals(6, BigIntegerMath.log10(BigInteger.valueOf(10000000), RoundingMode.DOWN));
        assertEquals(7, BigIntegerMath.log10(BigInteger.valueOf(100000000), RoundingMode.DOWN));
        assertEquals(8, BigIntegerMath.log10(BigInteger.valueOf(1000000000), RoundingMode.DOWN));
        assertEquals(9, BigIntegerMath.log10(BigInteger.valueOf(10000000000L), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UP));
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(3), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(4), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(5), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(17), RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log10(BigInteger.valueOf(32), RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log10(BigInteger.valueOf(33), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log10(BigInteger.valueOf(64), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log10(BigInteger.valueOf(65), RoundingMode.UP));
        assertEquals(7, BigIntegerMath.log10(BigInteger.valueOf(128), RoundingMode.UP));
        assertEquals(7, BigIntegerMath.log10(BigInteger.valueOf(129), RoundingMode.UP));
        assertEquals(31, BigIntegerMath.log10(BigInteger.valueOf(2147483647L), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log10(BigInteger.valueOf(2147483647L), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log10(BigInteger.valueOf((long) 1 << 32), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log10(BigInteger.valueOf((long) 1 << 32), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.valueOf((long) 1 << 64), RoundingMode.DOWN));
        assertEquals(64, BigIntegerMath.log10(BigInteger.valueOf((long) 1 << 64), RoundingMode.UP));

        try {
            BigIntegerMath.log10(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException expected) {
        }

        try {
            BigIntegerMath.log10(BigInteger.ONE, null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void testSqrt() {
        try {
            BigIntegerMath.sqrt(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException expected) {
        }

        try {
            BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException expected) {
        }

        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(8), BigIntegerMath.sqrt(BigInteger.valueOf(64), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(8), BigIntegerMath.sqrt(BigInteger.valueOf(64), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(16), BigIntegerMath.sqrt(BigInteger.valueOf(256), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(16), BigIntegerMath.sqrt(BigInteger.valueOf(256), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(65536), BigIntegerMath.sqrt(BigInteger.valueOf(4294967296L), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(65536), BigIntegerMath.sqrt(BigInteger.valueOf(4294967296L), RoundingMode.UP));

        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException expected) {
        }

        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException expected) {
        }

        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException expected) {
        }

        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException expected) {
        }
    }

    // Add more test methods for other methods in the class here

    @BeforeClass
    public static void init() {
        // Initialize BigIntegerMath if necessary
    }

    @AfterClass
    public static void cleanup() {
        // Cleanup BigIntegerMath if necessary
    }

}