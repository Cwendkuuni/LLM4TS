import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String actual = format.format(fraction, new StringBuffer(), null);
        String expected = "1/2";
        assertEquals(expected, actual);
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getIntegerInstance();
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance();
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(-3, 2);
        String actual = format.format(fraction, new StringBuffer(), null);
        String expected = "-3 % / 2 %";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDenominator() {
        Fraction fraction = new Fraction(-3, -2);
        ProperFractionFormat format = new ProperFractionFormat();
        try {
            format.parse(fraction.toText(), new ParsePosition(0));
        } catch (ParseException e) {
            fail("ParseException expected but not thrown.");
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "1 a/b";
        format.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testInvalidInput2() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "1 -1/2";
        format.parse(input, pos);
    }

    @Test
    public void testFormatAndParseRoundTrip() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction inputFraction = new Fraction(1, 2);
        String inputString = format.format(inputFraction, new StringBuffer(), null);
        Fraction outputFraction = format.parse(inputString, new ParsePosition(0));
        assertEquals(inputFraction, outputFraction);
    }
}