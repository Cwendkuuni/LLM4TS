package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(1, MathUtils.hash(0.0));
        assertEquals(16777216, MathUtils.hash(1.0));
        assertEquals(1592132517, MathUtils.hash(Double.NaN));
        assertEquals(Double.POSITIVE_INFINITY.hashCode(), MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY.hashCode(), MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = new double[]{0.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        int hashCode = MathUtils.hash(values);
        int expectedHashCode = 1671156265; // Arbitrary, just for testing
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(2.1, MathUtils.normalizeAngle(2.6, 0.1), 0.001);
        assertEquals(2.1, MathUtils.normalizeAngle(3.7, 0.1), 0.001);
        assertEquals(3.5, MathUtils.normalizeAngle(3.8, 0.1), 0.001);
        assertEquals(MathUtils.TWO_PI, MathUtils.normalizeAngle(MathUtils.TWO_PI, 0.1), 0.001);
        assertEquals(MathUtils.TWO_PI, MathUtils.normalizeAngle(MathUtils.TWO_PI + 0.1, 0.1), 0.001);
        assertEquals(0.1, MathUtils.normalizeAngle(-0.5, MathUtils.TWO_PI), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.5, MathUtils.reduce(1.6, 1.0, 0.5), 0.001);
        assertEquals(1.5, MathUtils.reduce(2.6, 1.0, 0.5), 0.001);
        assertEquals(1.1, MathUtils.reduce(1.6, 1.5, 0.5), 0.001);
        assertEquals(1.1, MathUtils.reduce(2.6, 1.5, 0.5), 0.001);
        assertEquals(1.0, MathUtils.reduce(1.0, 1.0, 0.5), 0.001);
        assertEquals(1.0, MathUtils.reduce(2.0, 1.0, 0.5), 0.001);
    }

    @Test
    public void testCopySignByte() {
        for (byte sign = Byte.MIN_VALUE; sign <= Byte.MAX_VALUE; sign++) {
            for (byte magnitude = Byte.MIN_VALUE; magnitude <= Byte.MAX_VALUE; magnitude++) {
                try {
                    byte expectedResult = MathUtils.copySign(magnitude, sign);
                    byte result = MathUtils.copySign((byte) magnitude, (byte) sign);
                    assertEquals(expectedResult, result);
                } catch (MathArithmeticException e) {
                    if (magnitude == -128 && sign >= 0) {
                        // Expected
                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void testCopySignShort() {
        for (short sign = Short.MIN_VALUE; sign <= Short.MAX_VALUE; sign++) {
            for (short magnitude = Short.MIN_VALUE; magnitude <= Short.MAX_VALUE; magnitude++) {
                try {
                    short expectedResult = MathUtils.copySign(magnitude, sign);
                    short result = MathUtils.copySign((short) magnitude, (short) sign);
                    assertEquals(expectedResult, result);
                } catch (MathArithmeticException e) {
                    if (magnitude == -32768 && sign >= 0) {
                        // Expected
                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void testCopySignInt() {
        for (int sign = Integer.MIN_VALUE; sign <= Integer.MAX_VALUE; sign++) {
            for (int magnitude = Integer.MIN_VALUE; magnitude <= Integer.MAX_VALUE; magnitude++) {
                try {
                    int expectedResult = MathUtils.copySign(magnitude, sign);
                    int result = MathUtils.copySign((int) magnitude, (int) sign);
                    assertEquals(expectedResult, result);
                } catch (MathArithmeticException e) {
                    if (sign >= 0 && magnitude == Integer.MIN_VALUE) {
                        // Expected
                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void testCopySignLong() {
        for (long sign = Long.MIN_VALUE; sign <= Long.MAX_VALUE; sign++) {
            for (long magnitude = Long.MIN_VALUE; magnitude <= Long.MAX_VALUE; magnitude++) {
                try {
                    long expectedResult = MathUtils.copySign(magnitude, sign);
                    long result = MathUtils.copySign((long) magnitude, (long) sign);
                    assertEquals(expectedResult, result);
                } catch (MathArithmeticException e) {
                    if (sign >= 0 && magnitude == Long.MIN_VALUE) {
                        // Expected
                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        try {
            MathUtils.checkFinite(Double.NaN);
            fail();
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = new double[]{0.0, 1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        MathUtils.checkFinite(values);

        double[] invalidValues = new double[]{Double.NaN, Double.NaN};
        try {
            MathUtils.checkFinite(invalidValues);
            fail();
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);

        Object nullObj = null;
        try {
            MathUtils.checkNotNull(nullObj);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        String pattern = "expected pattern";
        String[] args = new String[]{"arg1", "arg2"};
        MathUtils.checkNotNull(obj, pattern, args);

        Object nullObj = null;
        try {
            MathUtils.checkNotNull(nullObj, pattern, args);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}