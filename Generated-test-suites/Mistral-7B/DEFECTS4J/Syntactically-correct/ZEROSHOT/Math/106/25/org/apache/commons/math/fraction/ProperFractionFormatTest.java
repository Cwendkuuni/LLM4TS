import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simpledefault.SimpleDefaultNumberFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private final NumberFormat wholeFormat = new SimpleDefaultNumberFormat("#,##0", Locale.US);
 private final NumberFormat numeratorFormat = new SimpleDefaultNumberFormat("#,##0");
 private final NumberFormat denominatorFormat = new SimpleDefaultNumberFormat("#,##0");
 private final FractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("1 / 2", formatted.toString());

 Fraction parsed = properFractionFormat.parse("1 / 2", new ParsePosition(0));
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(fraction.getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(fraction.getDenominator(), parsed.getDenominator());
 }

 @Test
 public void testCustomFormat() {
 // Set up custom formats
 NumberFormat customWholeFormat = new SimpleDefaultNumberFormat("#(#,##0)##", Locale.US);
 NumberFormat customNumeratorFormat = new SimpleDefaultNumberFormat("#(#,##0)");
 NumberFormat customDenominatorFormat = new SimpleDefaultNumberFormat("#(#,##0)");
 FractionFormat customProperFractionFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);

 // Test formatting
 Fraction fraction = new Fraction(3, 4);
 String formatted = customProperFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("3 (3) / (4) 4", formatted.toString());

 // Test parsing
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = customProperFractionFormat.parse(formatted, pos);
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(fraction.getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(fraction.getDenominator(), parsed.getDenominator());
 }

 @Test
 public void testParseImproperFraction() {
 String improperFraction = "2 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(improperFraction, pos);
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(new Fraction(3, 2).getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(new Fraction(3, 2).getDenominator(), parsed.getDenominator());
 }

 @Test
 public void testParseWhitespace() {
 String whitespace = " 1 / 2 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(whitespace, pos);
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(new Fraction(1, 2).getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(new Fraction(1, 2).getDenominator(), parsed.getDenominator());
 }

 @Test
 public void testParseNegativeWholeNumber() {
 String negativeWholeNumber = "-1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(negativeWholeNumber, pos);
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(-1, parsed.getNumerator().intValue());
 Assertions.assertEquals(new Fraction(1, 2).getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(new Fraction(1, 2).getDenominator(), parsed.getDenominator());
 }

 @Test
 public void testParseNegativeNumerator() {
 String negativeNumerator = "1 / -2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(negativeNumerator, pos);
 Assertions.assertNotNull(parsed);
 Assertions.assertEquals(new Fraction(1, 2).getNumerator(), parsed.getNumerator());
 Assertions.assertEquals(-2, parsed.getDenominator().intValue());
 }

 @Test
 public void testParseInvalid() {
 String invalid = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(invalid, pos);
 Assertions.assertNull(parsed);
 Assertions.assertEquals(pos.getIndex(), 0);
 Assertions.assertEquals(pos.getErrorIndex(), 3);
 }

 @Test
 public void testParseInvalidNegativeNumerator() {
 String invalidNegativeNumerator = "-3 / -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(invalidNegativeNumerator, pos);
 Assertions.assertNull(parsed);
 Assertions.assertEquals(pos.getIndex(), 0);
 Assertions.assertEquals(pos.getErrorIndex(), 7);
 }

 @Test
 public void testParseInvalidNegativeWholeNumber() {
 String invalidNegativeWholeNumber = "-3 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(invalidNegativeWholeNumber, pos);
 Assertions.assertNull(parsed);
 Assertions.assertEquals(pos.getIndex(), 0);
 Assertions.assertEquals(pos.getErrorIndex(), 2);
 }

 @Test
 public void testParseInvalidWhitespace() {
 String invalidWhitespace = " 1 / 2 / ";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(invalidWhitespace, pos);
 Assertions.assertNull(parsed);
 Assertions.assertEquals(pos.getIndex(), 0);
 Assertions.assertEquals(pos.getErrorIndex(), 5);
 }

 @Test
 public void testFormatNegativeWholeNumber() {
 Fraction fraction = new Fraction(-3, 2);
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("-3 / 2", formatted.toString());
 }

 @Test
 public void testFormatNegativeNumerator() {
 Fraction fraction = new Fraction(3, -2);
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("3 / -2", formatted.toString());
 }

 @Test
 public void testFormatMixedNegative() {
 Fraction fraction = new Fraction(-3, -2);
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("-3 / 2", formatted.toString());
 }

 @Test
 public void testFormatLargeNumber() {
 Fraction fraction = new Fraction(BigDecimal.valueOf("12345678901234567890").toBigInteger(), BigDecimal.valueOf("98765432109876543210").toBigInteger());
 String formatted = properFractionFormat.format(fraction, new StringBuffer(), null);
 Assertions.assertEquals("1,234,567,890,123,456,789,012,345,678,901,234,567,890 / 9,876,543,210,987,654,321,098,765,432,109,876,543,210", formatted.toString());
 }
}