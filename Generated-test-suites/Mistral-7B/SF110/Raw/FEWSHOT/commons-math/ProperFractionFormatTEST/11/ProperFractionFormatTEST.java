package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test default constructor
        assertEquals("1 / 2", format.format(new Fraction(1, 2), new StringBuffer(), null));

        // Test constructor with NumberFormat
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "0");
        ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat);
        assertEquals("1", formatWithWholeFormat.format(new Fraction(1, 1), new StringBuffer(), null));
        assertEquals("1 / 2", formatWithWholeFormat.format(new Fraction(1, 2), new StringBuffer(), null));
        assertEquals("3 / 2", formatWithWholeFormat.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals("4 / 3", formatWithWholeFormat.format(new Fraction(4, 3), new StringBuffer(), null));

        // Test parse method
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 / 2", pos);
        assertEquals(new Fraction(1, 2), fraction);
        pos.setIndex(pos.getIndex() + 2);
        fraction = format.parse("3 / 2", pos);
        assertEquals(new Fraction(3, 2), fraction);
        fraction = format.parse("1", pos);
        assertEquals(new Fraction(1, 1), fraction);
        fraction = format.parse("1 / 3", pos);
        assertEquals(new Fraction(1, 3), fraction);
        fraction = format.parse("4 / 3", pos);
        assertEquals(new Fraction(2, 3), fraction);

        // Test parse with leading whitespace
        fraction = format.parse(" 1 / 2", pos);
        assertEquals(new Fraction(1, 2), fraction);

        // Test parse with trailing whitespace
        fraction = format.parse("1 / 2 ", pos);
        assertEquals(new Fraction(1, 2), fraction);

        // Test parse with invalid input
        pos.setIndex(0);
        fraction = format.parse("1 / 2a", pos);
        assertNull(fraction);
        assertEquals(3, pos.getIndex());

        // Test parse with negative numerator
        pos.setIndex(0);
        fraction = format.parse("-1 / 2", pos);
        assertEquals(new Fraction(-1, 2), fraction);
        assertEquals(3, pos.getIndex());

        // Test parse with negative denominator
        pos.setIndex(0);
        fraction = format.parse("1 / -2", pos);
        assertEquals(new Fraction(-1, 2), fraction);
        assertEquals(3, pos.getIndex());

        // Test parse with large numerator and denominator
        pos.setIndex(0);
        BigDecimal numerator = new BigDecimal("123456789012345678901234567890");
        BigDecimal denominator = new BigDecimal("987654321098765432109876543210");
        Fraction largeFraction = new Fraction(numerator.intValueExact(), denominator.intValueExact());
        fraction = format.parse(largeFraction.toString(), pos);
        assertEquals(largeFraction, fraction);
        assertEquals(largeFraction.toString().length() + pos.getIndex(), pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "0");
        format.setWholeFormat(wholeFormat);

        assertEquals("1", format.format(new Fraction(1, 1), new StringBuffer(), null).toString());
        assertEquals("1 / 2", format.format(new Fraction(1, 2), new StringBuffer(), null).toString());
        assertEquals("3 / 2", format.format(new Fraction(3, 2), new StringBuffer(), null).toString());
        assertEquals("4 / 3", format.format(new Fraction(4, 3), new StringBuffer(), null).toString());
    }
}