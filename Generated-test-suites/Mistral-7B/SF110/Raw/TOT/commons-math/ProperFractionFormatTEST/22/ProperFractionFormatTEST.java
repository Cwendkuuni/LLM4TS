import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(4, 5);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String formattedResult = toAppendTo.toString();
 String expectedResult = "1 4/5";
 assertEquals(expectedResult, formattedResult);
 }

 @Test
 public void testFormatWholeNumber() {
 Fraction fraction = new Fraction(3);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String formattedResult = toAppendTo.toString();
 String expectedResult = "3";
 assertEquals(expectedResult, formattedResult);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-4, 5);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String formattedResult = toAppendTo.toString();
 String expectedResult = "-1 4/5";
 assertEquals(expectedResult, formattedResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullFraction() {
 Fraction fraction = null;
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 }

 @Test
 public void testParseSimpleProperFraction() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "1 4/5";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = format.parse(source, pos);
 Fraction expectedFraction = new Fraction(4, 5);
 assertEquals(expectedFraction, parsedFraction);
 pos.setIndex(source.length());
 }

 @Test
 public void testParseWholeNumber() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = format.parse(source, pos);
 Fraction expectedFraction = new Fraction(3);
 assertEquals(expectedFraction, parsedFraction);
 pos.setIndex(source.length());
 }

 @Test
 public void testParseNegativeProperFraction() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-1 4/5";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = format.parse(source, pos);
 Fraction expectedFraction = new Fraction(-4, 5);
 assertEquals(expectedFraction, parsedFraction);
 pos.setIndex(source.length());
 }

 @Test(expected = ParsePositionException.class)
 public void testParseInvalidInput() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "1 4/5a";
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = null;
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Before
 public void setUp() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 }

}