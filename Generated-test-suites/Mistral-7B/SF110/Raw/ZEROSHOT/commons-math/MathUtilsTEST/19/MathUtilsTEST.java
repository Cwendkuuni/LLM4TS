package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expectedHash = 1319085553; // just an arbitrary value for testing
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 4.56, 7.89};
        int expectedHash = -1376426457; // just an arbitrary value for testing
        int actualHash = MathUtils.hash(values);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI;
        double center = 0.0;
        double expected = Math.PI / 2.0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);

        a = -Math.PI;
        expected = -Math.PI / 2.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);

        a = Math.PI + 2 * Math.PI;
        expected = Math.PI / 2.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        double a = Double.NaN;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testReduce() {
        double a = 2.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 1.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);

        a = -2.5;
        expected = -1.5;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);

        period = 0.5;
        expected = a;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test
    public void testCheckFiniteWithFinite() {
        double[] values = {1.23, 4.56, 7.89};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullAndPattern() {
        MathUtils.checkNotNull(null, "Invalid object: %s", "test");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithoutPattern() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullWithValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}