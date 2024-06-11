package org.apache.commons.math3.dfp.test;

import junit.framework.TestCase;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldConfigurable;
import org.apache.commons.math3.dfp.DfpRoundingMode;

public class DfpDecTest extends TestCase {

 private DfpFieldConfigurable field;
 private DfpDec dec;

 @Before
 public void setUp() {
 field = new DfpField(36);
 dec = new DfpDec(field);
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = new DfpDec(field, 123);
 DfpDec d3 = new DfpDec(field, 0.456);
 DfpDec d4 = new DfpDec(field, "-123.456");
 DfpDec d5 = new DfpDec(field, (byte) 0x80, (byte) 0x3f); // NaN
 DfpDec d6 = new DfpDec(field, (byte) 0x81, (byte) 0x3f); // infinity

 assertEquals(0, d1.compareTo(Dfp.ZERO));
 assertEquals(123, d2.intValueExact());
 assertEquals(0.456, d3.doubleValue(), 0.0001);
 assertEquals(-123.456, d4.toString());
 assertEquals(Double.NaN, d5.doubleValue(), 0.0);
 assertEquals(Double.POSITIVE_INFINITY, d6.doubleValue(), 0.0);
 }

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(dec);
 DfpDec d2 = dec.newInstance(123);
 DfpDec d3 = dec.newInstance(0.456);
 DfpDec d4 = dec.newInstance("-123.456");

 assertEquals(dec, d1);
 assertEquals(123, d2.intValueExact());
 assertEquals(0.456, d3.doubleValue(), 0.0001);
 assertEquals(-123.456, d4.toString());
 }

 @Test
 public void testRounding() {
 dec.setRoundingMode(DfpRoundingMode.CEILING);
 dec.setDecimalDigits(3);
 dec.setValue(123.456);

 assertEquals(124.0, dec.round(2).doubleValue(), 0.001);
 dec.setRoundingMode(DfpRoundingMode.DOWN);
 dec.setDecimalDigits(3);
 dec.setValue(123.556);

 assertEquals(123.0, dec.round(2).doubleValue(), 0.001);
 dec.setRoundingMode(DfpRoundingMode.HALF_DOWN);
 dec.setDecimalDigits(3);
 dec.setValue(123.556);

 assertEquals(123.5, dec.round(2).doubleValue(), 0.001);
 dec.setRoundingMode(DfpRoundingMode.HALF_UP);
 dec.setDecimalDigits(3);
 dec.setValue(123.556);

 assertEquals(124.0, dec.round(2).doubleValue(), 0.001);
 dec.setRoundingMode(DfpRoundingMode.HALF_EVEN);
 dec.setDecimalDigits(3);
 dec.setValue(123.556);

 assertEquals(123.5, dec.round(2).doubleValue(), 0.001);
 dec.setRoundingMode(DfpRoundingMode.HALF_ODD);
 dec.setDecimalDigits(3);
 dec.setValue(123.556);

 assertEquals(124.0, dec.round(2).doubleValue(), 0.001);
 }

 @Test
 public void testNextAfter() {
 DfpDec x1 = dec.newInstance(1.12345);
 DfpDec x2 = dec.newInstance(Double.MIN_VALUE);
 DfpDec x3 = dec.newInstance(Double.MAX_VALUE);
 DfpDec y1, y2, y3;

 // nextAfter(x, +inf)
 y1 = x1.nextAfter(x1);
 assertTrue(y1.isInfinite());
 // nextAfter(x, +x)
 y1 = x1.nextAfter(x1);
 assertEquals(x1, y1);
 // nextAfter(x, NaN)
 y1 = x1.nextAfter(dec.newInstance(Double.NaN));
 assertEquals(x1, y1);
 // nextAfter(+inf, x)
 y1 = x1.nextAfter(dec.newInstance(Double.POSITIVE_INFINITY));
 assertEquals(x1, y1);
 // nextAfter(x, -inf)
 y1 = x1.nextAfter(dec.newInstance(Double.NEGATIVE_INFINITY));
 assertEquals(x1, y1);
 // nextAfter(x, y) where x < y
 y2 = dec.newInstance(1.123454);
 y3 = dec.newInstance(1.123455);
 y1 = x1.nextAfter(y2);
 assertTrue(y1.compareTo(y3) > 0);
 // nextAfter(x, y) where x > y
 y1 = x1.nextAfter(y3);
 assertTrue(y1.compareTo(y2) < 0);
 // nextAfter(x, y) where x = y
 y1 = x1.nextAfter(y1);
 assertEquals(y1, x1);
 // nextAfter(-inf, y)
 y1 = dec.newInstance(Double.NEGATIVE_INFINITY).nextAfter(y2);
 assertTrue(y1.isNegative());
 assertTrue(y1.compareTo(y2) < 0);
 }

 @After
 public void tearDown() {
 dec = null;
 field = null;
 }
}