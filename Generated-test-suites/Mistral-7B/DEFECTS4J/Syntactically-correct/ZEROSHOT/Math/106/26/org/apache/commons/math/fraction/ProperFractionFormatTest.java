import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        // Test formatting
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        String expected = "1/2";
        Assert.assertEquals(expected, toAppendTo.toString());

        // Test parsing
        ParsePosition posParsed = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1/2", posParsed);
        Assert.assertEquals(fraction, parsedFraction);
        Assert.assertEquals(0, posParsed.getIndex());

        // Test formatting and parsing with negative whole number
        fraction = new Fraction(-1, 2);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        expected = "-1/2";
        Assert.assertEquals(expected, toAppendTo.toString());
        posParsed = new ParsePosition(0);
        parsedFraction = format.parse(expected, posParsed);
        Assert.assertEquals(fraction, parsedFraction);
        Assert.assertEquals(expected.length(), posParsed.getIndex());

        // Test parsing with improper fraction
        String improperFraction = "5";
        posParsed = new ParsePosition(0);
        parsedFraction = format.parse(improperFraction, posParsed);
        Assert.assertNull(parsedFraction);
        Assert.assertEquals(improperFraction.length(), posParsed.getIndex());

        // Test parsing with invalid fraction "-3 -1/2"
        String invalidFraction = "-3 -1/2";
        posParsed = new ParsePosition(0);
        parsedFraction = format.parse(invalidFraction, posParsed);
        Assert.assertNull(parsedFraction);
        Assert.assertEquals(invalidFraction.length(), posParsed.getIndex());
        Assert.assertEquals(1, posParsed.getErrorIndex());
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test formatting
        fraction = new Fraction(BigInteger.valueOf(3), BigDecimal.valueOf(4));
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        expected = "3 %";
        Assert.assertEquals(expected, toAppendTo.toString());

        // Test parsing
        posParsed = new ParsePosition(0);
        parsedFraction = format.parse(expected, posParsed);
        Assert.assertEquals(fraction, parsedFraction);
        Assert.assertEquals(expected.length(), posParsed.getIndex());

        // Test formatting and parsing with negative whole number and custom format
        fraction = new Fraction(BigInteger.valueOf(-3), BigDecimal.valueOf(4));
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        expected = "-3 %";
        Assert.assertEquals(expected, toAppendTo.toString());
        posParsed = new ParsePosition(0);
        parsedFraction = format.parse(expected, posParsed);
        Assert.assertEquals(fraction, parsedFraction);
        Assert.assertEquals(expected.length(), posParsed.getIndex());
    }
}