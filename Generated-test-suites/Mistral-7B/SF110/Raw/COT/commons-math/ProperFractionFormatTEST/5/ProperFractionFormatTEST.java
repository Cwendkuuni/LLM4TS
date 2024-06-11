import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Assert.assertNotNull(pff.getWholeFormat());
        Assert.assertNotNull(pff.getNumeratorFormat());
        Assert.assertNotNull(pff.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#");
        ProperFractionFormat pff = new ProperFractionFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, pff.getWholeFormat());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
        Assert.assertEquals("3 / 4", toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Assert.assertNotNull(pff.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat pff = new ProperFractionFormat();
        pff.setWholeFormat(null);
    }

    @Test
    public void testParseMethodWhenSuccess() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(2, 1, 3, 4), fraction);
    }

    @Test
    public void testParseMethodWhenWholePartIsNegative() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "-2 3/4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWhenDenominatorIsNegative() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2 3/-4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWhenInvalidCharacterFound() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2 3/4X";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(source.length(), pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWhenInvalidFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWhenInvalidInput() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String source = "2 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(source, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWithBigIntegerAndBigDecimal() {
        ProperFractionFormat pff = new ProperFractionFormat();
        BigInteger whole = BigInteger.valueOf(2);
        BigDecimal numerator = new BigDecimal("3.5");
        BigDecimal denominator = new BigDecimal("4.0");
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(whole.toString() + " " + numerator + "/" + denominator, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(whole.intValue(), numerator.toBigInteger(), denominator.toBigInteger()), fraction);
    }

    @Test
    public void testParseMethodWithBigDecimalAndBigInteger() {
        ProperFractionFormat pff = new ProperFractionFormat();
        BigDecimal whole = new BigDecimal("2.0");
        BigDecimal numerator = new BigDecimal("3.5");
        BigInteger denominator = BigInteger.valueOf(4);
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(whole.toString() + " " + numerator + "/" + denominator.toString(), pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(whole.intValue().intValue(), numerator.toBigInteger(), denominator), fraction);
    }

    @Test
    public void testParseMethodWithBigDecimalDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        BigDecimal whole = new BigDecimal("2.0");
        BigDecimal numerator = new BigDecimal("3.5");
        BigDecimal denominator = new BigDecimal("4.0");
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(whole.toString() + " " + numerator + "/" + denominator, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(whole.intValue().intValue(), numerator.toBigInteger(), denominator.toBigInteger()), fraction);
    }

    @Test
    public void testParseMethodWithBigIntegerNumerator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        BigInteger whole = BigInteger.valueOf(2);
        BigDecimal numerator = new BigDecimal("3.5");
        BigInteger denominator = BigInteger.valueOf(4);
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(whole.toString() + " " + numerator + "/" + denominator, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(whole.intValue(), numerator.toBigInteger(), denominator), fraction);
    }

    @Test
    public void testParseMethodWithBigIntegerNumeratorAndDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        BigInteger whole = BigInteger.valueOf(2);
        BigInteger numerator = BigInteger.valueOf(3);
        BigInteger denominator = BigInteger.valueOf(4);
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = pff.parse(whole.toString() + " " + numerator + "/" + denominator, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(whole.intValue(), numerator, denominator), fraction);
    }
}