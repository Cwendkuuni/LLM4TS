package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.RoundingException;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructorWithFactory() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory);
        assertEquals(0.0, dfpDec.doubleValueExact(), 0.0);
    }

    @Test
    public void testConstructorWithFactoryAndDouble() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.14);
        assertEquals(3.14, dfpDec.doubleValueExact(), 0.0);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new DfpDec(new DfpField(16), 3.14));
        assertEquals(3.14, dfpDec.doubleValueExact(), 0.0);
    }

    @Test
    public void testNewInstanceWithInvalidRadixDigits() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(new DfpField(8), 3.14));
        assertTrue(dfpDec.isNaN());
        assertEquals(3, dfpDec.nans);
    }

    @Test
    public void testRoundMethodBasic() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.5);
        int roundingMode = DfpField.RoundingMode.ROUND_DOWN;
        int expectedRoundedValue = 3;

        int actualRoundedValue = dfpDec.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);
    }

    @Test
    public void testRoundMethodEdgeCases() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.5);

        // Test rounding mode: ROUND_DOWN
        int roundingModeDown = DfpField.RoundingMode.ROUND_DOWN;
        int expectedRoundedValueDown = 3;
        int actualRoundedValueDown = dfpDec.round(roundingModeDown);
        assertEquals(expectedRoundedValueDown, actualRoundedValueDown);

        // Test rounding mode: ROUND_UP
        int roundingModeUp = DfpField.RoundingMode.ROUND_UP;
        int expectedRoundedValueUp = 4;
        int actualRoundedValueUp = dfpDec.round(roundingModeUp);
        assertEquals(expectedRoundedValueUp, actualRoundedValueUp);

        // Test rounding mode: ROUND_HALF_UP
        int roundingModeHalfUp = DfpField.RoundingMode.ROUND_HALF_UP;
        int expectedRoundedValueHalfUp = 4;
        int actualRoundedValueHalfUp = dfpDec.round(roundingModeHalfUp);
        assertEquals(expectedRoundedValueHalfUp, actualRoundedValueHalfUp);

        // Test rounding mode: ROUND_HALF_DOWN
        int roundingModeHalfDown = DfpField.RoundingMode.ROUND_HALF_DOWN;
        int expectedRoundedValueHalfDown = 3;
        int actualRoundedValueHalfDown = dfpDec.round(roundingModeHalfDown);
        assertEquals(expectedRoundedValueHalfDown, actualRoundedValueHalfDown);

        // Test rounding mode: ROUND_HALF_EVEN
        int roundingModeHalfEven = DfpField.RoundingMode.ROUND_HALF_EVEN;
        int expectedRoundedValueHalfEven = 4;
        int actualRoundedValueHalfEven = dfpDec.round(roundingModeHalfEven);
        assertEquals(expectedRoundedValueHalfEven, actualRoundedValueHalfEven);

        // Test rounding mode: ROUND_HALF_ODD
        int roundingModeHalfOdd = DfpField.RoundingMode.ROUND_HALF_ODD;
        int expectedRoundedValueHalfOdd = 3;
        int actualRoundedValueHalfOdd = dfpDec.round(roundingModeHalfOdd);
        assertEquals(expectedRoundedValueHalfOdd, actualRoundedValueHalfOdd);

        // Test rounding mode: ROUND_CEIL
        int roundingModeCeil = DfpField.RoundingMode.ROUND_CEIL;
        int expectedRoundedValueCeil = 4;
        int actualRoundedValueCeil = dfpDec.round(roundingModeCeil);
        assertEquals(expectedRoundedValueCeil, actualRoundedValueCeil);

        // Test rounding mode: ROUND_FLOOR
        int roundingModeFloor = DfpField.RoundingMode.ROUND_FLOOR;
        int expectedRoundedValueFloor = 3;
        int actualRoundedValueFloor = dfpDec.round(roundingModeFloor);
        assertEquals(expectedRoundedValueFloor, actualRoundedValueFloor);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testRoundMethodWithInvalidRoundingMode() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.5);
        int roundingModeInvalid = 123;
        dfpDec.round(roundingModeInvalid);
    }

    @Test(expected = RoundingException.class)
    public void testRoundMethodWithRoundingOverflow() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.5);
        int roundingMode = DfpField.RoundingMode.ROUND_UP;
        dfpDec.setExp(-32766);
        int actualRoundedValue = dfpDec.round(roundingMode);
    }

    @Test(expected = RoundingException.class)
    public void testRoundMethodWithRoundingUnderflow() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.5);
        int roundingMode = DfpField.RoundingMode.ROUND_UP;
        dfpDec.setExp(-32768);
        int actualRoundedValue = dfpDec.round(roundingMode);
    }

    @Test
    public void testNextAfterMethodBasic() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.14);
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, 3.15));
        assertEquals(3.15, nextAfter.doubleValueExact(), 0.0);
    }

    @Test
    public void testNextAfterMethodWithEqualValues() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.14);
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, 3.14));
        assertEquals(3.14, nextAfter.doubleValueExact(), 0.0);
    }

    @Test
    public void testNextAfterMethodWithLessThan() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.13);
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, 3.14));
        assertEquals(3.14, nextAfter.doubleValueExact(), 0.0);
    }

    @Test
    public void testNextAfterMethodWithGreaterThan() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, 3.15);
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, 3.14));
        assertEquals(3.14, nextAfter.doubleValueExact(), 0.0);
    }

    @Test
    public void testNextAfterMethodWithNaN() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "NaN"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "NaN")));
        assertTrue(nextAfter.isNaN());
        assertEquals(nextAfter.nans, 3);
    }

    @Test
    public void testNextAfterMethodWithNegativeZero() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "-0"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "-0")));
        assertTrue(dfpDec.isNegative());
        assertTrue(nextAfter.isNegative());
        assertEquals(nextAfter.doubleValueExact(), -0.0, 0.0);
    }

    @Test
    public void testNextAfterMethodWithInfinity() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "Inf"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "Inf")));
        assertTrue(dfpDec.isInfinite());
        assertTrue(nextAfter.isInfinite());
        assertEquals(nextAfter.doubleValueExact(), Double.POSITIVE_INFINITY, 0.0);
    }

    @Test
    public void testNextAfterMethodWithNegativeInfinity() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "-Inf"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "-Inf")));
        assertTrue(dfpDec.isNegative());
        assertTrue(nextAfter.isNegative());
        assertEquals(nextAfter.doubleValueExact(), Double.NEGATIVE_INFINITY, 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testNextAfterMethodWithInvalidString() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "abcd"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "efgh")));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNextAfterMethodWithInvalidRadixDigits() {
        DfpField factory = new DfpField(16);
        DfpDec dfpDec = new DfpDec(factory, new DfpDec(factory, "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZ"));
        Dfp nextAfter = dfpDec.nextAfter(new DfpDec(factory, new DfpDec(factory, "1234567890ABCDEFGHJKLMNOPQRSTUVWXYZ")));
    }
}