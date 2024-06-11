package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatEmpty() {
        ProperFractionFormat format = new ProperFractionFormat();
        String result = format.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("", result.toString());
    }

    @Test
    public void testProperFractionFormatZero() {
        ProperFractionFormat format = new ProperFractionFormat();
        String result = format.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("0", result.toString());
    }

    @Test
    public void testProperFractionFormatOne() {
        ProperFractionFormat format = new ProperFractionFormat();
        String result = format.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("1", result.toString());
    }

    @Test
    public void testProperFractionFormatNegativeOne() {
        ProperFractionFormat format = new ProperFractionFormat();
        String result = format.format(new Fraction(-1, 1), new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("-1", result.toString());
    }

    @Test
    public void testProperFractionFormatWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat newFormat = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(7, 3);
        String result = newFormat.format(fraction, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("2 1/3", result.toString());
    }

    @Test
    public void testProperFractionFormatParseSimple() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(2, 3), result);
        assertEquals(4, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(2, 1), result);
        assertEquals(2, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(2, 1, new int[]{3, 4}), result);
        assertEquals(7, pos.getIndex());
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "two";
        ParsePosition pos = new ParsePosition(0);
        format.parse(source, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "2/3/4";
        ParsePosition pos = new ParsePosition(0);
        format.parse(source, pos);
    }

    @Test(expected = NullArgumentException.class)
    public void testProperFractionFormatSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}