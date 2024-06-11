package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.commons.math3.fraction.*;
import org.apache.commons.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math3.exception.MathParseException;
import org.junit.Test;

public class ProperFractionFormatTest {

 private NumberFormat numberFormat;
 private ProperFractionFormat properFractionFormat;

 @Test
 public void testProperFractionFormatConstructor() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testProperFractionFormatConstructorWithFormat() {
 numberFormat = new DecimalFormat("#,##0.00");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat);
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testProperFractionFormatConstructorWithThreeFormats() {
 numberFormat = new DecimalFormat("#,##0.00");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 assertNotNull(properFractionFormat);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 } catch (NullArgumentException e) {
 assertEquals("WHOLE_FORMAT", e.getKey());
 return;
 }
 fail("Expected NullArgumentException");
 }

 @Test
 public void testFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 Fraction fraction = new Fraction(BigInteger.valueOf(13), BigInteger.valueOf(4));
 StringBuffer toAppendTo = new StringBuffer(128);
 FieldPosition pos = new FieldPosition(0, 0);
 String formattedFraction = properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("13 1/4", formattedFraction.toString());
 }

 @Test
 public void testParse() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 // Test valid fractions
 String source1 = "13 1/4";
 Fraction expected1 = new Fraction(BigInteger.valueOf(13), BigInteger.valueOf(4));
 ParsePosition pos1 = new ParsePosition(0);
 Fraction parsedFraction1 = properFractionFormat.parse(source1, pos1);
 assertEquals(expected1, parsedFraction1);
 assertEquals(source1.length(), pos1.getIndex());

 String source2 = "3 2/3";
 Fraction expected2 = new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(2));
 ParsePosition pos2 = new ParsePosition(0);
 Fraction parsedFraction2 = properFractionFormat.parse(source2, pos2);
 assertEquals(expected2, parsedFraction2);
 assertEquals(source2.length(), pos2.getIndex());

 String source3 = "-1/2";
 Fraction expected3 = new Fraction(BigInteger.valueOf(-1), BigInteger.valueOf(2));
 ParsePosition pos3 = new ParsePosition(0);
 Fraction parsedFraction3 = properFractionFormat.parse(source3, pos3);
 assertEquals(expected3, parsedFraction3);
 assertEquals(source3.length(), pos3.getIndex());

 String source4 = "1/3";
 Fraction expected4 = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(3));
 ParsePosition pos4 = new ParsePosition(0);
 Fraction parsedFraction4 = properFractionFormat.parse(source4, pos4);
 assertEquals(expected4, parsedFraction4);
 assertEquals(source4.length(), pos4.getIndex());

 // Test invalid fractions
 String source5 = "1/0";
 ParsePosition pos5 = new ParsePosition(0);
 Fraction parsedFraction5 = properFractionFormat.parse(source5, pos5);
 assertNull(parsedFraction5);
 assertEquals(0, pos5.getIndex());

 String source6 = "1/1/2";
 ParsePosition pos6 = new ParsePosition(0);
 Fraction parsedFraction6 = properFractionFormat.parse(source6, pos6);
 assertNull(parsedFraction6);
 assertEquals(0, pos6.getIndex());

 String source7 = "123/";
 ParsePosition pos7 = new ParsePosition(0);
 Fraction parsedFraction7 = properFractionFormat.parse(source7, pos7);
 assertNull(parsedFraction7);
 assertEquals(0, pos7.getIndex());

 String source8 = "123/abc";
 ParsePosition pos8 = new ParsePosition(0);
 Fraction parsedFraction8 = properFractionFormat.parse(source8, pos8);
 assertNull(parsedFraction8);
 assertEquals(0, pos8.getIndex());
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testParseWithInvalidWholeNumber() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 String source = "-1/2a";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertNull(parsedFraction);
 assertEquals(0, pos.getIndex());
 }

 @Test(expected = MathParseException.class)
 public void testParseWithInvalidNumerator() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 String source = "1/0a";
 ParsePosition pos = new ParsePosition(0);
 try {
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 } catch (MathParseException e) {
 assertEquals("Not a valid number: 0a", e.getMessage());
 return;
 }
 fail("Expected MathParseException");
 }
}