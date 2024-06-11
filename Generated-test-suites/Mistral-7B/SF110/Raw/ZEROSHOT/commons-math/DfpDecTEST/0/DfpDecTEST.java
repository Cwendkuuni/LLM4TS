package org.apache.commons.math3.dfp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DfpDecTest {

    @Test
    public void testConstructorWithFactory() {
        DfpField field = new DfpField(16);
        DfpDec d1 = new DfpDec(field);
        assertEquals("DfpDec created with factory", d1.getField(), field);
    }

    @Test
    public void testConstructorWithFactoryAndDouble() {
        DfpField field = new DfpField(16);
        DfpDec d1 = new DfpDec(field, 3.14);
        assertEquals("DfpDec created with factory and double", d1.getField(), field);
        assertEquals("DfpDec value", d1, new DfpDec(field, 3.14));
    }

    @Test
    public void testNewInstance() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance();
        assertEquals("NewInstance returns a new instance of DfpDec", d1, d2);
    }

    @Test
    public void testNewInstanceWithFactory() {
        DfpField field = new DfpField(16);
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance();
        assertEquals("NewInstance returns a new instance with the same field", d1.getField(), d2.getField());
        assertNotSame("NewInstance returns a new instance", d1, d2);
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance(42);
        assertEquals("NewInstance with int returns a new instance with the given value", 42, d2.getValue().intValueExact());
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance(Long.MAX_VALUE);
        assertEquals("NewInstance with long returns a new instance with the given value", Long.MAX_VALUE, d2.getValue().longValueExact());
    }

    @Test
    public void testNewInstanceWithBigInteger() {
        DfpField field = new DfpField(16);
        BigInteger bigInt = new BigInteger("1234567890123456789012345678901234567890");
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance(bigInt);
        assertEquals("NewInstance with BigInteger returns a new instance with the given value", bigInt, d2.getValue().toBigIntegerExact());
    }

    @Test
    public void testNewInstanceWithBigDecimal() {
        DfpField field = new DfpField(16);
        BigDecimal bigDecimal = new BigDecimal("123.4567890123456789012345678901234567890");
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance(bigDecimal);
        assertEquals("NewInstance with BigDecimal returns a new instance with the given value", bigDecimal, d2.getValue());
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = new Dfp(123.45);
        DfpDec d3 = d1.newInstance(d2);
        assertEquals("NewInstance with Dfp returns a new instance with the given value", d2, d3);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpField field = new DfpField(16);
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance("123.45");
        assertEquals("NewInstance with String returns a new instance with the given value", "123.45", d2.toString());
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance((byte) 0x12);
        assertEquals("NewInstance with byte returns a new instance with the given value", 0x12, d2.getValue().intValueExact() & 0xFF);
    }

    @Test
    public void testNewInstanceWithByteSign() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance((byte) 0x80);
        assertEquals("NewInstance with byte sign returns a new instance with the given sign", -1, d2.getSign());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeByteSign() {
        DfpDec d1 = new DfpDec();
        d1.newInstance((byte) 0xFF);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec d1 = new DfpDec();
        int decimalDigits = d1.getDecimalDigits();
        assertEquals("GetDecimalDigits returns the correct number of decimal digits", 64, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec d1 = new DfpDec();
        int roundingMode = RoundingMode.HALF_UP.getCode();
        int result = d1.round(roundingMode);
        assertEquals("Rounding with HALF_UP should return 0", 0, result);

        d1.setValue(new DfpDec(new DfpField(16), 1.5));
        result = d1.round(roundingMode);
        assertEquals("Rounding 1.5 to 1 decimal digit with HALF_UP should return 2", 2, d1.getValue().intValueExact());

        d1.setValue(new DfpDec(new DfpField(16), 1.5));
        result = d1.round(-roundingMode);
        assertEquals("Rounding 1.5 to 1 decimal digit with -HALF_UP should return 1", 1, d1.getValue().intValueExact());

        d1.setValue(new DfpDec(new DfpField(16), 3.5));
        result = d1.round(roundingMode);
        assertEquals("Rounding 3.5 to 1 decimal digit with HALF_UP should return 4", 4, d1.getValue().intValueExact());

        d1.setValue(new DfpDec(new DfpField(16), 3.5));
        result = d1.round(-roundingMode);
        assertEquals("Rounding 3.5 to 1 decimal digit with -HALF_UP should return 3", 3, d1.getValue().intValueExact());

        d1.setValue(new DfpDec(new DfpField(16), 1.5));
        d1.setRoundingMode(RoundingMode.UP);
        result = d1.round(roundingMode);
        assertEquals("Rounding 1.5 to 1 decimal digit with UP should return 2", 2, d1.getValue().intValueExact());

        d1.setValue(new DfpDec(new DfpField(16), 1.5));
        d1.setRoundingMode(RoundingMode.DOWN);
        result = d1.round(roundingMode);
        assertEquals("Rounding 1.5 to 1 decimal digit with DOWN should return 1", 1, d1.getValue().intValueExact());
    }

    @Test
    public void testNewInstanceWithNaN() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance(Dfp.NaN);
        assertEquals("NewInstance with NaN returns a new instance with the same NaN value", Dfp.NaN, d2);
    }

    @Test
    public void testNewInstanceWithInfinity() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = d1.newInstance(Dfp.POSITIVE_INFINITY);
        assertEquals("NewInstance with positive infinity returns a new instance with the same positive infinity value", Dfp.POSITIVE_INFINITY, d2);

        DfpDec d3 = new DfpDec();
        d3.setValue(new DfpDec(new DfpField(16), 1.0 / 0.0));
        DfpDec d4 = d3.newInstance(Dfp.NEGATIVE_INFINITY);
        assertEquals("NewInstance with negative infinity returns a new instance with the same negative infinity value", Dfp.NEGATIVE_INFINITY, d4);
    }

    @Test
    public void testNextAfter() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = new DfpDec(new DfpField(16), 0.5);

        DfpDec result = d1.nextAfter(d2);
        assertEquals("NextAfter of 0.5 with rounding mode HALF_UP should return 1.0", 1.0, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.DOWN);
        result = d1.nextAfter(d2);
        assertEquals("NextAfter of 0.5 with rounding mode DOWN should return 0.5", 0.5, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.UP);
        result = d1.nextAfter(d2);
        assertEquals("NextAfter of 0.5 with rounding mode UP should return 1.0", 1.0, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.HALF_DOWN);
        result = d1.nextAfter(d2);
        assertEquals("NextAfter of 0.5 with rounding mode HALF_DOWN should return 0.5", 0.5, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.HALF_UP);
        result = d1.nextAfter(new DfpDec(new DfpField(16), -0.5));
        assertEquals("NextAfter of -0.5 with rounding mode HALF_UP should return -0.5", -0.5, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.HALF_UP);
        result = d1.nextAfter(new DfpDec(new DfpField(16), -1.5));
        assertEquals("NextAfter of -1.5 with rounding mode HALF_UP should return -1.5", -1.5, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.HALF_UP);
        result = d1.nextAfter(new DfpDec(new DfpField(16), -2.5));
        assertEquals("NextAfter of -2.5 with rounding mode HALF_UP should return -2.5", -2.5, result.getValue().doubleValue());

        d1.setRoundingMode(RoundingMode.HALF_UP);
        result = d1.nextAfter(new DfpDec(new DfpField(16), Double.NEGATIVE_INFINITY));
        assertEquals("NextAfter of negative infinity with rounding mode HALF_UP should return negative infinity", Dfp.NEGATIVE_INFINITY, result);

        d1.setRoundingMode(RoundingMode.HALF_UP);
        result = d1.nextAfter(new DfpDec(new DfpField(16), Double.POSITIVE_INFINITY));
        assertEquals("NextAfter of positive infinity with rounding mode HALF_UP should return positive infinity", Dfp.POSITIVE_INFINITY, result);
    }

    @Test
    public void testNewInstanceWithNaNAndDifferentRadixDigits() {
        DfpDec d1 = new DfpDec();
        DfpDec d2 = new DfpDec(new DfpField(8));
        DfpDec d3 = d1.newInstance(Dfp.NaN);
        DfpDec d4 = d2.newInstance(Dfp.NaN);
        assertNotSame("NewInstance with NaN and different radix digits should not return the same instance", d3, d4);
    }
}