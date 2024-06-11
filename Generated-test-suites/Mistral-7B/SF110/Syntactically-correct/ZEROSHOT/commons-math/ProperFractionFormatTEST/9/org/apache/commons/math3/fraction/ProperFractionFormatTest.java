import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    private NumberFormat wholeFormat;

    @Before
    public void setUp() {
        wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        properFractionFormat = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat.FieldPosition pos = new ProperFractionFormat.FieldPosition(0, 0);

        properFractionFormat.format(fraction, toAppendTo, pos);
        Assert.assertEquals("1/2", toAppendTo.toString());

        fraction = new Fraction(3, 4);
        toAppendTo.setLength(0);
        properFractionFormat.format(fraction, toAppendTo, pos);
        Assert.assertEquals("3/4", toAppendTo.toString());

        fraction = new Fraction(-1, 2);
        toAppendTo.setLength(0);
        properFractionFormat.format(fraction, toAppendTo, pos);
        Assert.assertEquals("-1/2", toAppendTo.toString());

        fraction = new Fraction(0, 3);
        toAppendTo.setLength(0);
        properFractionFormat.format(fraction, toAppendTo, pos);
        Assert.assertEquals("0", toAppendTo.toString());

        fraction = new Fraction(5, 6);
        toAppendTo.setLength(0);
        properFractionFormat.format(fraction, toAppendTo, pos);
        Assert.assertEquals("5/6", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        String source = "1 2/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertNull(parsedFraction);
        Assert.assertEquals(1, pos.getIndex());

        source = "1 2/3";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2), parsedFraction);
        Assert.assertEquals(4, pos.getIndex());

        source = "5 3/4";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(5, 3), parsedFraction);
        Assert.assertEquals(7, pos.getIndex());

        source = "5 3/4 -1/2";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(5, 3).add(new Fraction(-1, 2)), parsedFraction);
        Assert.assertEquals(12, pos.getIndex());

        source = "5 / 3";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(5, 3), parsedFraction);
        Assert.assertEquals(4, pos.getIndex());

        source = "5 / 3.5";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(5, 3), parsedFraction);
        Assert.assertEquals(6, pos.getIndex());

        source = "0.5";
        parsedFraction = properFractionFormat.parse(source, pos);
        Assert.assertEquals(new Fraction(1, 2), parsedFraction);
        Assert.assertEquals(3, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat defaultFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat newFormat = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));

        Assert.assertNotNull(defaultFormat.getWholeFormat());
        Assert.assertNotNull(newFormat.getWholeFormat());

        Assert.assertEquals(defaultFormat.getWholeFormat(), defaultFormat.getWholeFormat());
        Assert.assertNotSame(defaultFormat.getWholeFormat(), newFormat.getWholeFormat());

        newFormat.setWholeFormat(defaultFormat.getWholeFormat());
        Assert.assertEquals(defaultFormat.getWholeFormat(), newFormat.getWholeFormat());

        newFormat.setWholeFormat(wholeFormat);
        Assert.assertNotSame(defaultFormat.getWholeFormat(), newFormat.getWholeFormat());

        newFormat.setWholeFormat(null);
        try {
            properFractionFormat.setWholeFormat(null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }
}