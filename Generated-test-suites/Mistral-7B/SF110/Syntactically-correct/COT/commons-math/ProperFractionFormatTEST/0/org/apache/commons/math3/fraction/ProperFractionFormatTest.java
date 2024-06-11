import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Assert.assertNotNull(properFractionFormat.getWholeFormat());
        Assert.assertNotNull(properFractionFormat.getNumeratorFormat());
        Assert.assertNotNull(properFractionFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
        Assert.assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testFormatMethodWithValidFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String expected = "1/2";
        String actual = toAppendTo.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatMethodWithWholePart() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String expected = "1 1/2";
        String actual = toAppendTo.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }

    @Test
    public void testParseMethodWithValidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseMethodWithNoWholePart() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseMethodWithWholePart() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithNullInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = null;
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithNullPosition() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1 1/2";
        ParsePosition pos = null;
        Fraction fraction = properFractionFormat.parse(input, pos);
    }

    @Test
    public void testParseMethodWithInvalidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1 1/0";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseMethodWithInvalidInput2() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "1 1/2 a";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(input, pos);
        Assert.assertNull(fraction);
        Assert.assertEquals(-1, pos.getErrorIndex());
    }
}