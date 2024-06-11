package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstruction() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();
        assertNotNull(fractionFormat.getWholeFormat());
        ProperFractionFormat fractionFormatWithCustomFormats = new ProperFractionFormat(new DecimalFormat("0"));
        assertNotNull(fractionFormatWithCustomFormats.getWholeFormat());
        assertNotSame(fractionFormat.getWholeFormat(), fractionFormatWithCustomFormats.getWholeFormat());
    }

    @Test
    public void testFormatFraction() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();

        Fraction fractionOneThird = new Fraction(1, 3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        fractionFormat.format(fractionOneThird, toAppendTo, pos);
        assertEquals("1/3", toAppendTo.toString());

        Fraction fractionTwoHalfs = new Fraction(2, 2);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        fractionFormat.format(fractionTwoHalfs, toAppendTo, pos);
        assertEquals("1", toAppendTo.toString());
        assertEquals("1", fractionFormat.format(new Fraction(1, 1), toAppendTo, pos).toString());
    }

    @Test
    public void testFormatFractionWithWholePart() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();

        Fraction fractionThreeFifths = new Fraction(3, 5);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        fractionFormat.format(fractionThreeFifths, toAppendTo, pos);
        assertEquals("3 1/5", toAppendTo.toString());

        Fraction fractionSevenSixths = new Fraction(7, 6);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        fractionFormat.format(fractionSevenSixths, toAppendTo, pos);
        assertEquals("1 1/2 + 1/3", toAppendTo.toString());
    }

    @Test
    public void testParseFraction() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();

        String source = "1 2/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 3), parsedFraction);
        assertEquals(2, pos.getIndex());

        source = "3 1/2";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(3, 2), parsedFraction);
        assertEquals(3, pos.getIndex());

        source = "1 2/4";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 4), parsedFraction);
        assertEquals(3, pos.getIndex());

        source = "-1 2/3";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());

        source = "1/-3";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());

        source = "1/-3.5";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());

        source = "1 2/3.5";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 3), parsedFraction);
        assertEquals(6, pos.getIndex());

        source = "1/zero";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());

        source = "1 /2";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), parsedFraction);
        assertEquals(3, pos.getIndex());

        source = "1 /2/3";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());

        source = "1 2/3 / 4 5/6";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction((Math.abs(1) * 6 + 2) * MathUtils.copySign(1, 1), 6), parsedFraction);
        assertEquals(10, pos.getIndex());

        source = "1.5 2/3";
        pos = new ParsePosition(0);

        parsedFraction = fractionFormat.parse(source, pos);
        assertEquals(new Fraction(3, 2), parsedFraction.getNumerator());
        assertEquals(2, parsedFraction.getDenominator());
        assertEquals(1, parsedFraction.getWholeNumber());
        assertEquals(7, pos.getIndex());
    }

    @Test(expected = NumberFormatException.class)
    public void testParseFractionWithInvalidInput() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();

        String source = "1 2/0";
        ParsePosition pos = new ParsePosition(0);

        fractionFormat.parse(source, pos);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();

        try {
            fractionFormat.setWholeFormat(null);
        } catch (NullArgumentException e) {
            assertEquals("WHOLE_FORMAT", e.getKey());
            assertEquals("null", e.getLocalizedMessage());
            throw e;
        }
    }
}