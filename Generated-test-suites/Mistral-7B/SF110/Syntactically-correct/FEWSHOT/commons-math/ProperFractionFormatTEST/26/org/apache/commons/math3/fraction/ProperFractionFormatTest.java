package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
    }

    @Test
    public void testConstructorWithFormat() {
        DecimalFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testFormatPositiveFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#.##");
        NumberFormat numeratorFormat = new DecimalFormat("#.##");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));

        assertEquals("2/3", toAppendTo.toString());
    }

    @Test
    public void testFormatZeroFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(0, 1);
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));

        assertEquals("0", toAppendTo.toString());
    }

    @Test
    public void testFormatNegativeFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#.##");
        NumberFormat numeratorFormat = new DecimalFormat("#.##");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(-3, 2);
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));

        assertEquals("-3/2", toAppendTo.toString());
    }

    @Test
    public void testParsePositiveFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#.##");
        DecimalFormat numeratorFormat = new DecimalFormat("#.##");
        DecimalFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("2/3", new ParsePosition(0));
        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void testParseZeroFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#");
        DecimalFormat numeratorFormat = new DecimalFormat("#");
        DecimalFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("0", new ParsePosition(0));
        assertNull(fraction);
    }

    @Test
    public void testParseNegativeFraction() {
        DecimalFormat wholeFormat = new DecimalFormat("#.##");
        DecimalFormat numeratorFormat = new DecimalFormat("#.##");
        DecimalFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("-3/2", new ParsePosition(0));
        assertEquals(new Fraction(-3, 2), fraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}