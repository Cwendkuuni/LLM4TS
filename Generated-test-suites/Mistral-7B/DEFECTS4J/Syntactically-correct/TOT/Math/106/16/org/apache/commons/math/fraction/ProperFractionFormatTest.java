import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 format = new ProperFractionFormat();
 String expected = "1/2";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractionDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(3, 4);
 String expected = "-3.00 / 4.00";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1 2/3";
 Fraction expected = new Fraction(1, 2, 3);
 Fraction actual = format.parse(input, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 format.parse(input, pos);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "3 / -1 2";
 ParsePosition pos = new ParsePosition(0);
 try {
 format.parse(input, pos);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseImproperFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "5 2/3";
 Fraction expected = new Fraction(13, 3);
 Fraction actual = format.parse(input, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLeadingMinusSign() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "-1 2/3";
 Fraction expected = new Fraction(-1, 2, 3);
 Fraction actual = format.parse(input, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2, 3);
 format = new ProperFractionFormat();
 String expected = "-1 2/3";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }
}