import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedResult = "1/2";
        Assert.assertEquals(expectedResult, actual);
    }

    @Test
    public void testFormatMixedFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedResult = "2 1/3";
        Assert.assertEquals(expectedResult, actual);
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedResult = "4";
        Assert.assertEquals(expectedResult, actual);
    }

    @Test
    public void testFormatNegativeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedResult = "-3/2";
        Assert.assertEquals(expectedResult, actual);
    }

    @Test
    public void testParseProperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParseMixedFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "2 1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testParseWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(4, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testParseNegativeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "-3/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(-3, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "abcd/ef";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }
}