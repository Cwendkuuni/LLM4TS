package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.cli.Fraction;
import org.apache.commons.cli.ParsePosition;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.FractionInputParser;
import org.apache.commons.math3.fraction.FractionUtil;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        assertEquals(FractionFormat.getDefaultNumberFormat(), format.getWholeFormat());
    }

    @Test
    public void testProperFractionFormatWithCustomWholeFormat() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertNotNull(format);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testProperFractionFormatFormatMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(2, 3);
        format.format(fraction, toAppendTo, null);
        assertEquals("2/3", toAppendTo.toString());

        StringBuffer toAppendTo2 = new StringBuffer();
        Fraction fraction2 = new Fraction(1, 2);
        format.format(fraction2, toAppendTo2, null);
        assertEquals("1/2", toAppendTo2.toString());

        StringBuffer toAppendTo3 = new StringBuffer();
        Fraction fraction3 = new Fraction(5, 6);
        format.format(fraction3, toAppendTo3, null);
        assertEquals("5/6", toAppendTo3.toString());

        StringBuffer toAppendTo4 = new StringBuffer();
        Fraction fraction4 = new Fraction(4, 1);
        format.format(fraction4, toAppendTo4, null);
        assertEquals("4", toAppendTo4.toString());
        toAppendTo4.append(" ");
        format.format(fraction4, toAppendTo4, null);
        assertEquals("4 1/1", toAppendTo4.toString());
    }

    @Test
    public void testProperFractionFormatParseMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse("2/3", pos);
        assertEquals("2/3", format.format(fraction, null, null));
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(0, pos.getIndex());

        Fraction fraction2 = format.parse("1/2", pos);
        assertEquals("1/2", format.format(fraction2, null, null));
        assertEquals(1, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());
        assertEquals(4, pos.getIndex());

        Fraction fraction3 = format.parse("5/6", pos);
        assertEquals("5/6", format.format(fraction3, null, null));
        assertEquals(5, fraction3.getNumerator());
        assertEquals(6, fraction3.getDenominator());
        assertEquals(9, pos.getIndex());

        Fraction fraction4 = format.parse("4", pos);
        assertEquals("4", format.format(fraction4, null, null));
        assertEquals(4, fraction4.getNumerator());
        assertEquals(1, fraction4.getDenominator());
        assertEquals(5, pos.getIndex());

        Fraction fraction5 = format.parse("4 1/1", pos);
        assertEquals("4 1/1", format.format(fraction5, null, null));
        assertEquals(4, fraction5.getNumerator());
        assertEquals(1, fraction5.getDenominator());
        assertEquals(10, pos.getIndex());

        Fraction fraction6 = format.parse("4 1/2", pos);
        assertEquals("4 1/2", format.format(fraction6, null, null));
        assertEquals(4, fraction6.getNumerator());
        assertEquals(2, fraction6.getDenominator());
        assertEquals(12, pos.getIndex());

        Fraction fraction7 = format.parse("2/3.5", pos);
        assertNull(fraction7);
        assertEquals(11, pos.getIndex());

        Fraction fraction8 = format.parse("2/3x", pos);
        assertNull(fraction8);
        assertEquals(6, pos.getIndex());

        Fraction fraction9 = format.parse("2/3-", pos);
        assertNull(fraction9);
        assertEquals(3, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatSetWholeFormatMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testProperFractionFormatSetWholeFormatNullArgumentException() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}