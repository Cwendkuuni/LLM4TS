package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.FractionFormat.DefaultNumberFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertNotNull(pff);

        ProperFractionFormat pffWithCustomFormats = new ProperFractionFormat(
                NumberFormatUtils.createNumberFormat("#,##0"),
                DefaultNumberFormat.getInstance(),
                DefaultNumberFormat.getInstance());
        assertNotNull(pffWithCustomFormats);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        // Test with no whole part
        String expected = "1/2";
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(1, 2);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expected, toAppendTo.toString());

        // Test with a whole part
        toAppendTo = new StringBuffer();
        fraction = new Fraction(4, 2);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "2 1/2";
        assertEquals(expected, toAppendTo.toString());

        // Test with a negative whole part
        toAppendTo = new StringBuffer();
        fraction = new Fraction(-3, 2);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "-1 1/2";
        assertEquals(expected, toAppendTo.toString());

        // Test with a large fraction
        toAppendTo = new StringBuffer();
        fraction = new Fraction(987654321, 314159265);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "3123529.865255112 11754922067081600 / 1000000000000";
        assertEquals(expected, toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat pff = new ProperFractionFormat();

        // Test parsing a simple fraction
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(1, 2);
        Fraction actual = pff.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing a fraction with a whole part
        source = "2 1/2";
        expected = new Fraction(2, 2);
        actual = pff.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing a negative fraction with a whole part
        source = "-1 1/2";
        expected = new Fraction(-1, 2);
        actual = pff.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing a large fraction
        source = "3123529.865255112 11754922067081600 / 1000000000000";
        expected = new Fraction(3123529865255112L, 1000000000000L);
        actual = pff.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing an invalid string
        source = "abc";
        assertNull(pff.parse(source, pos));
        assertEquals(0, pos.getIndex());

        // Test parsing a string with extra whitespace
        source = " 1 / 2 ";
        expected = new Fraction(1, 2);
        actual = pff.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing a string without a separator
        source = "1/2/";
        assertNull(pff.parse(source, pos));
        assertEquals(0, pos.getIndex());

        // Test parsing a string with an extra separator
        source = "1/2/";
        assertNull(pff.parse(source, pos));
        assertEquals(0, pos.getIndex());

        // Test parsing a string with an empty numerator
        source = "0 / 1";
        assertNull(pff.parse(source, pos));
        assertEquals(0, pos.getIndex());

        // Test parsing a string with a negative numerator
        source = "-1 / 1";
        Fraction actualNegativeNumerator = pff.parse(source, pos);
        assertEquals(new Fraction(-1, 1), actualNegativeNumerator);
        assertEquals(source.length(), pos.getIndex());

        // Test parsing a string with a negative denominator
        source = "1 / -1";
        Fraction actualNegativeDenominator = pff.parse(source, pos);
        assertEquals(new Fraction(1, -1), actualNegativeDenominator);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        // Test setting the whole format to null
        NumberFormat wholeFormat = null;
        try {
            pff.setWholeFormat(wholeFormat);
        } catch (NullArgumentException e) {
            assertEquals("WHOLE_FORMAT", e.getKey());
            assertEquals(new Object[0], e.getMessageArguments());
        }

        // Test setting the whole format to a valid NumberFormat
        NumberFormat validWholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        pff.setWholeFormat(validWholeFormat);
        assertEquals(validWholeFormat, pff.getWholeFormat());

        // Test setting the whole format to the same instance
        NumberFormat sameInstanceWholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        pff.setWholeFormat(sameInstanceWholeFormat);
        assertEquals(sameInstanceWholeFormat, pff.getWholeFormat());

        // Test setting the whole format to a different instance
        NumberFormat differentInstanceWholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        pff.setWholeFormat(differentInstanceWholeFormat);
        assertNotSame(sameInstanceWholeFormat, pff.getWholeFormat());
    }
}