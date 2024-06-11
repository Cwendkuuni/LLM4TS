package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("0 / 1", format.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1", format.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("2 / 1", format.format(new Fraction(2, 1), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 / 2", format.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("3 / 2", format.format(new Fraction(3, 2), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 / 3", format.format(new Fraction(1, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 / 4", format.format(new Fraction(1, 4), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("5 / 4", format.format(new Fraction(5, 4), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 / 5", format.format(new Fraction(1, 5), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("6 / 5", format.format(new Fraction(6, 5), new StringBuffer(), new FieldPosition(0, 0)));

        ProperFractionFormat formatWithCustomWholeFormat = new ProperFractionFormat(new DecimalFormat("#"));
        assertEquals("0", formatWithCustomWholeFormat.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", formatWithCustomWholeFormat.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("2", formatWithCustomWholeFormat.format(new Fraction(2, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", formatWithCustomWholeFormat.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("3", formatWithCustomWholeFormat.format(new Fraction(3, 2), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", formatWithCustomWholeFormat.format(new Fraction(1, 3), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", formatWithCustomWholeFormat.format(new Fraction(1, 4), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("5", formatWithCustomWholeFormat.format(new Fraction(5, 4), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", formatWithCustomWholeFormat.format(new Fraction(1, 5), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("6", formatWithCustomWholeFormat.format(new Fraction(6, 5), new StringBuffer(), new FieldPosition(0, 0)).toString());
    }

    @Test
    public void testFormatWithWholeAndNonZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("2 / 3", format.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("-2 / 3", format.format(new Fraction(-2, 3), new StringBuffer(), new FieldPosition(0, 0)));
    }

    @Test
    public void testParseWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("2", new ParsePosition(0));
        assertEquals(new Fraction(2, 1), result);

        result = format.parse("2 / 3", new ParsePosition(0));
        assertEquals(new Fraction(2, 3), result);

        try {
            Fraction result = format.parse("2 3", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }

        try {
            Fraction result = format.parse("2 / 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithWholeAndNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("2 1 / 3", new ParsePosition(0));
        assertEquals(new Fraction(2, 3), result);

        result = format.parse("-2 1 / 3", new ParsePosition(0));
        assertEquals(new Fraction(-2, 3), result);

        try {
            Fraction result = format.parse("2 1 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }

        try {
            Fraction result = format.parse("2 / 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithWholeNumberAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("2 1/3", new ParsePosition(0));
        assertEquals(new Fraction(2, 3), result);

        try {
            Fraction result = format.parse("2 1 / 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithWholeNonZeroAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("2 1/3", new ParsePosition(0));
        assertEquals(new Fraction(2, 3), result);

        result = format.parse("-2 1/3", new ParsePosition(0));
        assertEquals(new Fraction(-2, 3), result);

        try {
            Fraction result = format.parse("2 1 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }

        try {
            Fraction result = format.parse("2 / 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithWholeZeroAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction result = format.parse("0 1/3", new ParsePosition(0));
        assertEquals(new Fraction(0, 1), result);

        try {
            Fraction result = format.parse("0 1 / 3 / 4", new ParsePosition(0));
            fail();
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}