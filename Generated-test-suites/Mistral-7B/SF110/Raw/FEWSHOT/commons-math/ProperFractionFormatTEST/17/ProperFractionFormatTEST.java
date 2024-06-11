package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("2 1/2", format.format(new Fraction(2, 2), new StringBuffer(), null));
        assertEquals("1 3/4", format.format(new Fraction(1, 4), new StringBuffer(), null));
        assertEquals("0", format.format(new Fraction(0, 1), new StringBuffer(), null));
        assertEquals("1", format.format(new Fraction(1, 1), new StringBuffer(), null));
        assertEquals("3/2", format.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals("-1/2", format.format(new Fraction(-1, 2), new StringBuffer(), null));
        assertEquals("2 2/3", format.format(new Fraction(2, 3), new StringBuffer(), null));
        assertEquals("5/8", format.format(new Fraction(5, 8), new StringBuffer(), null));
    }

    @Test
    public void testParsePositive() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);
        fraction = format.parse("1 3/4", new ParsePosition(0));
        assertEquals(new Fraction(1, 4), fraction);
        fraction = format.parse("0", new ParsePosition(0));
        assertNull(fraction);
        fraction = format.parse("1", new ParsePosition(0));
        assertEquals(new Fraction(1, 1), fraction);
        fraction = format.parse("3/2", new ParsePosition(0));
        assertEquals(new Fraction(3, 2), fraction);

        Format wholeFormat1 = new DecimalFormat("#,##0");
        format.setWholeFormat(wholeFormat1);
        fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);

        NumberFormat numeratorFormat1 = new DecimalFormat("#,##0.##");
        format.setNumeratorFormat(numeratorFormat1);
        fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);

        NumberFormat denominatorFormat1 = new DecimalFormat("0#");
        format.setDenominatorFormat(denominatorFormat1);
        fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);

        NumberFormat numeratorFormat2 = new DecimalFormat("# 1/2");
        format.setNumeratorFormat(numeratorFormat2);
        fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);

        NumberFormat denominatorFormat2 = new DecimalFormat("1/##");
        format.setDenominatorFormat(denominatorFormat2);
        fraction = format.parse("2 1/2", new ParsePosition(0));
        assertEquals(new Fraction(2, 2), fraction);
    }

    @Test
    public void testParseNegative() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("-1/2", new ParsePosition(0));
        assertEquals(new Fraction(-1, 2), fraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testParseInvalidWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction;
        ParsePosition pos = new ParsePosition(0);

        fraction = format.parse("1 a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 /", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / /", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / / /", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a / c", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a / c a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);
    }

    @Test
    public void testParseInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction;
        ParsePosition pos = new ParsePosition(0);

        fraction = format.parse("1 / a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a / c", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 a / b a / c a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);
    }

    @Test
    public void testParseInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction;
        ParsePosition pos = new ParsePosition(0);

        fraction = format.parse("1 / a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / a / b", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / a / b a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / a / b a / c", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / a / b a / c a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);
    }

    @Test
    public void testParseInvalidComplex() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction;
        ParsePosition pos = new ParsePosition(0);

        fraction = format.parse("1 / (a + b)", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / (a + b) a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / (a + b) a / c", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / (a + b) a / c a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("1 / (a + b) a / c a / d", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);
    }

    @Test
    public void testParseInvalidComplexWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction;
        ParsePosition pos = new ParsePosition(0);

        fraction = format.parse("2 / (a + b)", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b)", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b) a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b) a / c", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b) a / c a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b) a / c a / d", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);

        fraction = format.parse("2 a / (a + b) a / c a / d a", pos);
        assertEquals(null, fraction);
        pos.setIndex(pos.getIndex() + 1);
    }

    @Test
    public void testParseBigDecimalWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#,##0.##");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.##");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0.##");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 123.456 / 789.789", new ParsePosition(0));
        assertEquals(new Fraction(new BigDecimal("2123.456"), new BigDecimal("789.789")), fraction);
    }

    @Test
    public void testParseBigIntegerWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 123 / 789", new ParsePosition(0));
        assertEquals(new Fraction(new BigInteger("2123"), new BigInteger("789")), fraction);
    }

    @Test
    public void testParseBigDecimalNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.##");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0.##");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 1.234.567 / 789.789", new ParsePosition(0));
        assertEquals(new Fraction(new BigDecimal("1234.567"), new BigDecimal("789.789")), fraction);
    }

    @Test
    public void testParseBigIntegerNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 123 / 789", new ParsePosition(0));
        assertEquals(new Fraction(new BigInteger("123"), new BigInteger("789")), fraction);
    }

    @Test
    public void testParseBigDecimalDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0.##");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 / 1.234.567", new ParsePosition(0));
        assertEquals(new Fraction(new BigDecimal("2"), new BigDecimal("1234.567")), fraction);
    }

    @Test
    public void testParseBigIntegerDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Format wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        format.setWholeFormat(wholeFormat);
        format.setNumeratorFormat(numeratorFormat);
        format.setDenominatorFormat(denominatorFormat);

        Fraction fraction = format.parse("2 / 123", new ParsePosition(0));
        assertEquals(new Fraction(new BigInteger("2"), new BigInteger("123")), fraction);
    }
}