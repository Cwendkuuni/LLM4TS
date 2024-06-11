import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    private static NumberFormat getDefaultNumberFormat() {
        return NumberFormat.getNumberInstance(DEFAULT_LOCALE);
    }

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        testFormatAndParse(format, 1, 2, "1/2");
        testFormatAndParse(format, -1, 2, "-1/2");
        testFormatAndParse(format, 1, 4, "1/4");
        testFormatAndParse(format, -1, 4, "-1/4");
        testFormatAndParse(format, 3, 2, "3/2");
        testFormatAndParse(format, -3, 2, "-3/2");
        testFormatAndParse(format, 1, 5, "1/5");
        testFormatAndParse(format, -1, 5, "-1/5");
        testFormatAndParse(format, 2, 3, "2/3");
        testFormatAndParse(format, -2, 3, "-2/3");
        testFormatAndParse(format, BigInteger.valueOf(13), BigInteger.valueOf(8), "13/8");
        testFormatAndParse(format, BigInteger.valueOf(-13), BigInteger.valueOf(8), "-13/8");
        testFormatAndParse(format, BigDecimal.valueOf(13.5), "13 1/2");
        testFormatAndParse(format, BigDecimal.valueOf(-13.5), "-13 1/2");
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.US);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        testFormatAndParse(format, 1, 2, "1/2");
        testFormatAndParse(format, -1, 2, "-1/2");
        testFormatAndParse(format, 1, 4, "1/4");
        testFormatAndParse(format, -1, 4, "-1/4");
        testFormatAndParse(format, 3, 2, "3/2");
        testFormatAndParse(format, -3, 2, "-3/2");
        testFormatAndParse(format, 1, 5, "1/5");
        testFormatAndParse(format, -1, 5, "-1/5");
        testFormatAndParse(format, 2, 3, "2/3");
        testFormatAndParse(format, -2, 3, "-2/3");
        testFormatAndParse(format, BigInteger.valueOf(13), BigInteger.valueOf(8), "13/8");
        testFormatAndParse(format, BigInteger.valueOf(-13), BigInteger.valueOf(8), "-13/8");
        testFormatAndParse(format, BigDecimal.valueOf(13.5), "13 1/2");
        testFormatAndParse(format, BigDecimal.valueOf(-13.5), "-13 1/2");
        testFormatAndParse(format, new Fraction(1, 2), "1 1/2");
        testFormatAndParse(format, new Fraction(-1, 2), "-1 1/2");
    }

    private void testFormatAndParse(ProperFractionFormat format, int whole, int numerator, String text) {
        testFormatAndParse(format, new Fraction(whole, 1), new Fraction(numerator, 1), text);
    }

    private void testFormatAndParse(ProperFractionFormat format, Fraction expected, String text) {
        testFormatAndParse(format, expected, text, null);
    }

    private void testFormatAndParse(ProperFractionFormat format, Fraction expected, String text, Fraction actual) {
        StringBuffer stringBuffer = new StringBuffer();
        FormatUtils.format(format, expected, stringBuffer, new FieldPosition(0, 0));
        String formatted = stringBuffer.toString();
        Assert.assertEquals("Format: ", formatted, text);

        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = format.parse(text, parsePosition);
        if (actual == null) {
            Assert.assertNull("Parse: ", result);
        } else {
            Assert.assertEquals("Parse: ", expected, result);
        }
    }
}