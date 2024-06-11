import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private DfpField field = new Dfp128Field();
 private DfpDec zero = new DfpDec(field.getZero());
 private DfpDec one = new DfpDec(field.getOne());
 private DfpDec negOne = new DfpDec(field, (byte) -1);
 private DfpDec ten = new DfpDec(field, 10);
 private DfpDec negativeTen = new DfpDec(field, (byte) -1, 10);
 private DfpDec smallNegativeNumber = new DfpDec(field, (byte) -12345, (byte) 0);
 private DfpDec bigNumber = new DfpDec(field, 123456789012345678901234567890L);

 @Test
 public void testConstructorWithfactory() {
 DfpDec dfpDec = new DfpDec(field);
 assertNotNull(dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndX() {
 DfpDec dfpDec = new DfpDec(field, 1);
 assertEquals(one, dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpDec dfpDec = new DfpDec(field, 1);
 assertEquals(one, dfpDec);

 DfpDec dfpDecInt = new DfpDec(field, 12345);
 DfpDec expected = new DfpDec(field, 12345.0);
 assertEquals(expected, dfpDecInt);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpDec dfpDec = new DfpDec(field, 123456789012345678901234567890L);
 DfpDec expected = new DfpDec(field, 123456789012345678901234567890.0);
 assertEquals(expected, dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(field, 3.14);
 DfpDec expected = new DfpDec(field, 3.14);
 assertEquals(expected, dfpDec);
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(field, new DfpDec(field, 1));
 assertEquals(one, dfpDec);
 }

 @Test
 public void testConstructorWithFactoryAndString() {
 DfpDec dfpDec = new DfpDec(field, "123.456");
 DfpDec expected = new DfpDec(field, 123.456);
 assertEquals(expected, dfpDec);
 }

 @Test
 public void testConstructorWithFactorySignAndNans() {
 DfpDec dfpDec = new DfpDec(field, (byte) -1, (byte) 3);
 assertEquals(negOne, dfpDec);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(field, 10);
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testNewInstanceX() {
 DfpDec dfpDec = new DfpDec(field, 10);
 DfpDec newInstance = dfpDec.newInstance(1);
 assertEquals(new DfpDec(field, 11), newInstance);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(field, 1234.5);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(13, decimalDigits);
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec(field, 123.4567);
 int rounded = dfpDec.round(0);
 assertEquals(0, rounded);

 DfpDec expectedRoundUp = new DfpDec(field, 123.4568);
 int roundedUp = dfpDec.round(DfpField.ROUND_UP);
 assertEquals(expectedRoundUp, dfpDec.newInstance(roundedUp));

 DfpDec expectedRoundDown = new DfpDec(field, 123.4566);
 int roundedDown = dfpDec.round(DfpField.ROUND_DOWN);
 assertEquals(expectedRoundDown, dfpDec.newInstance(roundedDown));
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec = new DfpDec(field, 0.1);
 DfpDec nextAfter = dfpDec.nextAfter(one);
 assertEquals(new DfpDec(field, 0.2), nextAfter);

 DfpDec dfpDecNegative = new DfpDec(field, -0.1);
 DfpDec nextAfterNegative = dfpDecNegative.nextAfter(negOne);
 assertEquals(new DfpDec(field, -0.0), nextAfterNegative);
 }

 @Test
 public void testNewInstanceWithDifferentRadixDigits() {
 DfpDec dfpDec = new DfpDec(field, 10);
 DfpDec dfpDec2 = new DfpDec(field, 8);
 assertNotEquals(dfpDec, dfpDec2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithDifferentRadixDigitsException() {
 DfpDec dfpDec = new DfpDec(field, 10);
 DfpDec dfpDec2 = new DfpDec(field, 5);
 }

 @Test
 public void testPower10() {
 DfpDec tenPower = DfpDec.power10(1);
 assertEquals(ten, tenPower);

 DfpDec tenPowerNegative = DfpDec.power10(-1);
 assertEquals(negativeTen, tenPowerNegative);
 }

 @Test
 public void testPower10K() {
 DfpDec expNegative = new DfpDec(field, (byte) -32767);
 DfpDec pow10KNegative = DfpDec.power10K(expNegative);
 assertEquals(new DfpDec(field, 0), pow10KNegative);

 DfpDec pow10KZero = DfpDec.power10K(zero);
 assertEquals(zero, pow10KZero);
 }

 @Test
 public void testAdd() {
 DfpDec sum = new DfpDec(field, 10).add(new DfpDec(field, 5));
 assertEquals(new DfpDec(field, 15), sum);
 }

 @Test
 public void testSubtract() {
 DfpDec difference = new DfpDec(field, 15).subtract(new DfpDec(field, 10));
 assertEquals(new DfpDec(field, 5), difference);
 }

 @Test
 public void testMultiply() {
 DfpDec product = new DfpDec(field, 10).multiply(new DfpDec(field, 5));
 assertEquals(new DfpDec(field, 50), product);
 }

 @Test
 public void testDivide() {
 DfpDec quotient = new DfpDec(field, 20).divide(new DfpDec(field, 4));
 assertEquals(new DfpDec(field, 5), quotient);
 }

 @Test
 public void testDivideByZero() {
 DfpDec divisor = new DfpDec(field, 0);
 DfpDec dfpDec = new DfpDec(field, 1);
 try {
 dfpDec.divide(divisor);
 fail();
 } catch (ArithmeticException e) {
 // Expected ArithmeticException
 }
 }

 @Test
 public void testDivideByZeroWithIEEEFlags() {
 DfpDec divisor = new DfpDec(field, 0);
 DfpDec dfpDec = new DfpDec(field, 1);
 divisor.getField().setIEEEFlagsBits(1);
 try {
 dfpDec.divide(divisor);
 fail();
 } catch (ArithmeticException e) {
 // Expected ArithmeticException
 }
 }

 @Test
 public void testDivideByZeroWithIEEEFlagsException() {
 DfpDec divisor = new DfpDec(field, 0);
 DfpDec dfpDec = new DfpDec(field, 1);
 divisor.getField().setIEEEFlagsBits(16);
 try {
 dfpDec.divide(divisor);
 fail();
 } catch (ArithmeticException e) {
 // Expected ArithmeticException
 }
 }

 @Test
 public void testLessThan() {
 assertTrue(zero.lessThan(one));
 assertFalse(one.lessThan(one));
 assertFalse(one.lessThan(zero));
 }

 @Test
 public void testEquals() {
 assertTrue(zero.equals(zero));
 assertFalse(zero.equals(one));
 assertTrue(new DfpDec(field, 1).equals(one));
 }

 @Test
 public void testGreaterThan() {
 assertFalse(one.lessThan(zero));
 assertTrue(one.greaterThan(zero));
 assertFalse(one.greaterThan(one));
 }

 @Test
 public void testClassify() {
 assertEquals(0, zero.classify());
 assertEquals(1, one.classify());
 assertEquals(2, negOne.classify());
 assertEquals(3, new DfpDec(field, Double.NaN).classify());
 }

 @Test
 public void testIntLog10() {
 double intLog10Expected = Math.log10(1000.0);
 DfpDec intLog10 = new DfpDec(field).intLog10(1000);
 assertEquals(intLog10Expected, intLog10.getDoubleValueExact(), 0.00001);
 }

 @Test
 public void testDotrap() {
 DfpDec trapResult = new DfpDec(field).dotrap(1, "newInstance", new DfpDec(field, 1), new DfpDec(field, 1));
 assertEquals(new DfpDec(field, 1), trapResult);
 }
}