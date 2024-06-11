package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private FractionFormat properFormat;
 private FractionFormat defaultFormat;

 @Before
 public void setup() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 DecimalFormat numeratorFormat = new DecimalFormat("#");
 DecimalFormat denominatorFormat = new DecimalFormat("#");
 properFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 defaultFormat = FractionFormat.getDefaultFormat();
 }

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "2/3";
 String actual = new StringBuffer().append(properFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(4, 3);
 String expected = "1 1/3";
 String actual = new StringBuffer().append(properFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 Fraction fraction = new Fraction(4);
 String expected = "4";
 String actual = new StringBuffer().append(properFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = ArithmeticException.class)
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-2, 3);
 StringBuffer toAppendTo = new StringBuffer();
 String actual = new StringBuffer().append(properFormat.format(fraction, toAppendTo, null)).toString();
 fail("Expected ArithmeticException when formatting negative fraction");
 }

 @Test
 public void testParseProperFraction() {
 Fraction fraction = properFormat.parse("2/3", new ParsePosition(0, 0));
 assertEquals(new Fraction(2, 3), fraction);
 }

 @Test
 public void testParseMixedFraction() {
 Fraction fraction = properFormat.parse("1 1/3", new ParsePosition(0, 0));
 assertEquals(new Fraction(1, 3), fraction.getNumerator());
 assertEquals(new Fraction(1, 3), fraction.getDenominator());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMissingNumerator() {
 Fraction fraction = properFormat.parse("3/", new ParsePosition(0, 0));
 fail("Expected NumberFormatException when parsing missing numerator");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMissingDenominator() {
 Fraction fraction = properFormat.parse("2/", new ParsePosition(0, 0));
 fail("Expected NumberFormatException when parsing missing denominator");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumber() {
 Fraction fraction = properFormat.parse("2 a/3", new ParsePosition(0, 0));
 fail("Expected NumberFormatException when parsing invalid number");
 }

 @Test
 public void testParseWholeNumber() {
 Fraction fraction = properFormat.parse("4", new ParsePosition(0, 0));
 assertEquals(new Fraction(4), fraction);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseNegativeFraction() {
 Fraction fraction = properFormat.parse("-2/3", new ParsePosition(0, 0));
 fail("Expected NumberFormatException when parsing negative fraction");
 }

 @Test
 public void testFormatDefaultFormat() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "2/3";
 String actual = new StringBuffer().append(defaultFormat.format(fraction, null, null)).toString();
 assertNotEquals(expected, actual);
 }

 @Test
 public void testParseDefaultFormat() {
 Fraction fraction = defaultFormat.parse("2/3", new ParsePosition(0, 0));
 assertNotEquals(new Fraction(2, 3), fraction);
 }

 @Test
 public void testSetWholeFormat() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat properFormat = new ProperFractionFormat(null, numeratorFormat, denominatorFormat);
 properFormat.setWholeFormat(wholeFormat);
 String expected = "4";
 String actual = new StringBuffer().append(properFormat.format(new Fraction(4), null, null)).toString();
 assertEquals(expected, actual);
 }
}