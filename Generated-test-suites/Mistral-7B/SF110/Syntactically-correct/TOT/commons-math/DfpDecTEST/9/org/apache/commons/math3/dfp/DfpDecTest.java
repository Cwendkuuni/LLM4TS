import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.Dfp;

public class DfpDecTest {

 private static final DfpField field = new Dfp128Field();

 @Test
 public void testConstructorWithDefaultFactory() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = new DfpDec(field).newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testConstructorWithFactoryAndNumber() {
 DfpDec d1 = new DfpDec(field, 1234);
 DfpDec d2 = new DfpDec(field, 1234);
 assertEquals(d1, d2);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec d2 = new DfpDec(field, 1234.5678);
 assertEquals(d1, d2);
 }

 @Test
 public void testConstructorWithFactoryAndString() {
 DfpDec d1 = new DfpDec(field, "1234.5678");
 DfpDec d2 = new DfpDec(field, "1234.5678");
 assertEquals(d1, d2);
 }

 @Test
 public void testConstructorWithFactoryAndDfp() {
 DfpDec d1 = new DfpDec(field, new DfpDec(field, 1234.5678));
 DfpDec d2 = new DfpDec(field, new DfpDec(field, 1234.5678));
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithDefaultFactory() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance();
 assertEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndByte() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance((byte) 1);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndInt() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance(1234);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndLong() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance(1234L);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndDouble() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance(1234.5678);
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndString() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance("1234.5678");
 assertNotEquals(d1, d2);
 }

 @Test
 public void testNewInstanceWithFactoryAndDfp() {
 DfpDec d1 = new DfpDec(field);
 DfpDec d2 = d1.newInstance(new DfpDec(field, 1234.5678));
 assertNotEquals(d1, d2);
 }

 @Test
 public void testRoundWithDefaultRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 int rounded = d1.round(0);
 assertEquals(0, rounded);
 }

 @Test
 public void testRoundWithUpRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(1);
 assertTrue(rounded.compareTo(new DfpDec(field, 1235.5678)) > 0);
 }

 @Test
 public void testRoundWithHalfUpRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(2);
 assertEquals(new DfpDec(field, 1235.5678), rounded);
 }

 @Test
 public void testRoundWithHalfDownRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(3);
 assertEquals(new DfpDec(field, 1234.5678), rounded);
 }

 @Test
 public void testRoundWithHalfEvenRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(4);
 assertEquals(new DfpDec(field, 1234.5678), rounded);
 }

 @Test
 public void testRoundWithOddRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(5);
 assertEquals(new DfpDec(field, 1234.5677), rounded);
 }

 @Test
 public void testRoundWithCeilRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(6);
 assertEquals(new DfpDec(field, 1235.5678), rounded);
 }

 @Test
 public void testRoundWithFloorRoundingMode() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec rounded = d1.round(7);
 assertEquals(new DfpDec(field, 1234.5678), rounded);
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(field, 0);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1));
 assertEquals(new DfpDec(field, 1), next);
 }

 @Test
 public void testNextAfterWithNegativeNumber() {
 DfpDec d1 = new DfpDec(field, -1234.5678);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1));
 assertEquals(new DfpDec(field, -1233.5678), next);
 }

 @Test
 public void testNextAfterWithPositiveNumberSmaller() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1235.5678));
 assertTrue(next.compareTo(new DfpDec(field, 1235.5679)) > 0);
 }

 @Test
 public void testNextAfterWithPositiveNumberEqual() {
 DfpDec d1 = new DfpDec(field, 1234.5678);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1234.5678));
 assertEquals(next, d1);
 }

 @Test
 public void testNextAfterWithInfiniteNumber() {
 DfpDec d1 = new DfpDec(field, Dfp.INFINITY);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1));
 assertEquals(Dfp.INFINITY, next);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(field, Dfp.NaN);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1));
 assertEquals(Dfp.NaN, next);
 }

 @Test
 public void testNextAfterWithNegativeInfiniteNumber() {
 DfpDec d1 = new DfpDec(field, Dfp.NEGATIVE_INFINITY);
 DfpDec next = d1.nextAfter(new DfpDec(field, 1));
 assertEquals(-Dfp.INFINITY, next);
 }
}