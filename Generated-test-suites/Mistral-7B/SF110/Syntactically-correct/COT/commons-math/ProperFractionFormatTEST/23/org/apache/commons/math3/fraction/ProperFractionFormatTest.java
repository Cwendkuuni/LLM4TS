package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

 private ProperFractionFormat pff;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 pff = new ProperFractionFormat();
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 pff.setWholeFormat(wholeFormat);
 }

 @Test
 public void testDefaultConstructor() {
 pff = new ProperFractionFormat();
 assertNotNull(pff.getWholeFormat());
 assertNotNull(pff.getNumeratorFormat());
 assertNotNull(pff.getDenominatorFormat());
 }

 @Test
 public void testConstructorWithFormat() {
 pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertSame(wholeFormat, pff.getWholeFormat());
 assertSame(numeratorFormat, pff.getNumeratorFormat());
 assertSame(denominatorFormat, pff.getDenominatorFormat());
 }

 @Test
 public void testFormatMethod() {
 Fraction fraction = new Fraction(2, 3);
 StringBuffer toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String expected = "2/3";
 assertEquals(expected, toAppendTo.toString());

 fraction = new Fraction(1, 4);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 expected = "1/4";
 assertEquals(expected, toAppendTo.toString());

 fraction = new Fraction(5, 2);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 expected = "5/2";
 assertEquals(expected, toAppendTo.toString());

 fraction = new Fraction(3, 5);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 expected = "3/5";
 assertEquals(expected, toAppendTo.toString());

 fraction = new Fraction(2, 1);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 expected = "2";
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testGetWholeFormat() {
 assertSame(wholeFormat, pff.getWholeFormat());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 pff.setWholeFormat(null);
 }

 @Test
 public void testParseMethod() {
 String source = "2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(2, 3);
 Fraction result = pff.parse(source, pos);
 assertEquals(expected, result);
 assertEquals(0, pos.getIndex());

 source = "1/4";
 pos = new ParsePosition(0);
 expected = new Fraction(1, 4);
 result = pff.parse(source, pos);
 assertEquals(expected, result);
 assertEquals(0, pos.getIndex());

 source = "5/2";
 pos = new ParsePosition(0);
 expected = new Fraction(5, 2);
 result = pff.parse(source, pos);
 assertEquals(expected, result);
 assertEquals(0, pos.getIndex());

 source = "3/5";
 pos = new ParsePosition(0);
 expected = new Fraction(3, 5);
 result = pff.parse(source, pos);
 assertEquals(expected, result);
 assertEquals(0, pos.getIndex());

 source = "2";
 pos = new ParsePosition(0);
 Fraction result2 = pff.parse(source, pos);
 assertNull(result2);
 assertEquals(0, pos.getIndex());

 source = "2/";
 pos = new ParsePosition(0);
 result = pff.parse(source, pos);
 assertNull(result);
 assertEquals(0, pos.getIndex());

 source = "2/3/2";
 pos = new ParsePosition(0);
 result = pff.parse(source, pos);
 assertNull(result);
 assertEquals(0, pos.getIndex());
 }
}