import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.DivisionByZeroException;
import org.apache.commons.math3.exception.FormatFlagsConversionException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);
        properFractionFormat.format(fraction, output, pos);
        Assert.assertEquals("1/2", output.toString());
    }

    @Test
    public void testProperFractionFormatConstructorWithNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"));
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);
        properFractionFormat.format(fraction, output, pos);
        Assert.assertEquals("1", output.toString());
        Assert.assertEquals("2/1", properFractionFormat.format(new Fraction(2, 1), output, pos).toString());
    }

    @Test
    public void testProperFractionFormatConstructorWithThreeNumberFormats() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);
        properFractionFormat.format(fraction, output, pos);
        Assert.assertEquals("1", output.toString());
        Assert.assertEquals("2/1", properFractionFormat.format(new Fraction(2, 1), output, pos).toString());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }

    @Test
    public void testFormatMethodWithValidFractions() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(-2, 3);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        properFractionFormat.format(fraction1, output, pos);
        Assert.assertEquals("1/2", output.toString());

        properFractionFormat.format(fraction2, output, pos);
        Assert.assertEquals("3/4", output.toString());

        properFractionFormat.format(fraction3, output, pos);
        Assert.assertEquals("-2/3", output.toString());
    }

    @Test
    public void testFormatMethodWithZeroDenominator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 0);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        try {
            properFractionFormat.format(fraction, output, pos);
            Assert.fail("Expected DivisionByZeroException");
        } catch (DivisionByZeroException e) {
            // expected
        }
    }

    @Test
    public void testParseMethodWithValidFractions() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = properFractionFormat.parse("1/2", pos);
        Assert.assertEquals(new Fraction(1, 2), fraction1);

        Fraction fraction2 = properFractionFormat.parse("3/4", pos);
        Assert.assertEquals(new Fraction(3, 4), fraction2);

        Fraction fraction3 = properFractionFormat.parse("-2/3", pos);
        Assert.assertEquals(new Fraction(-2, 3), fraction3);

        Fraction fraction4 = properFractionFormat.parse("5/2", pos);
        Assert.assertEquals(new Fraction(5, 2), fraction4);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMethodWithInvalidFractions() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        properFractionFormat.parse("invalid_string", pos);
        Assert.fail("Expected NumberFormatException");
    }

    @Test
    public void testParseMethodWithWhitespace() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = properFractionFormat.parse(" 1 / 2", pos);
        Assert.assertEquals(new Fraction(1, 2), fraction1);

        Fraction fraction2 = properFractionFormat.parse("3 / 2 ", pos);
        Assert.assertEquals(new Fraction(3, 2), fraction2);

        Fraction fraction3 = properFractionFormat.parse(" -2 / 3 ", pos);
        Assert.assertEquals(new Fraction(-2, 3), fraction3);
    }

    @Test
    public void testParseMethodWithFractionWithLargeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse("1000000000000000001/2", pos);
        Assert.assertEquals(new Fraction(BigInteger.valueOf(1000000000000000001L), 2), fraction);
    }

    @Test
    public void testParseMethodWithNegativeWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse("-1/2", pos);
        Assert.assertEquals(new Fraction(-1, 2), fraction);
    }

    @Test
    public void testParseMethodWithFractionContainingZero() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = properFractionFormat.parse("0/2", pos);
        Assert.assertEquals(new Fraction(0, 1), fraction);

        fraction = properFractionFormat.parse("1/0", pos);
        Assert.assertEquals(null, fraction);
    }

    @Test
    public void testParseMethodWithFractionContainingDecimalPoint() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction1 = properFractionFormat.parse("1/2.5", pos);
        Assert.assertEquals(new Fraction(1, 2), fraction1);

        Fraction fraction2 = properFractionFormat.parse("3/4.25", pos);
        Assert.assertEquals(new Fraction(3, 4), fraction2);

        Fraction fraction3 = properFractionFormat.parse("-2/3.5", pos);
        Assert.assertEquals(new Fraction(-2, 3), fraction3);

        Fraction fraction4 = properFractionFormat.parse("1.5/2", pos);
        Assert.assertEquals(new Fraction(3, 2), fraction4);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMethodWithFractionContainingDecimalPointAndWhitespace() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        properFractionFormat.parse(" 1 / 2.5 ", pos);
        Assert.fail("Expected NumberFormatException");
    }

    @Test(expected = FormatFlagsConversionException.class)
    public void testFormatMethodWithLocale() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        try {
            properFractionFormat.format(fraction, output, pos, Locale.CHINA);
            Assert.fail("Expected FormatFlagsConversionException");
        } catch (FormatFlagsConversionException e) {
            // expected
        }
    }

    @Test
    public void testFormatMethodWithInvalidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        try {
            properFractionFormat.format(new Fraction("invalid_string", 2), output, pos);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            properFractionFormat.format(new Fraction(1, 0), output, pos);
            Assert.fail("Expected DivisionByZeroException");
        } catch (DivisionByZeroException e) {
            // expected
        }

        try {
            properFractionFormat.format(new Fraction(1, 2), output, pos);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            properFractionFormat.format(new Fraction(1, 2), null, pos);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            properFractionFormat.format(fraction, null, pos);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseMethodWithInvalidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        try {
            properFractionFormat.parse("invalid_string", pos);
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            properFractionFormat.parse("1/0", pos);
            Assert.fail("Expected ParsePosition.ParseFailedException");
        } catch (ParsePosition.ParseFailedException e) {
            // expected
        }

        try {
            properFractionFormat.parse("1/-2", pos);
            Assert.fail("Expected ParsePosition.ParseFailedException");
        } catch (ParsePosition.ParseFailedException e) {
            // expected
        }

        try {
            properFractionFormat.parse("1 / 2", pos);
            Assert.assertEquals(new Fraction(1, 2), properFractionFormat.parse("1 / 2", pos));
        } finally {
            Assert.assertEquals(4, pos.getEndIndex());
        }

        try {
            properFractionFormat.parse("1/2", null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            properFractionFormat.parse("1/2", pos);
            Assert.assertEquals(new Fraction(1, 2), properFractionFormat.parse("1/2", pos));
            Assert.assertEquals(7, pos.getIndex());
        } finally {
            Assert.assertEquals(10, pos.getEndIndex());
        }

        try {
            properFractionFormat.parse("1/2", pos);
            properFractionFormat.parse("3 / 4", pos);
            Assert.assertEquals(11, pos.getIndex());
        } finally {
            Assert.assertEquals(15, pos.getEndIndex());
        }

        try {
            properFractionFormat.parse("1/2", pos);
            properFractionFormat.parse("3 / 4", pos);
            Assert.assertEquals(11, pos.getIndex());
            properFractionFormat.parse("5 / 6", pos);
            Assert.assertEquals(17, pos.getIndex());
        } finally {
            Assert.assertEquals(23, pos.getEndIndex());
        }
    }

    @Test
    public void testSetWholeFormatWithCustomNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat customNumberFormat = new DecimalFormat("#,###");
        properFractionFormat.setWholeFormat(customNumberFormat);
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        properFractionFormat.format(fraction, output, pos);
        String formattedOutput = output.toString();
        Assert.assertTrue(formattedOutput.startsWith("1,"));
    }

    @Test
    public void testSetNumeratorFormatWithCustomNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat customNumberFormat = new DecimalFormat("#,###");
        properFractionFormat.setNumeratorFormat(customNumberFormat);
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        properFractionFormat.format(fraction, output, pos);
        String formattedOutput = output.toString();
        Assert.assertTrue(formattedOutput.split(" ")[1].startsWith("1,"));
    }

    @Test
    public void testSetDenominatorFormatWithCustomNumberFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat customNumberFormat = new DecimalFormat("#,###");
        properFractionFormat.setDenominatorFormat(customNumberFormat);
        Fraction fraction = new Fraction(1, 2);
        StringBuffer output = new StringBuffer();
        FormatPosition pos = new FormatPosition(0, 0);

        properFractionFormat.format(fraction, output, pos);
        String formattedOutput = output.toString();
        Assert.assertTrue(formattedOutput.split("/")[1].startsWith("2,"));
    }
}