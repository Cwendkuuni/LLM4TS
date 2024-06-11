import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldConfigurable;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private static final DfpFieldConfigurable factory = new DfpField(16, 16, 53, 10000);

 @Test
 public void testZero() {
 DfpDec d1 = new DfpDec(factory);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec expected = DfpDec.newInstance(factory.getZero());
 assertEquals(expected, d2);
 }

 @Test
 public void testConstructorWithNumber() {
 DfpDec d1 = new DfpDec(factory, 1234L);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec expected = new DfpDec(factory, 1234L);
 assertEquals(expected, d2);
 }

 @Test
 public void testConstructorWithString() {
 DfpDec d1 = new DfpDec(factory, "1234.5678");
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec expected = new DfpDec(factory, "1234.5678");
 assertEquals(expected, d2);
 }

 @Test
 public void testNewInstanceWithNumber() {
 DfpDec d1 = new DfpDec(factory, 1234L);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec expected = new DfpDec(factory, 1234L);
 assertEquals(expected, d2);
 }

 @Test
 public void testNewInstanceWithDfp() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, 1234L));
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec expected = new DfpDec(factory, new DfpDec(factory, 1234L));
 assertEquals(expected, d2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewInstanceWithInvalidRadixDigits() {
 DfpDec d1 = new DfpDec(factory, 1234L);
 DfpDec d2 = DfpDec.newInstance(d1);
 DfpDec d3 = DfpDec.newInstance(factory, 7); // radixDigits should be 16, not 7
 DfpDec d4 = DfpDec.newInstance(d3);
 assertNotEquals(d2, d4);
 }

 @Test
 public void testRounding() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_UP;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 int expectedRoundedDigits = (int) Math.ceil(Math.log10(Math.pow(10, expectedDecimalDigits)) + 1);
 DfpDec expected = DfpDec.newInstance(factory, d1.toString().substring(0, expectedRoundedDigits + 1));
 assertEquals(expected, rounded);
 }

 @Test
 public void testRoundingDown() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_DOWN;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 int expectedRoundedDigits = (int) Math.floor(Math.log10(Math.pow(10, expectedDecimalDigits)) + 1);
 DfpDec expected = DfpDec.newInstance(factory, d1.toString().substring(0, expectedRoundedDigits + 1));
 assertEquals(expected, rounded);
 }

 @Test
 public void testRoundingHalfUp() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_HALF_UP;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String roundedString = rounded.toString();
 int expectedLength = d1String.length();
 int roundingDigitIndex = d1String.length() - expectedDecimalDigits;
 char roundingDigit = d1String.charAt(roundingDigitIndex);
 char roundedDigit = roundedString.charAt(roundingDigitIndex);
 if (roundingDigit == '5') {
 assertEquals("5", roundedDigit);
 } else {
 assertEquals(roundingDigit, roundedDigit);
 }
 }

 @Test
 public void testRoundingHalfDown() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_HALF_DOWN;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String roundedString = rounded.toString();
 int expectedLength = d1String.length();
 int roundingDigitIndex = d1String.length() - expectedDecimalDigits;
 char roundingDigit = d1String.charAt(roundingDigitIndex);
 char roundedDigit = roundedString.charAt(roundingDigitIndex);
 if (roundingDigit == '5') {
 assertEquals("4", roundedDigit);
 } else {
 assertEquals(roundingDigit, roundedDigit);
 }
 }

 @Test
 public void testRoundingHalfEven() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_HALF_EVEN;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String roundedString = rounded.toString();
 int expectedLength = d1String.length();
 int roundingDigitIndex = d1String.length() - expectedDecimalDigits;
 char roundingDigit = d1String.charAt(roundingDigitIndex);
 char roundedDigit = roundedString.charAt(roundingDigitIndex);
 if (roundingDigit == '5') {
 if (roundingDigitIndex == expectedLength - 1) {
 assertEquals("5", roundedDigit);
 } else {
 assertEquals("4", roundedDigit);
 }
 } else {
 assertEquals(roundingDigit, roundedDigit);
 }
 }

 @Test
 public void testRoundingHalfOdd() {
 DfpDec d1 = new DfpDec(factory, 12345.123456789L);
 int roundingMode = DfpField.ROUND_HALF_ODD;
 DfpDec rounded = DfpDec.newInstance(d1.round(roundingMode));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String roundedString = rounded.toString();
 int expectedLength = d1String.length();
 int roundingDigitIndex = d1String.length() - expectedDecimalDigits;
 char roundingDigit = d1String.charAt(roundingDigitIndex);
 char roundedDigit = roundedString.charAt(roundingDigitIndex);
 if (roundingDigit == '5') {
 if (roundingDigitIndex % 2 == 1) {
 assertEquals("5", roundedDigit);
 } else {
 assertEquals("4", roundedDigit);
 }
 } else {
 assertEquals(roundingDigit, roundedDigit);
 }
 }

 @Test
 public void testNextAfterWithLessThan() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, 1.0).subtract(new DfpDec(factory, 0.0000001)));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(new DfpDec(factory, 1.0)));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("1.0000001", nextString.substring(0, expectedLength));
 }

 @Test
 public void testNextAfterWithEqual() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, 1.0));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(d1));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals(d1String, nextString);
 }

 @Test
 public void testNextAfterWithGreaterThan() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, 1.0).add(new DfpDec(factory, 0.0000001)));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(new DfpDec(factory, 1.0)));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("1.0000001", nextString.substring(0, expectedLength));
 }

 @Test
 public void testNextAfterWithLessThanZero() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, -1.0).subtract(new DfpDec(factory, 0.0000001)));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(new DfpDec(factory, -1.0)));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("-1.0000001", nextString.substring(0, expectedLength));
 }

 @Test
 public void testNextAfterWithZero() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, 0.0));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(d1));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("0.0", nextString);
 }

 @Test
 public void testNextAfterWithNaN() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, Double.NaN));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(d1));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("NaN", nextString);
 }

 @Test
 public void testNextAfterWithInfinities() {
 DfpDec d1 = new DfpDec(factory, new DfpDec(factory, Double.POSITIVE_INFINITY));
 DfpDec next = DfpDec.newInstance(d1.nextAfter(d1));
 int expectedDecimalDigits = d1.getDecimalDigits();
 String d1String = d1.toString();
 String nextString = next.toString();
 int expectedLength = d1String.length();
 assertEquals("Inf", nextString);
 }
}