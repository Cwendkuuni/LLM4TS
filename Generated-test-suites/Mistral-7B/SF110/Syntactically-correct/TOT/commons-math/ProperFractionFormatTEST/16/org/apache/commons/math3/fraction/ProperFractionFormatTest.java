import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, Locale.ENGLISH));
        String expected = "1 3/4";
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        format.setWholeFormat(wholeFormat);
        int whole = 123;
        StringBuffer result = new StringBuffer();
        format.format(new Fraction(whole, 1), result, new FieldPosition(0, 0, Locale.ENGLISH));
        String expected = "123";
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, Locale.ENGLISH));
        String expected = "-3/4";
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 3, 4), fraction);
        BigDecimal expectedValue = new BigDecimal("1.75");
        BigDecimal actualValue = new BigDecimal(format.getFormatString().format(new Fraction(1, 3, 4)));
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testParseWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "123";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertEquals(new Fraction(123), fraction);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullSource() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse(null, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 3/0";
        ParsePosition pos = new ParsePosition(0);
        format.parse(source, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 -3/4";
        ParsePosition pos = new ParsePosition(0);
        format.parse(source, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}