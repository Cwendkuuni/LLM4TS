package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;

import org.junit.Before;
import org.junit.Test;

public class DfpDecTest {

    private DfpFieldFactory fieldFactory;
    private DfpField field;
    private Dfp zero;
    private Dfp one;
    private Dfp two;
    private Dfp three;
    private Dfp four;
    private Dfp five;
    private Dfp ten;
    private Dfp negOne;
    private Dfp negTwo;
    private Dfp negFive;
    private Dfp NaN;
    private Dfp Infinity;
    private Dfp negInfinity;
    private Dfp testDfp;

    @Before
    public void setUp() {
        fieldFactory = new DfpFieldFactory(16);
        field = fieldFactory.getDfpField();
        zero = field.getZero();
        one = new DfpDec(field, 1);
        two = new DfpDec(field, 2);
        three = new DfpDec(field, 3);
        four = new DfpDec(field, 4);
        five = new DfpDec(field, 5);
        ten = new DfpDec(field, 10);
        negOne = new DfpDec(field, -1);
        negTwo = new DfpDec(field, -2);
        negFive = new DfpDec(field, -5);
        NaN = new DfpDec(field, "NaN");
        Infinity = new DfpDec(field, "Inf");
        negInfinity = new DfpDec(field, "-Inf");
    }

    @Test
    public void testNewInstance() {
        DfpDec dfp = new DfpDec(field);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance());
    }

    @Test
    public void testNewInstanceWithByte() {
        DfpDec dfp = new DfpDec(field, (byte) 1);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance((byte) 1));
    }

    @Test
    public void testNewInstanceWithInt() {
        DfpDec dfp = new DfpDec(field, 1);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance(1));
    }

    @Test
    public void testNewInstanceWithLong() {
        DfpDec dfp = new DfpDec(field, 1L);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance(1L));
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpDec dfp = new DfpDec(field, 1.0);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance(1.0));
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpDec dfp = new DfpDec(field, one);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance(one));
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec dfp = new DfpDec(field, "1");
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance("1"));
    }

    @Test
    public void testNewInstanceWithByteSignByteNans() {
        DfpDec dfp = new DfpDec(field, (byte) 1, (byte) 3);
        assertNotNull(dfp);
        assertEquals(dfp, dfp.newInstance((byte) 1, (byte) 3));
    }

    @Test
    public void testGetDecimalDigits() {
        assertEquals(64, new DfpDec(field).getDecimalDigits());
        assertEquals(16, new DfpDec(field, 4).getDecimalDigits());
    }

    @Test
    public void testRound0() {
        testRound(0);
    }

    @Test
    public void testRoundUp() {
        testRound(1);
    }

    @Test
    public void testRoundDown() {
        testRound(2);
    }

    @Test
    public void testRoundHalfUp() {
        testRound(3);
    }

    @Test
    public void testRoundHalfDown() {
        testRound(4);
    }

    @Test
    public void testRoundHalfEven() {
        testRound(5);
    }

    @Test
    public void testRoundHalfOdd() {
        testRound(6);
    }

    @Test
    public void testRoundCeil() {
        testRound(7);
    }

    @Test
    public void testRoundTowardZero() {
        testRound(8);
    }

    private void testRound(int mode) {
        testRoundHelper(mode, new DfpDec(field, 4.5), 5);
        testRoundHelper(mode, new DfpDec(field, 4.4), 4);
        testRoundHelper(mode, new DfpDec(field, 4.6), 5);
        testRoundHelper(mode, new DfpDec(field, 4.5), 5);
        testRoundHelper(mode, new DfpDec(field, 4.5), 4);
        testRoundHelper(mode, new DfpDec(field, 0.5), 1);
        testRoundHelper(mode, new DfpDec(field, 0.5), 0);
        testRoundHelper(mode, new DfpDec(field, 0.4999999999999999), 0);
        testRoundHelper(mode, new DfpDec(field, 0.5000000000000001), 1);
        testRoundHelper(mode, new DfpDec(field, 0.5), 0);
        testRoundHelper(mode, new DfpDec(field, Double.NEGATIVE_INFINITY), Double.NEGATIVE_INFINITY);
        testRoundHelper(mode, new DfpDec(field, Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY);
        testRoundHelper(mode, new DfpDec(field, NaN), NaN);
    }

    private void testRoundHelper(int mode, DfpDec dfp, int expected) {
        DfpDec result = dfp.round(mode);
        assertEquals(expected, result.intValueExact());
        assertEquals(mode, result.getField().getRoundingMode());
    }

    @Test
    public void testNewInstanceWithInvalidRadix() {
        DfpDec dfp = new DfpDec(field, 1, 2);
        assertTrue(dfp.getField().getIEEEFlagsBits() == 1);
        assertNotNull(dfp.getZero());
        assertFalse(dfp.getZero().equals(dfp));
        assertEquals(dfp.getZero().getClass(), DfpDec.class);
        assertEquals(dfp.getZero().getField().getRadixDigits(), 16);
        assertEquals(dfp.getZero().getClassify(), 1);
    }

    @Test
    public void testNextAfter() {
        testNextAfterHelper(one, one, 0);
        testNextAfterHelper(one, two, 1);
        testNextAfterHelper(negOne, one, 1);
        testNextAfterHelper(one, negOne, 1);
        testNextAfterHelper(two, three, 1);
        testNextAfterHelper(three, four, 1);
        testNextAfterHelper(four, five, 1);
        testNextAfterHelper(five, ten, 1);
        testNextAfterHelper(ten, five, 1);
        testNextAfterHelper(five, ten, -1);
        testNextAfterHelper(ten, five, -1);
        testNextAfterHelper(one, zero, 0);
        testNextAfterHelper(zero, one, 0);
        testNextAfterHelper(one, zero, -1);
        testNextAfterHelper(zero, one, -1);
        testNextAfterHelper(one, negOne, 0);
        testNextAfterHelper(negOne, one, 0);
        testNextAfterHelper(one, negOne, -1);
        testNextAfterHelper(negOne, one, -1);
        testNextAfterHelper(negOne, negOne, 0);
        testNextAfterHelper(negOne, negOne, -1);
        testNextAfterHelper(negOne, negOne, 0);
        testNextAfterHelper(negOne, negOne, -1);
        testNextAfterHelper(zero, zero, 0);
        testNextAfterHelper(zero, zero, 1);
        testNextAfterHelper(zero, zero, -1);
        testNextAfterHelper(zero, zero, -1);
        testNextAfterHelper(zero, NaN, 0);
        testNextAfterHelper(NaN, zero, 0);
        testNextAfterHelper(NaN, NaN, 0);
        testNextAfterHelper(zero, Infinity, 0);
        testNextAfterHelper(Infinity, zero, 0);
        testNextAfterHelper(Infinity, Infinity, 0);
        testNextAfterHelper(zero, negInfinity, 0);
        testNextAfterHelper(negInfinity, zero, 0);
        testNextAfterHelper(negInfinity, negInfinity, 0);
        testNextAfterHelper(Infinity, one, 1);
        testNextAfterHelper(Infinity, two, 2);
        testNextAfterHelper(negInfinity, one, -1);
        testNextAfterHelper(negInfinity, two, -2);
        testNextAfterHelper(five, five, 0);
        testNextAfterHelper(five, five, 1);
        testNextAfterHelper(five, five, -1);
        testNextAfterHelper(five, five, -1);
        testNextAfterHelper(five, negFive, 1);
        testNextAfterHelper(negFive, five, -1);
        testNextAfterHelper(five, negFive, 1);
        testNextAfterHelper(five, negFive, 1);
        testNextAfterHelper(negFive, negFive, 0);
        testNextAfterHelper(negFive, negFive, -1);
        testNextAfterHelper(negFive, negFive, 0);
        testNextAfterHelper(negFive, negFive, -1);
        testNextAfterHelper(testDfp, testDfp, 0);
        testNextAfterHelper(testDfp, testDfp, 1);
        testNextAfterHelper(testDfp, testDfp, -1);
        testNextAfterHelper(testDfp, testDfp, -1);
        testNextAfterHelper(testDfp, testDfp.negate(), 0);
        testNextAfterHelper(testDfp.negate(), testDfp, 0);
        testNextAfterHelper(testDfp.negate(), testDfp.negate(), 0);
        testNextAfterHelper(testDfp.negate(), testDfp.negate(), -1);
        testNextAfterHelper(testDfp.negate(), testDfp.negate(), 1);
        testNextAfterHelper(testDfp.negate(), testDfp.negate(), 1);
    }

    private void testNextAfterHelper(DfpDec x, DfpDec y, int expectedSign) {
        DfpDec result = x.nextAfter(y);
        assertEquals(expectedSign == 1 ? 1 : -1, result.sign);
        assertEquals(x, result.prev(1));
        assertEquals(y, result.next(1));
        assertEquals(expectedSign * y.absValueExact(), result.absValueExact());
    }
}