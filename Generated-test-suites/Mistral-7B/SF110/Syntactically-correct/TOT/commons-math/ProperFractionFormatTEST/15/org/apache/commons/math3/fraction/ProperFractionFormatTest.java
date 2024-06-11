package org.apache.commons.math3.fraction;

import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberTheoryUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private Fraction fraction;

 @Test
 public void testProperFractionFormatWithDefaults() {
 format = new ProperFractionFormat();
 fraction = new Fraction(1, 2);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("1 / 2", formatted.toString());
 }

 @Test
 public void testProperFractionFormatWithCustomFormats() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractionDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 fraction = new Fraction(1, 3);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("1 1/3", formatted.toString());
 }

 @Test
 public void testProperFractionFormatWithWholePart() {
 format = new ProperFractionFormat();
 fraction = new Fraction(5, 2);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("5", formatted.substring(0, formatted.indexOf(" ")).toString());
 }

 @Test
 public void testProperFractionFormatWithNegativeNumerator() {
 format = new ProperFractionFormat();
 fraction = new Fraction(-2, 3);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("-2 / 3", formatted.toString());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullArgument() {
 format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 } catch (NullArgumentException e) {
 assertEquals("WHOLE_FORMAT", e.getId());
 fail("Expected NullArgumentException to be thrown, but it wasn't.");
 }
 }

 @Test
 public void testParseProperFractionWithWholePart() {
 format = new ProperFractionFormat();
 String source = "5 1/2";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(5, 1), fraction);
 }

 @Test
 public void testParseProperFractionWithNumeratorLessThanZero() {
 format = new ProperFractionFormat();
 String source = "-1 1/2";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(-1, 1), fraction);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullSource() {
 format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 try {
 format.parse(null, pos);
 } catch (NullArgumentException e) {
 assertEquals("SOURCE", e.getId());
 fail("Expected NullArgumentException to be thrown, but it wasn't.");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidSource() {
 format = new ProperFractionFormat();
 String source = "5 a 2";
 ParsePosition pos = new ParsePosition(0);
 try {
 format.parse(source, pos);
 } catch (ParseException e) {
 assertEquals("Error parsing 'a': Unparseable number: Unknown character 'a'", e.getMessage());
 fail("Expected ParseException to be thrown, but it wasn't.");
 }
 }

 @Test
 public void testParseWithMixedFraction() {
 format = new ProperFractionFormat();
 String source = "5 1/2";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(5, 1), fraction);
 }

 @Test
 public void testParseWithImproperFraction() {
 format = new ProperFractionFormat();
 String source = "3 1/3";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(3, 3), fraction);
 }

 @Test
 public void testParseWithSimpleFraction() {
 format = new ProperFractionFormat();
 String source = "1/2";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(1, 2), fraction);
 }

 @Test
 public void testParseWithLargeFraction() {
 format = new ProperFractionFormat();
 String source = "1234567/987654321";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(1234567, 987654321), fraction);
 }

 @Test
 public void testParseWithLargeNumeratorAndDenominator() {
 format = new ProperFractionFormat();
 String source = "123456789012345678901234567890/123456789012345678901234567890123";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(123456789012345678901234567890L, 123456789012345678901234567890123L), fraction);
 }

 @Test
 public void testParseWithLargeFractionWithNegativeSign() {
 format = new ProperFractionFormat();
 String source = "-123456789012345678901234567890/123456789012345678901234567890123";
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(source, pos);
 assertEquals(new Fraction(-123456789012345678901234567890L, 123456789012345678901234567890123L), fraction);
 }

 @Test
 public void testFormatWithLargeFraction() {
 format = new ProperFractionFormat();
 fraction = new Fraction(NumberTheoryUtils.nextPrime(100), NumberTheoryUtils.nextPrime(1000));
 String formatted = format.format(fraction, new StringBuffer(), null).toString();
 assertTrue(Precision.abs(Double.parseDouble(formatted), Fraction.doubleValue(fraction)) < 1.0e-12);
 }
}