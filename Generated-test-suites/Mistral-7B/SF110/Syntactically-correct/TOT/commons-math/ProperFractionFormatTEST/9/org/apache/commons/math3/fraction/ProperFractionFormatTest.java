package org.apache.commons.math3.fraction;

import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat wholeFormat, numeratorFormat, denominatorFormat;
 private ProperFractionFormat properFractionFormat;

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(3, 4);
 String expected = "1 3/4";
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(2, 5);
 String expected = "0 2/5";
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 int wholeNumber = 10;
 String expected = String.valueOf(wholeNumber);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat format = new ProperFractionFormat(NumberFormatUtils.getNumberInstance());
 format.format(new Fraction(wholeNumber, 1), toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 4);
 Fraction actual = new ProperFractionFormat().parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "0 2/5";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(2, 5);
 Fraction actual = new ProperFractionFormat().parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "10";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(10, 1);
 Fraction actual = new ProperFractionFormat().parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInvalidInput() {
 String source = "1 3/0";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = new ProperFractionFormat().parse(source, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 0);
 pos.setErrorIndex(source.length());
 }

 @Test
 publicvoid testFormatWithCustomWholeNumberFormat() {
 DecimalFormat wholeFormat = new DecimalFormat("0");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, getNumeratorFormat(), getDenominatorFormat());
 Fraction fraction = new Fraction(3, 4);
 String expected = "0 3/4";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithCustomWholeNumberFormat() {
 String source = "0 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 4);
 Fraction actual = new ProperFractionFormat(getWholeFormat(), getNumeratorFormat(), getDenominatorFormat()).parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 publicvoid testParseWithCustomWholeNumberFormatAndInvalidInput() {
 String source = "0 3/0";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = new ProperFractionFormat(getWholeFormat(), getNumeratorFormat(), getDenominatorFormat()).parse(source, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 0);
 pos.setErrorIndex(source.length());
 }

 @Before
 public void setup() {
 wholeFormat = NumberFormatUtils.getNumberInstance();
 numeratorFormat = new DecimalFormat("0");
 denominatorFormat = new DecimalFormat("0");
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

}