package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static final Locale LOCALE = Locale.US;
 private static final NumberFormat WHOLE_FORMAT = NumberFormat.getNumberInstance(LOCALE);
 private static final NumberFormat NUMERATOR_FORMAT = NumberFormat.getNumberInstance(LOCALE);
 private static final NumberFormat DENOMINATOR_FORMAT = NumberFormat.getPercentInstance(LOCALE);
 private static final ProperFractionFormat PROPER_FORMAT = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 1/2";
 StringBuffer toAppendTo = new StringBuffer();
 PROPER_FORMAT.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithWholePart() {
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 2";
 StringBuffer toAppendTo = new StringBuffer();
 PROPER_FORMAT.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithNegativeNumerator() {
 Fraction fraction = new Fraction(-1, 2);
 String expected = "-1 1/2";
 StringBuffer toAppendTo = new StringBuffer();
 PROPER_FORMAT.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithDenominatorGreaterThanOne() {
 Fraction fraction = new Fraction(1, 3);
 String expected = "1/3";
 StringBuffer toAppendTo = new StringBuffer();
 PROPER_FORMAT.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNullFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 PROPER_FORMAT.format(null, toAppendTo, new FieldPosition(0, 0));
 }

 @Test
 public void testParseSimpleFraction() {
 Fraction expected = new Fraction(1, 2);
 String source = "1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = PROPER_FORMAT.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithWholePart() {
 Fraction expected = new Fraction(2, 1);
 String source = "2 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = PROperFractionFormat.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithNegativeNumerator() {
 Fraction expected = new Fraction(-1, 2);
 String source = "-1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = PROPER_FORMAT.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithDenominatorGreaterThanOne() {
 Fraction expected = new Fraction(1, 3);
 String source = "1/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = PROPER_FORMAT.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullFormat() {
 String source = "2 2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(null, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 String source = "2 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullNumeratorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, null, DENOMINATOR_FORMAT);
 String source = "2 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullDenominatorFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, null);
 String source = "2 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = format.parse(source, pos);
 }

 @Test
 public void testParseWithBigDecimalWholePart() {
 BigDecimal whole = new BigDecimal("3.14");
 Fraction expected = new Fraction(3, 1);
 String source = "3.14 1/";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithBigIntegerNumerator() {
 BigInteger numerator = new BigInteger("-5");
 BigInteger denominator = new BigInteger("3");
 Fraction expected = new Fraction(numerator, denominator);
 String source = "-5/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithBigDecimalWholePartAndNumerator() {
 BigDecimal whole = new BigDecimal("2.71828");
 BigDecimal numerator = new BigDecimal("2");
 BigInteger denominator = new BigInteger("3");
 Fraction expected = new Fraction(new BigDecimal(Math.pow(2.71828, 2.0)), denominator);
 String source = "2.71828 2/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithBigDecimalWholePartAndDenominator() {
 BigDecimal whole = new BigDecimal("3");
 BigDecimal numerator = new BigDecimal("1");
 BigDecimal denominator = new BigDecimal("4");
 Fraction expected = new Fraction(numerator, denominator);
 String source = "3 1/4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithMixedBigDecimalAndBigInteger() {
 BigDecimal whole = new BigDecimal("1.5");
 BigDecimal numerator = new BigDecimal("2");
 BigInteger denominator = new BigInteger("3");
 Fraction expected = new Fraction(new BigDecimal(1.5).add(new BigDecimal(2).divide(new BigDecimal(3), BigDecimal.ROUND_HALF_UP)), denominator);
 String source = "1.5 2/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test
 public void testParseWithBigDecimalWholePartAndComplexFraction() {
 BigDecimal whole = new BigDecimal("3.14159265358979323846");
 BigDecimal numerator = new BigDecimal("1").add(new BigDecimal("2").pow(2));
 BigDecimal denominator = new BigDecimal("1").add(new BigDecimal("3").pow(2)).add(new BigDecimal("5").pow(2)).multiply(new BigDecimal("7"));
 Fraction expected = new Fraction(new BigDecimal(whole.add(numerator).divide(denominator, BigDecimal.ROUND_HALF_UP, MathContext.DECIMAL128)), denominator);
 String source = "3.14159265358979323846 1 + 2^2 / (1 + 3^2 + 5^2)^2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction actual = format.parse(source, pos);
 assertEquals(expected, actual);
 pos.setIndex(pos.getIndex() + source.length());
 }
}