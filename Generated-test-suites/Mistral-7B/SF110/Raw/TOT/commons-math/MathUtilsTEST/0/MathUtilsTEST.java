package org.junit.jupiter.api;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {
    @Test
    public void testHashDouble() {
        // Test with positive values
        double value = 1.0;
        int expected = 51; // arbitrary positive hashcode
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);

        // Test with negative value
        value = -1.0;
        expected = -51; // arbitrary negative hashcode
        actual = MathUtils.hash(value);
        assertEquals(expected, actual);

        // Test with special double value (NaN and Infinity)
        double nan = Double.NaN;
        double inf = Double.POSITIVE_INFINITY;
        int nanHash = Double.hashCode(nan);
        int infHash = Double.hashCode(inf);
        int actualNan = MathUtils.hash(nan);
        int actualInf = MathUtils.hash(inf);
        assertEquals(nanHash, actualNan);
        assertEquals(infHash, actualInf);
    }

    @Test
    public void testNormalizeAngle() {
        // Test with positive angles
        double a = 0.0;
        double center = 0.0;
        double expected = 0.0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);

        a = 3.141592653589793; // Pi
        center = 0.0;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);

        // Test with negative angles
        a = -3.141592653589793; // -Pi
        center = 0.0;
        expected = 6.283185307179586; // 2 * Pi
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);

        // Test with angles close to zero
        a = 0.00001;
        center = 0.0;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);

        // Test with angles close to 2 * Pi
        a = 6.283185307179586 - 0.00001;
        center = 0.0;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);

        // Test with angles larger than 2 * Pi
        a = 12.56789;
        center = 6.283185307179586;
        expected = a % (2 * FastMath.PI);
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testReduce() {
        // Test with angles close to 0
        double a = 0.00001;
        double period = 2 * Math.PI;
        double offset = 0.0;
        double expected = a;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);

        // Test with angles close to period
        a = period - 0.00001;
        expected = 0.0;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);

        // Test with angles larger than period
        a = period * 2.0;
        expected = a % period;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);

        // Test with negative angles
        a = -period;
        expected = period - abs(a) % period;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);

        // Test with offset larger than period
        period = Math.PI;
        offset = 2 * period;
        a = Math.PI;
        expected = a - period;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }
}