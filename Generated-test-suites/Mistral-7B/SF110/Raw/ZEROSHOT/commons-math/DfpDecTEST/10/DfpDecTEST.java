import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testDfpDecConstruction() {
 DfpDec d1 = new DfpDec(new DfpField(16));
 DfpDec d2 = new DfpDec(new DfpField(16), 123);
 DfpDec d3 = new DfpDec(new DfpDec(new DfpField(16), 456), 789);
 DfpDec d4 = new DfpDec(new DfpDec(new DfpField(16), 0.123), "3.14159265");
 DfpDec d5 = new DfpDec(new DfpDec(new DfpField(16), -1), 0x7FFF);

 assertEquals("DfpDec constructor with null field should throw an exception",
 expectedException(NullPointerException.class), () -> new DfpDec(null));
 }

 @Test
 public void testNewInstance() {
 DfpDec d = new DfpDec(new DfpField(16), 12345.6789);
 DfpDec newInstance = d.newInstance();
 DfpDec newInstance1 = d.newInstance(123);
 DfpDec newInstance2 = d.newInstance(-256);
 DfpDec newInstance3 = d.newInstance(new DfpDec(new DfpField(16), 0xfedcba98));
 DfpDec newInstance4 = d.newInstance(new DfpDec(new DfpField(16), 3.14159265));

 assertEquals("newInstance should return a new DfpDec instance with the same value", d, newInstance);
 assertEquals("newInstance with int argument should return a new DfpDec instance with the given int value", 12345, newInstance1.getDoubleValue(), 0.0001);
 assertEquals("newInstance with negative long argument should return a new DfpDec instance with the given negative long value", -256.0, newInstance2.getDoubleValue(), 0.0001);
 assertEquals("newInstance with DfpDec argument should return a new DfpDec instance with the given DfpDec value", 3.14159265, newInstance3.getDoubleValue(), 0.0001);
 }

 @Test
 public void testRound() {
 DfpDec d1 = new DfpDec(new DfpField(16), 1234567.89);
 DfpDec d2 = new DfpDec(new DfpField(16), 1234567.5);
 DfpDec d3 = new DfpDec(new DfpField(16), 1234567.333);
 DfpDec d4 = new DfpDec(new DfpField(16), 1234567.123);
 DfpDec d5 = new DfpDec(new DfpField(16), 1234566.999);
 DfpDec d6 = new DfpDec(new DfpField(16), 1234566.5);
 DfpDec d7 = new DfpDec(new DfpField(16), 1234566.111);
 DfpDec d8 = new DfpDec(new DfpField(16), 1234565.999);
 DfpDec d9 = new DfpDec(new DfpField(16), 1234565.5);
 DfpDec d10 = new DfpDec(new DfpField(16), 1234565.111);

 assertEquals("round(0) should return 0", 0, d1.round(0));
 assertEquals("round(-1) should throw an exception", expectedException(IllegalArgumentException.class), () -> d1.round(-1));
 assertEquals("round(int ROUND_DOWN) should round towards zero", 1234567, d2.round(0));
 assertEquals("round(int ROUND_UP) should round up", 1234568, d3.round(0));
 assertEquals("round(int ROUND_HALF_UP) should round up if the digit to be rounded is 5 or greater", 1234568, d4.round(0));
 assertEquals("round(int ROUND_HALF_DOWN) should round down if the digit to be rounded is 5 or greater", 1234567, d5.round(0));
 assertEquals("round(int ROUND_HALF_EVEN) should round evenly if the digit to be rounded is 5", 1234567, d6.round(0));
 assertEquals("round(int ROUND_HALF_ODD) should round away from zero if the digit to be rounded is 5", 1234568, d7.round(0));
 assertEquals("round(int ROUND_CEIL) should round towards positive infinity", 1234568, d8.round(0));
 assertEquals("round(int ROUND_FLOOR) should round towards negative infinity", 1234567, d9.round(0));
 }

 @Test
 public void testNegativeValues() {
 DfpDec d1 = new DfpDec(new DfpField(16), -1234567.89);
 DfpDec d2 = new DfpDec(new DfpField(16), -1234567.5);
 DfpDec d3 = new DfpDec(new DfpField(16), -1234567.333);
 DfpDec d4 = new DfpDec(new DfpField(16), -1234567.123);
 DfpDec d5 = new DfpDec(new DfpField(16), -1234566.999);
 DfpDec d6 = new DfpDec(new DfpField(16), -1234566.5);
 DfpDec d7 = new DfpDec(new DfpField(16), -1234566.111);
 DfpDec d8 = new DfpDec(new DfpField(16), -1234565.999);
 DfpDec d9 = new DfpDec(new DfpField(16), -1234565.5);
 DfpDec d10 = new DfpDec(new DfpField(16), -1234565.111);

 assertEquals("round(0) should return 0", 0, d1.round(0));
 assertEquals("round(-1) should throw an exception", expectedException(IllegalArgumentException.class), () -> d1.round(-1));
 assertEquals("round(int ROUND_DOWN) should round towards zero", -1234567, d2.round(0));
 assertEquals("round(int ROUND_UP) should round up", -1234566, d3.round(0));
 assertEquals("round(int ROUND_HALF_UP) should round up if the digit to be rounded is 5 or greater", -1234566, d4.round(0));
 assertEquals("round(int ROUND_HALF_DOWN) should round down if the digit to be rounded is 5 or greater", -1234567, d5.round(0));
 assertEquals("round(int ROUND_HALF_EVEN) should round evenly if the digit to be rounded is 5", -1234567, d6.round(0));
 assertEquals("round(int ROUND_HALF_ODD) should round away from zero if the digit to be rounded is 5", -1234566, d7.round(0));
 assertEquals("round(int ROUND_CEIL) should round towards positive infinity", -1234566, d8.round(0));
 assertEquals("round(int ROUND_FLOOR) should round towards negative infinity", -1234567, d9.round(0));
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(new DfpField(16), 0.123456789);
 DfpDec d2 = new DfpDec(new DfpField(16), 1234567.89);
 DfpDec d3 = new DfpDec(new DfpField(16), 1234567.5);
 DfpDec d4 = new DfpDec(new DfpField(16), 1234567.333);
 DfpDec d5 = new DfpDec(new DfpField(16), 1234567.123);
 DfpDec d6 = new DfpDec(new DfpField(16), 1234566.999);
 DfpDec d7 = new DfpDec(new DfpField(16), 1234566.5);
 DfpDec d8 = new DfpDec(new DfpField(16), 1234566.111);
 DfpDec d9 = new DfpDec(new DfpField(16), 1234565.999);
 DfpDec d10 = new DfpDec(new DfpField(16), 1234565.5);
 DfpDec d11 = new DfpDec(new DfpField(16), 1234565.111);
 DfpDec d12 = new DfpDec(new DfpField(16), 1234564.999);
 DfpDec d13 = new DfpDec(new DfpField(16), 1234564.5);
 DfpDec d14 = new DfpDec(new DfpField(16), 1234564.111);
 DfpDec d15 = new DfpDec(new DfpField(16), 1234563.999);
 DfpDec d16 = new DfpDec(new DfpField(16), 1234563.5);
 DfpDec d17 = new DfpDec(new DfpField(16), 1234563.111);
 DfpDec d18 = new DfpDec(new DfpField(16), 1234562.999);
 DfpDec d19 = new DfpDec(new DfpField(16), 1234562.5);
 DfpDec d20 = new DfpDec(new DfpField(16), 1234562.111);

 assertEquals("nextAfter(NaN) should return NaN", d1.nextAfter(d1), d1);
 assertEquals("nextAfter(negInf) should return inf", d1.nextAfter(new DfpDec(new DfpField(16), Double.NEGATIVE_INFINITY)), new DfpDec(new DfpField(16), Double.POSITIVE_INFINITY));
 assertEquals("nextAfter(0) should return 0", d1, d1.nextAfter(d1));
 assertEquals("nextAfter(posInf) should return inf", new DfpDec(new DfpField(16), Double.POSITIVE_INFINITY), d1.nextAfter(new DfpDec(new DfpField(16), Double.POSITIVE_INFINITY)));
 assertEquals("nextAfter(x < y) should return y", d2, d1.nextAfter(d2));
 assertEquals("nextAfter(x > y) should return x", d1, d2.nextAfter(d1));
 assertEquals("nextAfter(x = y) should return y", d2, d2.nextAfter(d2));
 assertEquals("nextAfter(x < 0) should return next positive number", -1234567.89, d1.nextAfter(d5));
 assertEquals("nextAfter(x > 0) should return next smaller number", 1234567.9, d1.nextAfter(d2));
 assertEquals("nextAfter(x == 0) should return a small positive number", 4.94066e-324, d1.nextAfter(new DfpDec(new DfpField(16), 0)));
 }
}