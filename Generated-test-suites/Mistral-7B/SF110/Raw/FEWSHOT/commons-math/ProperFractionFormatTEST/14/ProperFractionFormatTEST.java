package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.*;
import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();

        // Test empty fraction
        Fraction fraction = new Fraction();
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("", toAppendTo.toString());

        // Test fraction with whole number
        fraction = new Fraction(4, 1);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("4", toAppendTo.toString());

        // Test fraction with numerator and denominator
        fraction = new Fraction(1, 2);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("1/2", toAppendTo.toString());

        // Test fraction with negative numerator
        fraction = new Fraction(-2, 1);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("-2/1", toAppendTo.toString());

        // Test fraction with large numerator and denominator
        fraction = new Fraction(12345, 6789);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("2003/3013", toAppendTo.toString());

        // Test fraction with leading whitespace
        toAppendTo.setLength(0);
        fraction = new Fraction(1, 2);
        pff.format(fraction, toAppendTo, new FieldPosition(3, 0));
        assertEquals("  1/2", toAppendTo.toString());

        // Test fraction with trailing whitespace
        toAppendTo.setLength(0);
        pff.format(fraction, toAppendTo, new FieldPosition(0, 3));
        assertEquals("1/2   ", toAppendTo.toString());
    }

    @Test
    public void testParse() throws ParseException {
        ProperFractionFormat pff = new ProperFractionFormat();

        // Test empty string
        String source = "";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        assertNull(fraction);

        // Test string with only a whole number
        source = "4";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(4, 1), fraction);

        // Test string with a fraction
        source = "1/2";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);

        // Test string with a negative numerator
        source = "-1/2";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(-1, 2), fraction);

        // Test string with a large fraction
        source = "12345/6789";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(12345, 6789), fraction);

        // Test string with leading whitespace
        source = "  1/2";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);

        // Test string with trailing whitespace
        source = "1/2   ";
        fraction = pff.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);

        // Test string with an illegal character
        source = "1/2a";
        fraction = pff.parse(source, pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        // Test string with an illegal fraction
        source = "1/0";
        fraction = pff.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        // Test setting null whole format
        try {
            pff.setWholeFormat(null);
        } catch (NullArgumentException e) {
            assertEquals("WHOLE_FORMAT", e.getKey());
            assertTrue(e.getMessage().isEmpty());
        }
    }
}