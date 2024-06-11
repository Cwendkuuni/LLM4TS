package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

import junit.framework.TestCase;

public class DfpDecTest extends TestCase {

 private DfpField field;
 private DfpDec dfpDec;

 @Before
 public void setUp() {
 field = new Dfp128Field();
 dfpDec = new DfpDec(field);
 }

 @Test
 public void testConstructor() {
 DfpDec dfpDec1 = new DfpDec(field);
 DfpDec dfpDec2 = new DfpDec(field, 1);
 DfpDec dfpDec3 = new DfpDec(field, 123);
 DfpDec dfpDec4 = new DfpDec(field, 1234567890L);
 DfpDec dfpDec5 = new DfpDec(field, 123.45);
 DfpDec dfpDec6 = new DfpDec(dfpDec);
 DfpDec dfpDec7 = new DfpDec(field, "123.45");
 DfpDec dfpDec8 = new DfpDec(field, (byte) 1, (byte) 3);

 assertEquals(0, dfpDec1.compareTo(dfpDec1));
 assertEquals(1, dfpDec2.compareTo(dfpDec1));
 assertEquals(123, dfpDec3.intValue());
 assertEquals(1234567890L, dfpDec4.longValue());
 assertEquals(123.45, dfpDec5.doubleValue(), 0.0001);
 assertEquals(1, dfpDec6.compareTo(dfpDec1));
 assertEquals(1, dfpDec7.compareTo(dfpDec5));
 assertEquals(1, dfpDec8.signum());
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec1 = dfpDec.newInstance();
 DfpDec dfpDec2 = dfpDec.newInstance((byte) 1);
 DfpDec dfpDec3 = dfpDec.newInstance(123);
 DfpDec dfpDec4 = dfpDec.newInstance(1234567890L);
 DfpDec dfpDec5 = dfpDec.newInstance(123.45);
 DfpDec dfpDec6 = dfpDec.newInstance("123.45");
 DfpDec dfpDec7 = dfpDec.newInstance((byte) 1, (byte) 3);

 assertEquals(0, dfpDec1.compareTo(dfpDec1));
 assertEquals(1, dfpDec2.compareTo(dfpDec1));
 assertEquals(123, dfpDec3.intValue());
 assertEquals(1234567890L, dfpDec4.longValue());
 assertEquals(123.45, dfpDec5.doubleValue(), 0.0001);
 assertEquals(1, dfpDec6.compareTo(dfpDec5));
 assertEquals(1, dfpDec7.signum());
 }

 @Test
 public void testGetDecimalDigits() {
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(496, decimalDigits);
 }

 @Test
 public void testRound() {
 Dfp a = field.getPow(10, 1);
 Dfp b = dfpDec.newInstance(a);
 Dfp c = dfpDec.newInstance(a);
 Dfp d = dfpDec.newInstance(a);
 int roundingMode = DfpField.ROUND_HALF_UP;

 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c.round(roundingMode);
 assertEquals(a.add(field.getOne()), c);

 d.round(-roundingMode);
 assertEquals(a.subtract(field.getOne()), d);

 a = field.getPow(10, 2);
 b = dfpDec.newInstance(a);
 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c = dfpDec.newInstance(a);
 c.round(roundingMode);
 assertEquals(a.add(field.getOne()), c);

 d = dfpDec.newInstance(a);
 d.round(-roundingMode);
 assertEquals(a.subtract(field.getOne()), d);

 roundingMode = DfpField.ROUND_DOWN;

 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c.round(roundingMode);
 assertEquals(a, c);

 d.round(-roundingMode);
 assertEquals(a.add(field.getOne()), d);

 a = field.getPow(10, 3);
 b = dfpDec.newInstance(a);
 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c = dfpDec.newInstance(a);
 c.round(roundingMode);
 assertEquals(a, c);

 d = dfpDec.newInstance(a);
 d.round(-roundingMode);
 assertEquals(a.add(field.getOne()), d);

 roundingMode = DfpField.ROUND_CEIL;

 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c = dfpDec.newInstance(a);
 c.round(roundingMode);
 assertEquals(a.add(field.getOne()), c);

 d = dfpDec.newInstance(a);
 d.round(-roundingMode);
 assertEquals(a, d);

 roundingMode = DfpField.ROUND_FLOOR;

 dfpDec.round(roundingMode);
 assertEquals(a, dfpDec);

 c.round(roundingMode);
 assertEquals(a, c);

 d = dfpDec.newInstance(a);
 d.round(-roundingMode);
 assertEquals(a.subtract(field.getOne()), d);
 }

 @Test
 public void testNextAfter() {
 Dfp a = field.getPow(10, -3);
 Dfp b = dfpDec.newInstance(a.negate());
 Dfp c = dfpDec.nextAfter(b);

 assertEquals(a, c);
 }

 @Test
 public void testNegativeZero() {
 Dfp a = field.getNegativeZero();
 Dfp b = dfpDec.newInstance(a);
 Dfp c = dfpDec.nextAfter(b);

 assertEquals(field.getZero(), c);
 }

 @Test
 public void testInfinity() {
 Dfp a = field.getInfinity();
 Dfp b = dfpDec.newInstance(a);
 Dfp c = dfpDec.nextAfter(b);

 assertEquals(field.getInfinity(), c);
 }

 @Test
 public void testNaN() {
 Dfp a = field.getNaN();
 Dfp b = dfpDec.newInstance(a);
 Dfp c = dfpDec.nextAfter(b);

 assertEquals(a, c);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInvalidRadixDigits() {
 DfpDec dfpDec1 = new DfpDec(field);
 DfpDec dfpDec2 = new DfpDec(new Dfp16Field());
 dfpDec1.newInstance(dfpDec2);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testStringConstructorNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, "-123");
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testStringConstructorNonNumber() {
 DfpDec dfpDec = new DfpDec(field, "abc");
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testByteConstructorNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, (byte) -123);
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testIntConstructorNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -123);
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testLongConstructorNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -1234567890123L);
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testDoubleConstructorNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -123.45);
 fail("Expected MathIllegalArgumentException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testStringConstructorLengthGreaterThanRadixDigits() {
 DfpDec dfpDec = new DfpDec(field, "123456789012345678901234567890");
 fail("Expected MathIllegalArgumentException");
 }
}