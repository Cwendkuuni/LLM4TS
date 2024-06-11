import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;

 @Before
 public void setup() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = new DecimalFormat("#");
 }

 @Test
 public void testDefaultConstructor() {
 Fraction result = properFractionFormat.parse("1/2", new ParsePosition(0, null));
 assertEquals("The result of parsing '1/2' should be 1/2", new Fraction(1, 2), result);

 String actual = properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), null);
 assertEquals("The formatted string of 1/2 should be '1/2'", "1/2", actual.toString());
 }

 @Test
 public void testNumberFormatConstructor() {
 NumberFormat numeratorFormat = new DecimalFormat("#");
 NumberFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat properFractionFormatWithCustomFormats = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 Fraction result = properFractionFormatWithCustomFormats.parse("3 whole / 4 parts", new ParsePosition(0, null));
 assertEquals("The result of parsing '3 whole / 4 parts' should be 3 + 4/1", new Fraction(7, 4), result);

 String actual = properFractionFormatWithCustomFormats.format(new Fraction(7, 4), new StringBuffer(), null);
 assertEquals("The formatted string of 3 + 4/1 should be '3 whole / 4 parts'", "3 whole / 4 parts", actual.toString());
 }

 @Test
 public void testWholeFormat() {
 ProperFractionFormat properFractionFormatWithCustomWholeFormat = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));

 Fraction result = properFractionFormatWithCustomWholeFormat.parse("3 whole", new ParsePosition(0, null));
 assertEquals("The result of parsing '3 whole' should be 3", new Fraction(3, 1), result);

 String actual = properFractionFormatWithCustomWholeFormat.format(new Fraction(3, 1), new StringBuffer(), null);
 assertEquals("The formatted string of 3 should be '3 whole'", "3 whole", actual.toString());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 } catch (NullArgumentException e) {
 // Expected
 return;
 }
 fail("Setting a null format should throw a NullArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("Invalid format", pos);
 assertEquals("The result of parsing an invalid format should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumber() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("A", pos);
 assertEquals("The result of parsing a non-number should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseNegativeNumerator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("1/-2", pos);
 assertEquals("The result of parsing a negative numerator should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseZeroNumerator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("0/1", pos);
 assertEquals("The result of parsing a zero numerator should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMissingDenominator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("1", pos);
 assertEquals("The result of parsing a fraction without a denominator should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test(expected = ArithmeticException.class)
 public void testParseOverflow() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 Fraction result = properFractionFormat.parse("1/0", pos);
 assertEquals("The result of parsing a division by zero should be null", null, result);
 assertFalse("The parse position should not be modified", pos.getIndex() > 0);
 }

 @Test
 public void testFormatNegativeFraction() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 StringBuffer toAppendTo = new StringBuffer();
 Fraction fraction = new Fraction(-3, 2);
 properFractionFormat.format(fraction, toAppendTo, null);
 assertEquals("The formatted string of -3/2 should be '-3/2'", "-3/2", toAppendTo.toString());
 }

 @Test
 public void testFormatZeroFraction() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 StringBuffer toAppendTo = new StringBuffer();
 Fraction fraction = new Fraction(0, 1);
 properFractionFormat.format(fraction, toAppendTo, null);
 assertEquals("The formatted string of 0/1 should be '0'", "0", toAppendTo.toString());
 }

 @Test
 public void testFormatLargeFraction() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 StringBuffer toAppendTo = new StringBuffer();
 Fraction fraction = new Fraction(Long.MAX_VALUE, 1);
 properFractionFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 String expected = new StringBuilder().append(Long.MAX_VALUE).append("/1").toString();
 assertTrue("The formatted string of a large fraction should match the expected value", Precision.closeEquals(expected, actual, 0.0));
 }
}