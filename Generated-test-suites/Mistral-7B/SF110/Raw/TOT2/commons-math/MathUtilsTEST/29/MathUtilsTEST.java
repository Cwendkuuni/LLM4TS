package org.junit.test;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = 12345; // any valid hash code
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithNegativeSignAndPositiveMagnitude() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testNormalizeAngleWithValidInputs() {
        double a = 3.14;
        double center = 0.0;
        double expected = -3.14; // result will depend on the defined TWO_PI value in MathUtils class
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFiniteWithNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFiniteWithInf() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testHashArrayOfDoubles() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = 12345; // any valid hash code
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignIntWithNegativeSignAndPositiveMagnitude() {
        int magnitude = 5;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntWithPositiveSignAndPositiveMagnitude() {
        int magnitude = 5;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithNegativeMagnitudeAndPositiveSign() {
        int magnitude = -5;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithNegativeMagnitudeAndNegativeSign() {
        int magnitude = -5;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFiniteWithNullArray() {
        double[] values = null;
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteWithFiniteArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaNInArray() {
        double[] values = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfInArray() {
        double[] values = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNotNullWithNullPattern() {
        Localizable pattern = null;
        MathUtils.checkNotNull(new Object(), pattern, new Object[0]);
    }
}