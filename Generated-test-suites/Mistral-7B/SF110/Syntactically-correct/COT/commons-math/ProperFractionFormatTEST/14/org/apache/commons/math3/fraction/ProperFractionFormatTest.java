package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFormat;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() {
        this.properFormat = new ProperFractionFormat();
    }

    @Test
    public void testEmptyString() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction();
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        assertEquals("", result.toString());
    }

    @Test
    public void testFormatZeroFraction() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction(0, 1);
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        assertEquals("0", result.toString());
    }

    @Test
    public void testFormatSimpleFraction() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction(1, 2);
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        assertEquals("1/2", result.toString());
    }

    @Test
    public void testFormatFractionWithWholePart() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction(4, 2);
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        assertEquals("2 2/2", result.toString());
    }

    @Test
    public void testFormatLargeFraction() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction(new BigInteger("1234567890"), new BigInteger("987654321"));
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        String expected = NumberFormatUtils.formatNumber(fraction.getNumerator(), properFormat.getNumeratorFormat()) + " / " +
                NumberFormatUtils.formatNumber(fraction.getDenominator(), properFormat.getDenominatorFormat());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatNegativeFraction() {
        StringBuffer result = new StringBuffer();
        Fraction fraction = new Fraction(-1, 2);
        properFormat.format(fraction, result, new FieldPosition(0, 0));
        assertEquals("-1/2", result.toString());
    }

    @Test
    public void testParseSimpleFraction() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFormat.parse("1/2", pos);
        assertEquals(new Fraction(1, 2), fraction);
        assertEquals(4, pos.getIndex());
    }

    @Test
    public void testParseFractionWithWholePart() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFormat.parse("2 2/2", pos);
        assertEquals(new Fraction(4, 2), fraction);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseFractionWithLargeNumerator() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFormat.parse("1234567890 / 987654321", pos);
        assertEquals(new Fraction(new BigInteger("1234567890"), new BigInteger("987654321")), fraction);
        assertEquals(21, pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        properFormat.setWholeFormat(null);
    }
}