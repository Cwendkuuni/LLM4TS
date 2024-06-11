package org.apache.commons.math3.fraction;

import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        // Edge case: test with null NumberFormat
        ProperFractionFormat properFractionFormatNull = new ProperFractionFormat(null);
        assertThrows(NullArgumentException.class, () -> properFractionFormatNull);
    }

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        Assert.assertEquals("1/2", actual);
    }

    @Test
    public void testProperFractionFormatWithCustomNumberFormat() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        Assert.assertEquals("3/4", actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testProperFractionFormatWithNullNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(null, new DecimalFormat("#"), new DecimalFormat("#"));
    }

    @Test
    public void testParse() {
        String input = "1 2/3";
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(1, 3);
        Fraction actual = properFractionFormat.parse(input, pos);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithInvalidInput() {
        String input = "1 2/0";
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(input, pos);
        Assert.assertNull(actual);
        Assert.assertEquals(3, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidCharacter() {
        String input = "1 a/2";
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = properFractionFormat.parse(input, pos);
        Assert.assertNull(actual);
        Assert.assertEquals(1, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("#");
        properFractionFormat.setWholeFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }
}