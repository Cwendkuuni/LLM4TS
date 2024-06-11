package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final FractionFormat PROPER_FORMAT = new ProperFractionFormat();
 private static final NumberFormat WHOLE_FORMAT = new DecimalFormat("#");

 @Test
 public void testProperFractionFormat_simple() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "1 2/3";
 String actual = PROPER_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormat_wholePart_zero() {
 Fraction fraction = new Fraction(0, 3);
 String expected = "";
 String actual = PROPER_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormat_wholePart_positive() {
 Fraction fraction = new Fraction(4, 3);
 String expected = "1 3/4";
 String actual = PROPER_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormat_wholePart_negative() {
 Fraction fraction = new Fraction(-4, 3);
 String expected = "-1 3/4";
 String actual = PROPER_FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormat_nullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null);
 // This line should throw a NullArgumentException
 format.format(new Fraction(2, 3), new StringBuffer(), null);
 }

 @Test
 public void testParse_simple() {
 Fraction expected = new Fraction(2, 3);
 Fraction actual = PROPER_FORMAT.parse("1 2/3", new ParsePosition(0, null)).getNumber();
 assertEquals(expected, actual);
 }

 @Test(expected = NullArgumentException.class)
 public void testParse_nullSource() {
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat();
 // This line should throw a NullArgumentException
 format.parse(null, pos);
 }

 @Test(expected = NullArgumentException.class)
 public void testParse_nullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, new DecimalFormat("#"), new DecimalFormat("#"));
 // This line should throw a NullArgumentException
 format.parse("1 2/3", new ParsePosition(0, null));
 }

 @Test(expected = NullArgumentException.class)
 public void testParse_nullNumeratorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, null, new DecimalFormat("#"));
 // This line should throw a NullArgumentException
 format.parse("1 2/3", new ParsePosition(0, null));
 }

 @Test(expected = NullArgumentException.class)
 public void testParse_nullDenominatorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), null);
 // This line should throw a NullArgumentException
 format.parse("1 2/3", new ParsePosition(0, null));
 }

 @Test
 public void testParse_leadingWhitespace() {
 Fraction expected = new Fraction(2, 3);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "  1 2/3";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_trailingWhitespace() {
 Fraction expected = new Fraction(2, 3);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 2/3   ";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_wholePart_positive() {
 Fraction expected = new Fraction(4, 3);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 4/3";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_wholePart_negative() {
 Fraction expected = new Fraction(-4, 3);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "-1 4/3";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_denominator_zero() {
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 2/0";
 Fraction actual = format.parse(source, pos);
 assertNull(actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_denominator_negative() {
 Fraction expected = new Fraction(-2, 3);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 2/-3";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed() {
 Fraction expected = new Fraction(3, 4);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "2 3/4";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_leadingWhitespace() {
 Fraction expected = new Fraction(3, 4);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "  2 3/4";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_trailingWhitespace() {
 Fraction expected = new Fraction(3, 4);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "2 3/4   ";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "2 5/8";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits_leadingWhitespace() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "  2 5/8";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits_trailingWhitespace() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "2 5/8   ";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits_denominatorLeadingZero() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "2 0 5/8";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits_denominatorLeadingZero_leadingWhitespace() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "  0 5/8";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_multipleDigits_denominatorLeadingZero_trailingWhitespace() {
 Fraction expected = new Fraction(3, 8);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "0 5/8   ";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_fractionWithLargerNumeratorAndDenominator() {
 Fraction expected = new Fraction(11, 12);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 11/12";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_fractionWithLargerNumeratorAndDenominator_leadingWhitespace() {
 Fraction expected = new Fraction(11, 12);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "  1 11/12";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_mixed_fractionWithLargerNumeratorAndDenominator_trailingWhitespace() {
 Fraction expected = new Fraction(11, 12);
 ParsePosition pos = new ParsePosition(0, null);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, new DecimalFormat("#"), new DecimalFormat("#"));
 String source = "1 11/12   ";
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(source.length());
 }

 @Test
 public void testParse_simple_withPrecision() {
 Fraction expected = new Fraction(2, 3);
 Fraction actual = PROPER_FORMAT.parse("1 2/3", new ParsePosition(0, null));
 assertEquals(expected, actual);
 assertEquals(expected.toString(), PROPER_FORMAT.format(expected, new StringBuffer(), null).toString());
 Double expectedValue = (double) expected.getNumerator() / (double) expected.getDenominator();
 Double actualValue = Precision.round(Double.parseDouble(PROPER_FORMAT.format(expected, new StringBuffer(), null).toString()), 3);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParse_mixed_withPrecision() {
 Fraction expected = new Fraction(3, 4);
 ParsePosition pos = new ParsePosition(0, null);
 Fraction actual = PROPER_FORMAT.parse("2 3/4", pos);
 assertEquals(expected, actual);
 Double expectedValue = (double) expected.getNumerator() / (double) expected.getDenominator();
 Double actualValue = Precision.round(Double.parseDouble(PROPER_FORMAT.format(expected, new StringBuffer(), null).toString()), 3);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParse_wholePart_withPrecision() {
 Fraction expected = new Fraction(4, 1);
 ParsePosition pos = new ParsePosition(0, null);
 Fraction actual = PROPER_FORMAT.parse("4", pos);
 assertEquals(expected, actual);
 Double expectedValue = (double) expected.getNumerator() / (double) expected.getDenominator();
 Double actualValue = Precision.round(Double.parseDouble(PROPER_FORMAT.format(expected, new StringBuffer(), null).toString()), 3);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParse_negativeFraction_withPrecision() {
 Fraction expected = new Fraction(-3, 4);
 ParsePosition pos = new ParsePosition(0, null);
 Fraction actual = PROPER_FORMAT.parse("-2 3/4", pos);
 assertEquals(expected, actual);
 Double expectedValue = (double) expected.getNumerator() / (double) expected.getDenominator();
 Double actualValue = Precision.round(Double.parseDouble(PROPER_FORMAT.format(expected, new StringBuffer(), null).toString()), 3);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testParse_mixedFractionWithLargerNumeratorAndDenominator_withPrecision() {
 Fraction expected = new Fraction(11, 12);
 ParsePosition pos = new ParsePosition(0, null);
 Fraction actual = PROPER_FORMAT.parse("1 11/12", pos);
 assertEquals(expected, actual);
 Double expectedValue = (double) expected.getNumerator() / (double) expected.getDenominator();
 Double actualValue = Precision.round(Double.parseDouble(PROPER_FORMAT.format(expected, new StringBuffer(), null).toString()), 3);
 assertEquals(expectedValue, actualValue);
 }
}