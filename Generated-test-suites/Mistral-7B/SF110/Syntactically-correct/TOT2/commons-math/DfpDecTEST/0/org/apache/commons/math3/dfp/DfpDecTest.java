import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private static final DfpField FIELD = new DfpField(23, 18, 12);

 @Test
 public void testConstructor() {
 DfpDec d1 = new DfpDec(FIELD);
 DfpDec d2 = new DfpDec(FIELD, 0);
 DfpDec d3 = new DfpDec(FIELD, 1);
 DfpDec d4 = new DfpDec(FIELD, -1);
 DfpDec d5 = new DfpDec(FIELD, 12345);
 DfpDec d6 = new DfpDec(FIELD, 12345.12345);
 DfpDec d7 = new DfpDec(FIELD, DfpDec.ONE);
 DfpDec d8 = new DfpDec(FIELD, DfpDec.NEG_ONE);
 DfpDec d9 = new DfpDec(FIELD, new DfpDec(FIELD, 1));
 DfpDec d10 = new DfpDec(FIELD, new DfpDec(FIELD, 1).newInstance(2));

 assertEquals("Check instance equality", d1, d1.newInstance());
 assertEquals("Check constructor with double", d6, new DfpDec(FIELD, d6.doubleValue()));
 }

 @Test
 public void testNewInstance() {
 DfpDec d1 = new DfpDec(FIELD, 12345);
 DfpDec d2 = d1.newInstance();
 assertEquals("NewInstance should return a new instance", d1, d2);
 }

 @Test
 public void testGetDecimalDigits() {
 DfpDec d1 = new DfpDec(FIELD, 12345.12345);
 int digits = d1.getDecimalDigits();
 assertEquals("getDecimalDigits should return the correct number of digits", 91, digits);
 }

 @Test
 public void testRound() {
 DfpDec d1 = new DfpDec(FIELD, 12345.6789);
 int roundingMode = DfpField.RoundingMode.ROUND_DOWN;
 int result = d1.round(roundingMode);
 assertEquals("Round down should round towards zero", 0, result);

 roundingMode = DfpField.RoundingMode.ROUND_UP;
 result = d1.round(roundingMode);
 assertEquals("Round up should round away from zero", 1, result);

 roundingMode = DfpField.RoundingMode.ROUND_HALF_UP;
 result = d1.round(roundingMode);
 assertEquals("Round half up should round to nearest even", 0, result);
 }

 @Test
 public void testRoundWithNegativeNumber() {
 DfpDec d1 = new DfpDec(FIELD, -12345.6789);
 int roundingMode = DfpField.RoundingMode.ROUND_DOWN;
 int result = d1.round(roundingMode);
 assertEquals("Round down should round towards zero for negative numbers", 0, result);

 roundingMode = DfpField.RoundingMode.ROUND_UP;
 result = d1.round(roundingMode);
 assertEquals("Round up should round away from zero for negative numbers", -1, result);

 roundingMode = DfpField.RoundingMode.ROUND_HALF_UP;
 result = d1.round(roundingMode);
 assertEquals("Round half up should round to nearest even for negative numbers", 0, result);
 }

 @Test
 public void testNextAfter() {
 DfpDec d1 = new DfpDec(FIELD, 12345.12345);
 DfpDec d2 = new DfpDec(FIELD, 12345.12345);
 DfpDec d3 = new DfpDec(FIELD, 12345.12345);
 DfpDec d4 = new DfpDec(FIELD, 12345.0);
 DfpDec d5 = new DfpDec(FIELD, -12345.12345);
 DfpDec d6 = new DfpDec(FIELD, -12345.0);
 DfpDec d7 = new DfpDec(FIELD, Double.NEGATIVE_INFINITY);
 DfpDec d8 = new DfpDec(FIELD, Double.POSITIVE_INFINITY);

 // Test nextAfter with equal numbers
 DfpDec result1 = d1.nextAfter(d2);
 assertTrue("nextAfter should return the same number for equal numbers", d1.equals(result1));

 // Test nextAfter with smaller number
 DfpDec result2 = d1.nextAfter(d4);
 assertTrue("nextAfter should return the next number for smaller numbers", d4.lessThan(result2));

 // Test nextAfter with larger number
 DfpDec result3 = d2.nextAfter(d1);
 assertTrue("nextAfter should return the previous number for larger numbers", d1.lessThan(result3));

 // Test nextAfter with negative numbers
 DfpDec result4 = d5.nextAfter(d6);
 assertTrue("nextAfter should return the next number for negative numbers", d6.lessThan(result4));

 // Test nextAfter with NaN and Infinity
 DfpDec result5 = d7.nextAfter(d8);
 assertTrue("nextAfter should return NaN for Infinity", d7.isNaN());
 DfpDec result6 = d8.nextAfter(d7);
 assertTrue("nextAfter should return Infinity for NaN", d8.isInfinite());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidRadixDigits() {
 DfpField invalidField = new DfpField(2, 18, 12);
 new DfpDec(invalidField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRoundWithInvalidRoundingMode() {
 DfpDec d1 = new DfpDec(FIELD, 12345.12345);
 int roundingMode = 12345;
 int result = d1.round(roundingMode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithNegativeSign() {
 DfpDec d1 = new DfpDec(FIELD, 12345);
 DfpDec d2 = new DfpDec(FIELD, -1, 1);
 DfpDec d3 = new DfpDec(FIELD, -1, DfpDec.NEG_ONE);
 DfpDec d4 = new DfpDec(FIELD, -1, new DfpDec(FIELD, 1));
 DfpDec d5 = new DfpDec(FIELD, -1, new DfpDec(FIELD, 1).newInstance(2));
 DfpDec d6 = new DfpDec(FIELD, new DfpDec(FIELD, 1).newInstance(-1));
 DfpDec d7 = new DfpDec(FIELD, new DfpDec(FIELD, 1).newInstance(2).newInstance(-1));

 // Test construction with negative sign and valid number
 assertEquals("Check construction with negative sign and valid number", d1.negate().toString(), d2.toString());

 // Test construction with negative sign and DfpDec object
 assertEquals("Check construction with negative sign and DfpDec object", d1.negate().toString(), d3.toString());

 // Test construction with negative sign and Dfp object
 assertEquals("Check construction with negative sign and Dfp object", d1.negate().toString(), d4.toString());

 // Test construction with negative sign and double
 assertEquals("Check construction with negative sign and double", d1.negate().toString(), d5.toString());

 // Test construction with negative sign and string
 assertEquals("Check construction with negative sign and string", d1.negate().toString(), d6.toString());
 }
}