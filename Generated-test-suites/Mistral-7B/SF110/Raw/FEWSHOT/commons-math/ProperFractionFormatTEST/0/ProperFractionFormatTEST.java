package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat, numeratorFormat, denominatorFormat;

 @org.junit.Before
 public void setUp() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @org.junit.Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 format.format(fraction, toAppendTo, pos);
 assertEquals("3 / 4", toAppendTo.toString());

 fraction = new Fraction(1, 2);
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, pos);
 assertEquals("1 / 2", toAppendTo.toString());

 fraction = new Fraction(5, 1);
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, pos);
 assertEquals("5", toAppendTo.toString());
 }

 @org.junit.Test
 public void testGetWholeFormat() {
 assertEquals(wholeFormat, format.getWholeFormat());
 }

 @org.junit.Test
 public void testParse() {
 ParsePosition pos = new ParsePosition(0);
 // Test empty string
 assertEquals(null, format.parse("", pos));
 pos.setIndex(0);

 // Test whole number
 assertEquals(new Fraction(1, 1), format.parse("1", pos));

 // Test proper fraction
 assertEquals(new Fraction(1, 2), format.parse("1/2", pos));

 // Test improper fraction
 assertEquals(new Fraction(3, 2), format.parse("1 1/2", pos));

 // Test mixed number
 assertEquals(new Fraction(1, 2) , format.parse("1 1/2", pos));
 assertEquals(new Fraction(3, 2), format.parse("1 1 1/2", pos));

 // Test negative sign
 assertEquals(new Fraction(-1, 2), format.parse("-1/2", pos));

 // Test invalid input
 assertNull(format.parse("1/0", pos));
 assertNull(format.parse("1/-2", pos));
 assertNull(format.parse("1 a/b", pos));
 assertNull(format.parse("-1 a/b", pos));
 }

 @org.junit.Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 format.setWholeFormat(null);
 }
}