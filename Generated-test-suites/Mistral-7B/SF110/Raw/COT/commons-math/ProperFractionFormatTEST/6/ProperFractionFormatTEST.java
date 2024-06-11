import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testConstructors() {
        ProperFractionFormat defaultFormat = new ProperFractionFormat();
        Assert.assertNotNull(defaultFormat);

        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat();
        ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat);
        Assert.assertNotNull(customFormat);

        ProperFractionFormat threeArgFormat = new ProperFractionFormat(wholeFormat, NumberFormatUtils.createNumberFormat(), NumberFormatUtils.createNumberFormat());
        Assert.assertNotNull(threeArgFormat);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();

        Fraction fraction = new Fraction(1, 2);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String expected = "1/2";
        Assert.assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(2, 3);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "2/3";
        Assert.assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(3, 4);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "3/4";
        Assert.assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(-1, 2);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "-1/2";
        Assert.assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(1, -2);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        expected = "-1/2";
        Assert.assertEquals(expected, toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Assert.assertNotNull(format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse("1/2", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(1, 2), fraction);
        Assert.assertEquals(4, pos.getIndex());

        fraction = format.parse("2/3", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(2, 3), fraction);
        Assert.assertEquals(7, pos.getIndex());

        fraction = format.parse("3/4", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(3, 4), fraction);
        Assert.assertEquals(10, pos.getIndex());

        fraction = format.parse("-1/2", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(-1, 2), fraction);
        Assert.assertEquals(5, pos.getIndex());

        fraction = format.parse("1/-2", pos);
        Assert.assertNotNull(fraction);
        Assert.assertEquals(new Fraction(-1, 2), fraction);
        Assert.assertEquals(5, pos.getIndex());

        String input = "1/2 + 2/3 - 3/4";
        Fraction result = null;
        pos.setIndex(0);
        while (pos.getIndex() < input.length()) {
            fraction = format.parse(input, pos);
            if (fraction != null) {
                result = Fraction.add(result, fraction);
                if (pos.getIndex() < input.length() - 1) {
                    pos.setIndex(pos.getIndex() + 1);
                    char nextChar = input.charAt(pos.getIndex());
                    if (nextChar != ' ') {
                        pos.setIndex(pos.getIndex() - 1);
                        break;
                    }
                }
            }
        }
        Assert.assertEquals(new Fraction(5, 6), result);
    }
}