import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import org.junit.Assert;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Assert.assertNotNull(format.getWholeFormat());
        Assert.assertNotNull(format.getNumeratorFormat());
        Assert.assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithFormat() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormatUtils.getNumberInstance(Locale.US));
        Assert.assertEquals(NumberFormatUtils.getNumberInstance(Locale.US), format.getWholeFormat());
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat wholeFormat = NumberFormatUtils.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormatUtils.getPercentInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormatUtils.getPercentInstance(Locale.US);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Assert.assertEquals(wholeFormat, format.getWholeFormat());
        Assert.assertEquals(numeratorFormat, format.getNumeratorFormat());
        Assert.assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormatMethodWithWholeAndProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FormatUtil.setDefaultLocale(Locale.US);
        format.format(fraction, toAppendTo, null);
        String expectedResult = "1 3/4";
        Assert.assertEquals(expectedResult, toAppendTo.toString());
    }

    @Test
    public void testFormatMethodWithOnlyProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, null);
        String expectedResult = "3/4";
        Assert.assertEquals(expectedResult, toAppendTo.toString());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithNullSource() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse(null, pos);
    }

    @Test
    public void testParseMethodWithValidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(source.length(), pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 1/0";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseMethodWithWholeAndProperFractionInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(3, 2), fraction);
        Assert.assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParseMethodWithOnlyProperFractionInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(source.length(), pos.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}