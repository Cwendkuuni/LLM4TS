package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.exception.ArithmeticException;
import org.apache.commons.math3.exception.DivisionByZeroException;
import org.apache.commons.math3.exception.IEEEArithmeticException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testConstructorWithFactory() {
        DfpField factory = new Dfp128Field();
        DfpDec dfpDec = new DfpDec(factory);
        assertNotNull(dfpDec);
    }

    @Test
    public void testConstructorWithFactoryAndDouble() {
        DfpField factory = new Dfp128Field();
        DfpDec dfpDec = new DfpDec(factory, 3.14);
        assertEquals(3.14, dfpDec.doubleValue(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithFactoryAndNegativeDecimalDigits() {
        DfpField factory = new Dfp128Field();
        new DfpDec(factory, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithFactoryAndNaN() {
        DfpField factory = new Dfp128Field();
        new DfpDec(factory, Double.NaN);
    }

    @Test
    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        DfpDec newInstance = dfpDec.newInstance();
        assertSame(dfpDec, newInstance);
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        DfpDec newInstance = dfpDec.newInstance((byte) 1);
        assertNotSame(dfpDec, newInstance);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        DfpDec newInstance = dfpDec.newInstance(42);
        assertNotSame(dfpDec, newInstance);
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        DfpDec newInstance = dfpDec.newInstance(42L);
        assertNotSame(dfpDec, newInstance);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        DfpDec dfp = new DfpDec(new Dfp128Field(), 3.14);
        DfpDec newInstance = dfpDec.newInstance(dfp);
        assertEquals(3.14, newInstance.doubleValue(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithDfpWithDifferentRadixDigits() {
        DfpField factory1 = new Dfp128Field();
        DfpDec dfpDec1 = new DfpDec(factory1);
        DfpField factory2 = new Dfp64Field();
        DfpDec dfp = new DfpDec(factory2, 3.14);
        DfpDec newInstance = dfpDec1.newInstance(dfp);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field());
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(49, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 1.5);
        int actual = dfpDec.round(0);
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundWithPositiveNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 1.5);
        int actual = dfpDec.round(1);
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundWithNegativeNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), -1.5);
        int actual = dfpDec.round(1);
        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundWithBigNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 12345678.987654321);
        int actual = dfpDec.round(2);
        int expected = 12345678;
        assertEquals(expected, actual);
    }

    @Test(expected = ArithmeticException.class)
    public void testRoundWithSubnormalNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 0.000000000000000123456789);
        dfpDec.round(0);
    }

    @Test(expected = ArithmeticException.class)
    public void testRoundWithDenormalizedNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 0.00000000000000123456789);
        dfpDec.round(0);
    }

    @Test(expected = IEEEArithmeticException.class)
    public void testRoundWithNaN() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), Double.NaN);
        dfpDec.round(0);
    }

    @Test(expected = ArithmeticException.class)
    public void testRoundWithInfinity() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), Double.POSITIVE_INFINITY);
        dfpDec.round(0);
    }

    @Test(expected = ArithmeticException.class)
    public void testRoundWithNegativeInfinity() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), Double.NEGATIVE_INFINITY);
        dfpDec.round(0);
    }

    @Test(expected = NumberFormatException.class)
    public void testStringConstructorWithInvalidString() {
        new DfpDec(new Dfp128Field(), "abcd");
    }

    @Test
    public void testStringConstructorWithValidString() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), "123.456789");
        assertEquals("123.456789", dfpDec.toString());
    }

    @Test
    public void testStringConstructorWithExponentialNotation() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), "123.456e+05");
        assertEquals("123456.0", dfpDec.toString());
    }

    @Test
    public void testStringConstructorWithExponentialNegativeNotation() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), "123.456e-05");
        assertEquals("0.0123456", dfpDec.toString());
    }

    @Test
    public void testStringConstructorWithNegativeNumber() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), "-123.456789");
        assertEquals("-123.456789", dfpDec.toString());
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec1 = new DfpDec(new Dfp128Field(), 1.23);
        DfpDec dfpDec2 = dfpDec1.nextAfter(new DfpDec(new Dfp128Field(), 1.24));
        assertEquals(1.24, dfpDec2.doubleValue(), 0.0001);
    }

    @Test
    public void testNextAfterWithEqualNumbers() {
        DfpDec dfpDec1 = new DfpDec(new Dfp128Field(), 1.23);
        DfpDec dfpDec2 = dfpDec1.nextAfter(dfpDec1);
        assertEquals(1.23, dfpDec2.doubleValue(), 0.0001);
    }

    @Test(expected = DivisionByZeroException.class)
    public void testNextAfterWithDivisionByZero() {
        DfpDec dfpDec = new DfpDec(new Dfp128Field(), 0.0);
        dfpDec.nextAfter(new DfpDec(new Dfp128Field(), 0.0));
    }
}