package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.apache.commons.math3.dfp.DfpUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DfpDecTest extends TestCase {

    private final DfpFieldFactory factory = new DfpField(10, 12);
    private final DfpField field = factory.getFieldInstance();
    private final DfpDec zero = new DfpDec(field);
    private final DfpDec one = new DfpDec(field, 1);
    private final DfpDec ten = new DfpDec(field, 10);
    private final DfpDec negativeOne = new DfpDec(field, -1);
    private final DfpDec negativeTen = new DfpDec(field, -10);

    public DfpDecTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DfpDecTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructor() {
        DfpDec dfpDec = new DfpDec(field);
        assertEquals(dfpDec.getClass(), DfpDec.class);
    }

    public void testNewInstance() {
        DfpDec dfpDec = new DfpDec(field);
        DfpDec newInstance = dfpDec.newInstance();
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance, dfpDec);
    }

    public void testNewInstanceWithValue() {
        DfpDec dfpDec = new DfpDec(field, 42);
        DfpDec newInstance = dfpDec.newInstance();
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), 42.0, 0.0001);
    }

    public void testNewInstanceWithDfp() {
        DfpDec dfpDec = new DfpDec(field, one);
        DfpDec newInstance = dfpDec.newInstance(one);
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance, dfpDec);
    }

    public void testRound() {
        DfpDec dfpDec = new DfpDec(field, 1.5);
        int rounded = dfpDec.round(0);
        assertEquals(rounded, 0);
        assertEquals(dfpDec.getValue(), 1.5, 0.0001);

        DfpDec dfpDec2 = new DfpDec(field, 1.6);
        int rounded2 = dfpDec2.round(0);
        assertEquals(rounded2, 0);
        assertEquals(dfpDec2.getValue(), 1.6, 0.0001);

        DfpDec dfpDec3 = new DfpDec(field, 1.7);
        int rounded3 = dfpDec3.round(0);
        assertEquals(rounded3, 1);
        assertEquals(dfpDec3.getValue(), 1.7, 0.0001);

        DfpDec dfpDec4 = new DfpDec(field, 1.5);
        int rounded4 = dfpDec4.round(1);
        assertEquals(rounded4, 0);
        assertEquals(dfpDec4.getValue(), 1.5, 0.0001);

        DfpDec dfpDec5 = new DfpDec(field, 1.6);
        int rounded5 = dfpDec5.round(1);
        assertEquals(rounded5, 1);
        assertEquals(dfpDec5.getValue(), 1.6, 0.0001);
    }

    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec(field, -0.5);
        DfpDec nextAfter = dfpDec.nextAfter(zero);
        assertEquals(nextAfter.getClass(), DfpDec.class);
        assertEquals(nextAfter.getValue(), -0.5, 0.0001);

        DfpDec dfpDec2 = new DfpDec(field, 0.5);
        DfpDec nextAfter2 = dfpDec2.nextAfter(one);
        assertEquals(nextAfter2.getClass(), DfpDec.class);
        assertEquals(nextAfter2.getValue(), 1.0, 0.0001);

        DfpDec dfpDec3 = new DfpDec(field, 1.5);
        DfpDec nextAfter3 = dfpDec3.nextAfter(ten);
        assertEquals(nextAfter3.getClass(), DfpDec.class);
        assertEquals(nextAfter3.getValue(), 10.5, 0.0001);

        DfpDec dfpDec4 = new DfpDec(field, 2.5);
        DfpDec nextAfter4 = dfpDec4.nextAfter(ten);
        assertEquals(nextAfter4.getClass(), DfpDec.class);
        assertEquals(nextAfter4.getValue(), 10.5, 0.0001);

        DfpDec dfpDec5 = new DfpDec(field, -2.5);
        DfpDec nextAfter5 = dfpDec5.nextAfter(negativeTen);
        assertEquals(nextAfter5.getClass(), DfpDec.class);
        assertEquals(nextAfter5.getValue(), -9.5, 0.0001);
    }

    public void testNewInstanceWithSignAndNaN() {
        DfpDec dfpDec = new DfpDec(field, 1, 1);
        DfpDec newInstance = dfpDec.newInstance((byte) 1, (byte) 1);
        assertEquals(newInstance.getClass(), DfpDec.class);
    }

    public void testNewInstanceWithString() {
        DfpDec dfpDec = new DfpDec(field, "3.14159265358979323846");
        DfpDec newInstance = dfpDec.newInstance("3.14159265358979323846");
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), 3.14159265358979323846, 0.0001);
    }

    public void testNewInstanceWithStringNaN() {
        DfpDec dfpDec = new DfpDec(field, "nan");
        DfpDec newInstance = dfpDec.newInstance("nan");
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.nans, 3);
    }

    public void testNewInstanceWithInt() {
        DfpDec dfpDec = new DfpDec(field, 42);
        DfpDec newInstance = dfpDec.newInstance(42);
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), 42.0, 0.0001);
    }

    public void testNewInstanceWithLong() {
        DfpDec dfpDec = new DfpDec(field, Long.parseLong("9223372036854775807"));
        DfpDec newInstance = dfpDec.newInstance(Long.parseLong("9223372036854775807"));
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), 9.223372036854775807E18, 0.0001);
    }

    public void testNewInstanceWithDouble() {
        DfpDec dfpDec = new DfpDec(field, Double.POSITIVE_INFINITY);
        DfpDec newInstance = dfpDec.newInstance(Double.POSITIVE_INFINITY);
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), Double.POSITIVE_INFINITY, 0.0001);
    }

    public void testNewInstanceWithDfpAndDifferentRadixDigits() {
        DfpDec dfpDec = new DfpDec(field, one);
        DfpDec dfpDec2 = new DfpDec(new DfpField(5, 12), one);
        DfpDec newInstance = dfpDec.newInstance(dfpDec2);
        assertEquals(newInstance.getClass(), DfpDec.class);
        assertEquals(newInstance.getValue(), 1.0, 0.0001);
    }

    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec(field, 123456789.123456789);
        int decimalDigits = dfpDec.getDecimalDigits();
        assertEquals(decimalDigits, 47);
    }
}