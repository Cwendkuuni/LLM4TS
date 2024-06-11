import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 testFormat(format, "1/2", new Fraction(1, 2));
 testParse(format, "1/2", new Fraction(1, 2));
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, "3 1/2", new Fraction(3, 2));
 testParse(format, "3 1/2", new Fraction(3, 2));
 }

 @Test
 public void testFormatNegativeWhole() {
 format = new ProperFractionFormat();
 String result = format.format(new Fraction(-1, 2), new StringBuffer(), null);
 Assert.assertEquals("-1 1/2", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidWholeNegative() {
 format = new ProperFractionFormat();
 String source = "-1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidNumerator() {
 format = new ProperFractionFormat();
 String source = "2/0";
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDenominator() {
 format = new ProperFractionFormat();
 String source = "2 -1/2";
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidMixedFormat() {
 format = new ProperFractionFormat();
 String source = "-2 -1/2";
 ParsePosition pos = new ParsePosition(0);
 format.parse(source, pos);
 }

 @Test
 public void testFormatWithCustomNumberFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(-3, 2), new StringBuffer(), null);
 Assert.assertEquals("-03 1/2", result.toString());
 }

 @Test
 public void testParseWithCustomNumberFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-03 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(-3, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndNegativeSignBeforeFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(-3, 2), new StringBuffer(), null);
 Assert.assertEquals("-3 1/2", result.toString());
 }

 @Test
 public void testParseWithCustomNumberFormatAndNegativeSignBeforeFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(-3, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFormatWithBigDecimal() {
 ProperFractionFormat format = new ProperFractionFormat();
 BigDecimal bigDecimal = new BigDecimal("-1.5");
 Fraction fraction = new Fraction(bigDecimal.intValueExact(), bigDecimal.scale());
 String result = format.format(fraction, new StringBuffer(), null);
 Assert.assertEquals("-1 1/2", result.toString());
 }

 @Test
 public void testParseWithBigDecimal() {
 ProperFractionFormat format = new ProperFractionFormat();
 BigDecimal bigDecimal = new BigDecimal("-1.5");
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(bigDecimal.toPlainString(), pos);
 Assert.assertEquals(-1, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFormatWithMixedFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(3, 2), new StringBuffer(), null);
 Assert.assertEquals("300% 1/2", result.toString());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStyles() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(3, 2), new StringBuffer(), null);
 Assert.assertEquals("300% 1/2", result.toString());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(-3, 2), new StringBuffer(), null);
 Assert.assertEquals("-300% 1/2", result.toString());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFractionAndBigDecimal() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 BigDecimal bigDecimal = new BigDecimal("-1.5");
 Fraction fraction = new Fraction(bigDecimal.intValueExact(), bigDecimal.scale());
 String result = format.format(fraction, new StringBuffer(), null);
 Assert.assertEquals("-300% 1/2", result.toString());
 }

 @Test
 public void testParseWithCustomNumberFormatAndDifferentNumberStyles() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "300% 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(3, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testParseWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFraction() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-300% 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(-3, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFractionAndCustomDenominatorFormatWithLeadingZeroes() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ITALY);
 denominatorFormat.setMinimumIntegerDigits(2);
 denominatorFormat.setParseIntegerOnly(true);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String result = format.format(new Fraction(-3, 2), new StringBuffer(), null);
 Assert.assertEquals("-300% 02/2", result.toString());
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFractionAndCustomDenominatorFormatWithLeadingZeroesAndBigDecimal() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ITALY);
 denominatorFormat.setMinimumIntegerDigits(2);
 denominatorFormat.setParseIntegerOnly(true);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 BigDecimal bigDecimal = new BigDecimal("-1.5");
 Fraction fraction = new Fraction(bigDecimal.intValueExact(), bigDecimal.scale());
 String result = format.format(fraction, new StringBuffer(), null);
 Assert.assertEquals("-300% 02/2", result.toString());
 }

 @Test
 public void testParseWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFractionAndCustomDenominatorFormatWithLeadingZeroes() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ITALY);
 denominatorFormat.setMinimumIntegerDigits(2);
 denominatorFormat.setParseIntegerOnly(true);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String source = "-300% 02/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(-3, fraction.getNumerator());
 Assert.assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFormatAndParseWithDifferentNumberStylesAndNegativeSignBeforeFractionAndCustomDenominatorFormatWithLeadingZeroes_RoundTrip() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ITALY);
 denominatorFormat.setMinimumIntegerDigits(2);
 denominatorFormat.setParseIntegerOnly(true);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String input = "-300% 02/2";
 Fraction fraction = format.parse(input, new ParsePosition(0));
 String output = format.format(fraction, new StringBuffer(), null);
 Assert.assertEquals(input, output);
 }

 @Test
 public void testFormatWithCustomNumberFormatAndDifferentNumberStylesAndNegativeSignBeforeFractionAndCustomDenominatorFormatWithLeadingZeroesAndBigDecimalWithDifferentScale() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumIntegerDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ITALY);
 denominatorFormat.setMinimumIntegerDigits(2);
 denominatorFormat.setParseIntegerOnly(true);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 BigDecimal bigDecimal1 = new BigDecimal("-1.5");
 Fraction fraction1 = new Fraction(bigDecimal1.intValueExact(), bigDecimal1.scale());
 BigDecimal bigDecimal2 = new BigDecimal("-1.6");
 Fraction fraction2 = new Fraction(bigDecimal2.intValueExact(), bigDecimal2.scale());
 String result1 = format.format(fraction1, new StringBuffer(), null);
 String result2 = format.format(fraction2, new StringBuffer(), null);
 Assert.assertEquals(LevenshteinDistance.distance(result1, result2), 1);
 }
}