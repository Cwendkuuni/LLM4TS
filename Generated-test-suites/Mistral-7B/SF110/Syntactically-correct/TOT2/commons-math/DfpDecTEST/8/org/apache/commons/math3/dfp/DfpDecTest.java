import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpException;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private DfpField field = new Dfp128Field();

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec = new DfpDec(new DfpDec(field, 1.2345));
 assertEquals(1.2345, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testConstructorWithLong() {
 DfpDec dfpDec = new DfpDec(field, 1234567890123456789L);
 assertEquals(1234567890123456789.0, dfpDec.doubleValue(), 0.0);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec dfpDec = new DfpDec(field, "1.2345");
 assertEquals(1.2345, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(field);
 DfpDec newInstance = dfpDec.newInstance();
 assertEquals(dfpDec, newInstance);
 }

 @Test
 public void testRound() {
 DfpDec dfpDec = new DfpDec(field, 1.2356);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 dfpDec.round(1);
 assertTrue(dfpDec.toString().endsWith("5"));
 dfpDec.round(2);
 assertEquals(1.2356, dfpDec.doubleValue(), 0.0001);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec dfpDec = new DfpDec(field, 1.12345);
 int decimalDigits = dfpDec.getDecimalDigits();
 assertEquals(16, decimalDigits);
 }

 @Test(expected = DfpException.class)
 public void testNewInstanceWithIncompatibleRadixDigits() {
 DfpDec dfpDec1 = new DfpDec(field, 123);
 DfpDec dfpDec2 = new DfpDec(new Dfp16Field(), 123);
 DfpDec newInstance = dfpDec1.newInstance(dfpDec2);
 }

 @Test
 public void testNextAfter() {
 DfpDec dfpDec1 = new DfpDec(field, 1.2345);
 DfpDec dfpDec2 = new DfpDec(field, 1.2346);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(dfpDec2, nextAfter);
 }

 @Test
 public void testNextAfterZero() {
 DfpDec dfpDec = new DfpDec(field, 0);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(1.0E-324, nextAfter.doubleValue(), 0.0);
 }

 @Test
 public void testNextAfterNegativeNumber() {
 DfpDec dfpDec = new DfpDec(field, -1.2345);
 DfpDec nextAfter = dfpDec.nextAfter(dfpDec);
 assertEquals(-1.2344, nextAfter.doubleValue(), 0.0001);
 }

 @Test
 public void testNextAfterNaN() {
 DfpDec dfpDec1 = new DfpDec(field, Double.NaN);
 DfpDec dfpDec2 = new DfpDec(field, 1.2345);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertEquals(dfpDec2, nextAfter);
 }

 @Test
 public void testNextAfterInfinity() {
 DfpDec dfpDec1 = new DfpDec(field, Double.POSITIVE_INFINITY);
 DfpDec dfpDec2 = new DfpDec(field, 1.2345);
 DfpDec nextAfter = dfpDec1.nextAfter(dfpDec2);
 assertNotNull(nextAfter);
 assertTrue(nextAfter.isInfinite());
 }

}