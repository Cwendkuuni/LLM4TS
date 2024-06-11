import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 @Test
 public void testDfpDecConstructor() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory);
 assertNotNull(dfpDec);
 }

 @Test
 public void testDfpDecConstructorDouble() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 3.14);
 assertEquals("3.14", dfpDec.toString());
 }

 @Test
 public void testNewInstance() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec1 = new DfpDec(factory, 1.23);
 DfpDec dfpDec2 = dfpDec1.newInstance();
 assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testNewInstanceDfp() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec1 = new DfpDec(factory, 1.23);
 DfpDec dfpDec2 = new DfpDec(factory, new DfpDec(factory, 1.23));
 assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testRoundingZero() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 0.5);
 int result = dfpDec.round(0);
 assertEquals(0, result);
 }

 @Test
 public void testRoundingUp() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int result = dfpDec.round(0);
 assertEquals(1, result);
 }

 @Test
 public void testRoundingHalfUp() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int result = dfpDec.round(4);
 assertEquals(1, result);
 }

 @Test
 public void testRoundingHalfDown() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int result = dfpDec.round(5);
 assertEquals(0, result);
 }

 @Test
 public void testRoundingHalfEven() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int result = dfpDec.round(6);
 assertEquals(0, result);
 }

 @Test
 public void testRoundingHalfOdd() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int result = dfpDec.round(7);
 assertEquals(1, result);
 }

 @Test
 public void testRoundingCeil() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 1.4);
 int result = dfpDec.round(0);
 assertEquals(1, result);
 }

 @Test
 public void testRoundingFloor() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, -1.4);
 int result = dfpDec.round(0);
 assertEquals(-2, result);
 }

 @Test
 public void testNextAfter() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec1 = new DfpDec(factory, 1.23);
 DfpDec dfpDec2 = new DfpDec(factory, 1.3);
 DfpDec result = dfpDec1.nextAfter(dfpDec2);
 assertFalse(dfpDec1.equals(result));
 }

 @Test
 public void testNextAfterZero() {
 DfpField factory = new Dfp16Field();
 DfpDec dfpDec = new DfpDec(factory, 0);
 DfpDec result = dfpDec.nextAfter(new DfpDec(factory, 1.0));
 assertFalse(dfpDec.equals(result));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testNewInstanceInvalidRadixDigits() {
 DfpField factory = new Dfp1Field(); // Invalid radix digits
 DfpDec dfpDec = new DfpDec(factory);
 }
}