import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat intFormat;
 private ProperFractionFormat format;

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("1 / 2", result.toString());

 Fraction parsedFraction = format.parse("1 / 2", new ParsePosition(0));
 assertEquals(fraction, parsedFraction);
 }

 @Test
 public void testCustomFormat() {
 intFormat = NumberFormat.getNumberInstance();
 intFormat.setMinimumFractionDigits(2);
 intFormat.setMaximumFractionDigits(2);
 intFormat.setParseBigDecimal(true);

 format = new ProperFractionFormat(intFormat, intFormat, intFormat);

 Fraction fraction = new Fraction(3, 4);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("3.00 / 4", result.toString());

 Fraction parsedFraction = format.parse("3.00 / 4", new ParsePosition(0));
 assertEquals(fraction, parsedFraction);
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormat() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("-3 -1/2", pos);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatWithWhitespace() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("-3  -1/2", pos);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatNegativeNumerator() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("-2 / 3", pos);
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatNegativeDenominator() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 format.parse("2 / -3", pos);
 fail("Expected ParseException");
 }

 @Test
 public void testParseImproperFraction() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse("5/2", pos);
 assertEquals(new Fraction(11, 2), fraction);
 }
}