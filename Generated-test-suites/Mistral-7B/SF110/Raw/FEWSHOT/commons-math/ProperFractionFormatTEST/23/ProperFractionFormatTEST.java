package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertNotNull(pff);

        ProperFractionFormat pffCustom = new ProperFractionFormat(NumberFormatUtils.createNumberFormat("#,##0.##"));
        assertNotNull(pffCustom);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        StringBuffer sb = new StringBuffer(100);

        // Test fraction with no whole part
        Fraction fraction = new Fraction(1, 2);
        pff.format(fraction, sb, null);
        assertEquals("1 / 2", sb.toString());

        // Test fraction with whole part
        fraction = new Fraction(4, 2);
        pff.format(fraction, sb, null);
        assertEquals("2 1 / 2", sb.toString());

        // Test fraction with negative sign
        fraction = new Fraction(-3, 2);
        pff.format(fraction, sb, null);
        assertEquals("-1 1 / 2", sb.toString());

        // Test fraction with large numerator and denominator
        fraction = new Fraction(12345, 6789);
        pff.format(fraction, sb, null);
        assertEquals("1948 1 / 6789", sb.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat pff = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction;

        // Test parsing simple fractions
        fraction = pff.parse("2/3", pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(4, pos.getIndex());

        fraction = pff.parse("1 3/4", pos);
        assertEquals(new Fraction(1, 4), fraction);
        assertEquals(5, pos.getIndex());

        fraction = pff.parse("-1 2/3", pos);
        assertEquals(new Fraction(-1, 3), fraction);
        assertEquals(7, pos.getIndex());

        // Test parsing fractions with whole part
        fraction = pff.parse("2", pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        fraction = pff.parse("2 1/2", pos);
        assertEquals(new Fraction(2, 2), fraction);
        assertEquals(4, pos.getIndex());

        // Test parsing fractions with invalid input
        fraction = pff.parse("2/0", pos);
        assertNull(fraction);
        assertEquals(-1, pos.getIndex());

        fraction = pff.parse("2/three", pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        fraction = pff.parse("2/", pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        fraction = pff.parse("2//3", pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();

        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.##");
        pff.setWholeFormat(wholeFormat);

        StringBuffer sb = new StringBuffer(100);
        Fraction fraction = new Fraction(1, 2);
        pff.format(fraction, sb, null);

        assertEquals("2,001 1 / 2", sb.toString());
    }
}