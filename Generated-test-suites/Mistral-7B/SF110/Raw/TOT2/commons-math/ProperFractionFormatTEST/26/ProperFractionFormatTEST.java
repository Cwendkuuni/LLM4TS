package org.junit.tests;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.DecimalFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat format = new DecimalFormat("#.##");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testConstructorWithThreeFormatObjects() {
        NumberFormat wholeFormat = new DecimalFormat("#.##");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testFormatWithValidFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.format(fraction, toAppendTo, null);
        assertEquals("2/3", toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParseWithValidStringSource() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat format = new DecimalFormat("#.##");
        properFractionFormat.setWholeFormat(format);
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertEquals(format, wholeFormat);
    }

    // Test edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithZeroDenominatorFraction() {
        Fraction fraction = new Fraction(1, 0);
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.format(fraction, toAppendTo, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithZeroNumeratorFraction() {
        Fraction fraction = new Fraction(0, 3);
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.format(fraction, toAppendTo, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyStringSource() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNegativeNumeratorFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String source = "-2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
    }

    @Test(expected = NullPointerException.class)
    public void testFormatWithNullFraction() {
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.format(null, toAppendTo, null);
    }
}