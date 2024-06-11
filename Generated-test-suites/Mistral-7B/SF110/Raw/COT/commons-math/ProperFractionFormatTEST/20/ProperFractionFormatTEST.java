package org.junit.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat defaultNumberFormat;

 @Before
 public void setup() {
 properFractionFormat = new ProperFractionFormat();
 defaultNumberFormat = new DecimalFormat();
 }

 // Test constructor with no arguments
 @Test
 public void testProperFractionFormatConstructorNoArgs() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertNotNull(pff);
 }

 // Test constructor with NumberFormat argument
 @Test
 public void testProperFractionFormatConstructorNumberFormat() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat);
 assertNotNull(pff);
 assertEquals(wholeFormat, pff.getWholeFormat());
 }

 // Test format method with valid fraction
 @Test
 public void testFormatValidFraction() {
 Fraction fraction = new Fraction(3, 4);
 String expected = "3 / 4";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 // Test format method with fraction containing whole part
 @Test
 public void testFormatFractionWithWholePart() {
 Fraction fraction = new Fraction(5, 2);
 String expected = "5 ";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 // Test format method with negative fraction
 @Test(expected = MathIllegalArgumentException.class)
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-2, 3);
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 fail("Expected MathIllegalArgumentException");
 }

 // Test parse method with valid input
 @Test
 public void testParseValidInput() {
 String input = "3 / 4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, pos);
 assertEquals(new Fraction(3, 4), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 // Test parse method with input containing only whole part
 @Test
 public void testParseInputWithOnlyWholePart() {
 String input = "5";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, pos);
 assertEquals(new Fraction(5, 1), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 // Test parse method with input containing fraction with negative denominator
 @Test(expected = MathIllegalArgumentException.class)
 public void testParseInputWithNegativeDenominator() {
 String input = "3 / -2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, pos);
 fail("Expected MathIllegalArgumentException");
 }

 // Test parse method with input containing unparseable fraction
 @Test(expected = NumberFormatException.class)
 public void testParseInputWithUnparseableFraction() {
 String input = "3 / 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(input, pos);
 fail("Expected NumberFormatException");
 }

 // Test setWholeFormat method with null argument
 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullArgument() {
 ProperFractionFormat pff = new ProperFractionFormat();
 try {
 pff.setWholeFormat(null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
 }
}