import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1 2/3", new ParsePosition(0));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
    }

    @Test
    public void testCustomProperFractionFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("1 2/3", new ParsePosition(0));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
    }

    @Test
    public void testFormatFraction() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(1, 3);
        String result1 = format.format(fraction1, new StringBuffer(), null);
        Assert.assertEquals("1 1/3", result1.toString());

        Fraction fraction2 = new Fraction(1, 4);
        String result2 = format.format(fraction2, new StringBuffer(), null);
        Assert.assertEquals("1/4", result2.toString());

        Fraction fraction3 = new Fraction(5, 8);
        String result3 = format.format(fraction3, new StringBuffer(), null);
        Assert.assertEquals("3/4", result3.toString());

        Fraction fraction4 = new Fraction(4, 7);
        String result4 = format.format(fraction4, new StringBuffer(), null);
        Assert.assertEquals("1 1/7", result4.toString());
    }

    @Test
    public void testParseFractionWithWholePart() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = format.parse("2 1/2", new ParsePosition(0));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(3, 2), fraction);

        fraction = format.parse("1 3 1/2", new ParsePosition(0));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(5, 2), fraction);
    }

    @Test
    public void testParseFractionWithWholePartAndNegativeSign() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 -1/2", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(-1, 2), fraction);
    }

    @Test
    public void testParseFractionWithDenominatorPartOnly() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1/3", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(0, 3), fraction);

        pos.setIndex(1);
        fraction = format.parse("1/", pos);
        Assert.assertNull(fraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testParseWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null);
    }

    @Test
    public void testFormatWithCustomNumberFormats() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(1, 3);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("1 % 1/3", result);
    }

    @Test
    public void testParseWithCustomNumberFormats() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = format.parse("1 % 1/3", new ParsePosition(0));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 3), fraction);
    }

    @Test
    public void testFormatFractionWithBigDecimal() {
        ProperFractionFormat format = new ProperFractionFormat();

        BigDecimal number = new BigDecimal("2.375");
        Fraction fraction = new Fraction(number);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("2 3/4", result);
    }

    @Test
    public void testFormatFractionWithBigInteger() {
        ProperFractionFormat format = new ProperFractionFormat();

        BigInteger number = new BigInteger("12345678901234567890");
        Fraction fraction = new Fraction(number);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("1 12345678901234567890 / 1", result);
    }

    @Test
    public void testParseFractionWithBigDecimal() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        BigDecimal number = new BigDecimal("7.125");
        Fraction fraction = format.parse(number.toString(), pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(7, 4), fraction);
        Assert.assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseFractionWithBigInteger() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        BigInteger number = new BigInteger("12345678901234567890");
        Fraction fraction = format.parse(number.toString(), pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(12345678901234567890L), fraction);
        Assert.assertEquals(25, pos.getIndex());
    }

    @Test
    public void testFormatFractionWithZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(0, 0);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testParseFractionWithZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("0/0", pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testFormatFractionWithInfiniteDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, new BigInteger("12345678901234567890"));
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("0 / ∞", result);
    }

    @Test
    public void testParseFractionWithInfiniteDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("0 / ∞", pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testFormatFractionWithNaN() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(Double.NaN, 1);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testParseFractionWithNaN() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("NaN / 1", pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testFormatFractionWithInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, Double.POSITIVE_INFINITY);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("∞", result);
    }

    @Test
    public void testParseFractionWithInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("∞", pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testFormatFractionWithNegativeInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(1, Double.NEGATIVE_INFINITY);
        String result = format.format(fraction, new StringBuffer(), null).toString();
        Assert.assertEquals("-∞", result);
    }

    @Test
    public void testParseFractionWithNegativeInfinity() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("-∞", pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }
}