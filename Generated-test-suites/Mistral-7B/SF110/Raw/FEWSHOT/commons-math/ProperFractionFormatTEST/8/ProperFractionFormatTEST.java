package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNull(format.getWholeFormat());
        assertSame(FractionFormat.getDefaultNumberFormat(), format.getNumeratorFormat());
        assertSame(FractionFormat.getDefaultNumberFormat(), format.getDenominatorFormat());

        NumberFormat wholeFormat = new DecimalFormat("#", Locale.US);
        ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat);
        assertSame(wholeFormat, formatWithWholeFormat.getWholeFormat());
        assertEquals(new DecimalFormat("#").format(1.5), formatWithWholeFormat.format(new Fraction(1, 2), new StringBuffer(), null).toString());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction1, toAppendTo, pos);
        assertEquals("/1 2 /", toAppendTo.toString());

        fraction1.setDenominator(3);
        toAppendTo.setLength(0);
        format.format(fraction1, toAppendTo, pos);
        assertEquals("/1 3 /", toAppendTo.toString());

        Fraction fraction2 = new Fraction(BigInteger.valueOf(5), BigDecimal.valueOf(3));
        toAppendTo.setLength(0);
        format.format(fraction2, toAppendTo, pos);
        assertEquals("/5 3 /", toAppendTo.toString());

        Fraction fraction3 = new Fraction(-1, 2);
        toAppendTo.setLength(0);
        format.format(fraction3, toAppendTo, pos);
        assertEquals("/-1 2 /", toAppendTo.toString());

        Fraction fraction4 = new Fraction(0, 3);
        toAppendTo.setLength(0);
        format.format(fraction4, toAppendTo, pos);
        assertNull(toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = format.parse("1/2", pos);
        assertEquals(new Fraction(1, 2), fraction1);
        assertEquals(4, pos.getIndex());

        fraction1 = format.parse("1/3", pos);
        assertEquals(new Fraction(1, 3), fraction1);
        assertEquals(5, pos.getIndex());

        Fraction fraction2 = format.parse("5/3", pos);
        assertEquals(new Fraction(5, 3), fraction2);
        assertEquals(8, pos.getIndex());

        Fraction fraction3 = format.parse("-1/2", pos);
        assertEquals(new Fraction(-1, 2), fraction3);
        assertEquals(7, pos.getIndex());

        Fraction fraction4 = format.parse("0/3", pos);
        assertNull(fraction4);
        assertEquals(0, pos.getIndex());

        Fraction fraction5 = format.parse("1 2 /", pos);
        assertNull(fraction5);
        assertEquals(0, pos.getIndex());

        fraction5 = format.parse("1 / 2", pos);
        assertEquals(new Fraction(1, 2), fraction5);
        assertEquals(5, pos.getIndex());

        fraction5 = format.parse("1 / 2 3 /", pos);
        assertNull(fraction5);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        NumberFormat wholeFormat = new DecimalFormat("#,##0.#", Locale.US);
        format.setWholeFormat(wholeFormat);
        assertSame(wholeFormat, format.getWholeFormat());

        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("/1, 0 2 /", toAppendTo.toString());
    }
}