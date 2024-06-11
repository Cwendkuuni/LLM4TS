import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static ProperFractionFormat format;

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 String actual = format(fraction).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(3, 4);
 String expected = "-300.0% / 4";
 String actual = customFormat(fraction).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNegativeWholeNumber() {
 Fraction fraction = new Fraction(-1, 2);
 String expected = "-1/2";
 String actual = format(fraction).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatZeroNumerator() {
 Fraction fraction = new Fraction(0, 1);
 String expected = "0 / 1";
 String actual = format(fraction).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatZeroDenominator() {
 Fraction fraction = new Fraction(1, 0);
 assertNull(format(fraction));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 String source = "3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail();
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNegativeDenominator() {
 String source = "1 / -2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail();
 }

 @Test
 public void testParseSimpleFraction() {
 String source = "1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 Fraction expected = new Fraction(1, 2);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseMixedNumber() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 Fraction expected = new Fraction(7, 2);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseImproperFraction() {
 String source = "5 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 Fraction expected = new Fraction(7, 2);
 assertEquals(expected, fraction);
 }

 private String format(Fraction fraction) {
 String result = new StringBuffer().append(format.format(fraction, new StringBuffer(), null)).toString();
 return result;
 }

}