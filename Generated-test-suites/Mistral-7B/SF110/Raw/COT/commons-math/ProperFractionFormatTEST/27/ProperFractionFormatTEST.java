import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer(100);
        pff.format(fraction, result, new FieldPosition(0, 0));
        String expectedResult = NumberFormatUtils.format(1.0, "0.5", null) + " / 2";
        Assert.assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testConstructorWithFormat() {
        ProperFractionFormat pff = new ProperFractionFormat(NumberFormat.getNumberInstance());
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer(100);
        pff.format(fraction, result, new FieldPosition(0, 0));
        String expectedResult = "3/4";
        Assert.assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testConstructorWithThreeFormats() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(1, 3);
        StringBuffer result = new StringBuffer(100);
        pff.format(fraction, result, new FieldPosition(0, 0));
        String expectedResult = String.format("%s %s / %s", wholeFormat.format(1), "", denominatorFormat.format(3));
        Assert.assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 8);
        StringBuffer result = new StringBuffer(100);
        pff.format(fraction, result, new FieldPosition(0, 0));
        String expectedResult = NumberFormatUtils.format(5.0 / 8.0, "0.625", NumberFormat.Style.LONG) + " / 8";
        Assert.assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Assert.assertNotNull(pff.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat pff = new ProperFractionFormat();
        pff.setWholeFormat(null);
    }

    @Test
    public void testParseMethodWithValidFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(3, pos.getIndex());
    }

    @Test
    public void testParseMethodWithInvalidFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "ABC 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseMethodWithInvalidDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "1 / 0";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseMethodWithValidBigDecimalFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "1.25 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseMethodWithBigDecimalWholePart() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2.5 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertEquals(new Fraction(2, 1), fraction);
        Assert.assertEquals(6, pos.getIndex());
    }
}