import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        String expected = "1/2";
        String result = format.format(fraction, toAppendTo, pos).toString();

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, -2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        String expected = "3";
        String result = format.format(fraction, toAppendTo, pos).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 2/3";
        ParsePosition pos = new ParsePosition(0);

        Fraction expected = new Fraction(1, 2);
        Fraction result = format.parse(source, pos);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1/0";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-1/2";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test
    public void testParseWithInvalidInputString() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "abc";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = format.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(1, pos.getErrorIndex());
    }
}