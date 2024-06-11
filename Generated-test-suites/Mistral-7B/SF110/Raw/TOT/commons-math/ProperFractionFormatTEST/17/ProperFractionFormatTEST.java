package org.apache.commons.math3.fraction;

import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 StringBuffer toAppendTo = new StringBuffer();
 format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(3, 4);
 String expected = "3/4";
 StringBuffer toAppendTo = new StringBuffer();
 format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 int wholeNumber = 5;
 Fraction fraction = new Fraction(wholeNumber, 1);
 String expected = "" + wholeNumber;
 StringBuffer toAppendTo = new StringBuffer();
 format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 String expected = "-1/2";
 StringBuffer toAppendTo = new StringBuffer();
 format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLargeFraction() {
 Fraction fraction = new Fraction(1234567890, 987654321);
 String expected = "1234567890/987654321";
 StringBuffer toAppendTo = new StringBuffer();
 format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1/2";
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "3 1/2";
 Fraction expected = new Fraction(3, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "5";
 Fraction expected = new Fraction(5, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseNegativeFraction() {
 String source = "-1/2";
 Fraction expected = new Fraction(-1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 assertEquals(expected, fraction);
 }

 @Test
 public void testParseFractionWithMultipleWhitespaces() {
 String source = "1 / 2";
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 assertEquals(expected, fraction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFraction() {
 String source = "1 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNegativeDenominator() {
 String source = "1/-2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumber() {
 String source = "1/abc";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 format = new ProperFractionFormat();
 format.setWholeFormat(wholeFormat);
 String expected = "1";
 String source = "1/2";
 Fraction fraction = new Fraction(1, 2);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 String formattedWhole = wholeFormat.format(1);
 String expectedWithWhole = formattedWhole + " " + actual.substring(actual.indexOf(" "));
 assertEquals(expectedWithWhole, actual);
 }
}