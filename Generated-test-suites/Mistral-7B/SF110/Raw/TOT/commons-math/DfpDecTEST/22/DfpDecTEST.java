import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testConstructorWithFactory() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory);
 assertEquals(0.0, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory, 3.14159265358979323846);
 assertEquals(3.14159265358979323846, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndString() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory, "3.14159265358979323846");
 assertEquals(3.14159265358979323846, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndByte() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory, (byte) 0x3B);
 assertEquals(59.0, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndInt() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory, 59);
 assertEquals(59.0, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndLong() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec = new DfpDec(factory, 9223372036854775807L);
 assertEquals(9.223372036854775807E+18, dfpDec.doubleValueExact(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndDfp() {
 DfpField factory = new DfpField(16, 64);
 DfpDec dfpDec1 = new DfpDec(factory, 3.14159265358979323846);
 DfpDec dfpDec2 = new DfpDec(factory, new DfpDec(factory, 1.2345));
 assertEquals(3.775987321811332221, dfpDec1.add(dfpDec2).doubleValueExact(), 0.0);
 }

 @Test
 public void testRoundingZero() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, 1.2345);
 // Test rounding down
 assertEquals(1.2345, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
 // Test rounding up
 assertEquals(1.235, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding to even
 assertEquals(1.2345, number.round(Dfp.ROUND_HALF_DOWN).doubleValueExact(), 0.0);
 assertEquals(1.2345, number.round(Dfp.ROUND_HALF_UP).doubleValueExact(), 0.0);
 // Test rounding towards positive infinity
 assertEquals(1.235, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding towards negative infinity
 assertEquals(1.2345, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
}

 @Test
 public void testRoundingPositiveNumber() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, 123.456);
 // Test rounding down
 assertEquals(123.456, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
 // Test rounding up
 assertEquals(123.46, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding to even
 assertEquals(123.456, number.round(Dfp.ROUND_HALF_DOWN).doubleValueExact(), 0.0);
 assertEquals(123.456, number.round(Dfp.ROUND_HALF_UP).doubleValueExact(), 0.0);
 // Test rounding towards positive infinity
 assertEquals(123.46, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding towards negative infinity
 assertEquals(123.456, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
}

 @Test
 public void testRoundingNegativeNumber() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, -123.456);
 // Test rounding down
 assertEquals(-123.456, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
 // Test rounding up
 assertEquals(-123.455, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding to even
 assertEquals(-123.456, number.round(Dfp.ROUND_HALF_DOWN).doubleValueExact(), 0.0);
 assertEquals(-123.456, number.round(Dfp.ROUND_HALF_UP).doubleValueExact(), 0.0);
 // Test rounding towards positive infinity
 assertEquals(-123.455, number.round(Dfp.ROUND_UP).doubleValueExact(), 0.0);
 // Test rounding towards negative infinity
 assertEquals(-123.456, number.round(Dfp.ROUND_DOWN).doubleValueExact(), 0.0);
}

 @Test
 public void testNextAfter() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, 0.123456789);
 DfpDec nextNumber = number.nextAfter(new DfpDec(factory, 1.0));
 assertEquals(0.123456789000000183105477336151383262, nextNumber.doubleValueExact(), 0.0);
 }

 @Test
 public void testNextAfterZero() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, 0.0);
 DfpDec nextNumber = number.nextAfter(new DfpDec(factory, 1.0));
 assertEquals(1.111111111111111110999999998337203653e-324, nextNumber.doubleValueExact(), 0.0);
 }

 @Test
 public void testNextAfterNegativeNumber() {
 DfpField factory = new DfpField(16, 64);
 DfpDec number = new DfpDec(factory, -0.123456789);
 DfpDec nextNumber = number.nextAfter(new DfpDec(factory, 1.0));
 assertEquals(-0.123456788999999816894522261525634735, nextNumber.doubleValueExact(), 0.0);
 }
}