import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    ProperFractionFormat properFractionFormat = new ProperFractionFormat();

    @Test
    public void testFormatProperFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer stringBuffer = new StringBuffer(20);
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        ProperFractionFormat.format(fraction, stringBuffer, fieldPosition);
        String result = stringBuffer.toString();
        assertEquals("2/3", result);
    }

    @Test
    public void testFormatZeroFraction() {
        Fraction fraction = new Fraction(0, 1);
        StringBuffer stringBuffer = new StringBuffer(20);
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        ProperFractionFormat.format(fraction, stringBuffer, fieldPosition);
        String result = stringBuffer.toString();
        assertEquals("0", result);
    }

    @Test
    public void testFormatProperFractionWithWholePart() {
        Fraction fraction = new Fraction(4, 3);
        StringBuffer stringBuffer = new StringBuffer(20);
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        ProperFractionFormat.format(fraction, stringBuffer, fieldPosition);
        String result = stringBuffer.toString();
        assertEquals("1 1/3", result);
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test
    public void testParseProperFraction() {
        String source = "2/3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void testParseMixedNumber() {
        String source = "1 2/3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertEquals(new Fraction(1, 2), fraction.getNumerator());
        assertEquals(new Fraction(3, 1), fraction.getDenominator());
    }

    @Test
    public void testParseInvalidInput() {
        String source = "2/3a";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertNull(fraction);
        assertEquals(3, parsePosition.getIndex());
    }

    @Test
    public void testParseZeroFraction() {
        String source = "0";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertEquals(new Fraction(0, 1), fraction);
    }

    @Test
    public void testParseNegativeFraction() {
        String source = "-2/3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertEquals(new Fraction(-2, 3), fraction);
    }

    @Test
    public void testParseProperFractionWithWholePart() {
        String source = "1 2/3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, parsePosition);
        assertEquals(new Fraction(1, 2), fraction.getNumerator());
        assertEquals(new Fraction(3, 1), fraction.getDenominator());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = new DecimalFormat("#");
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }
}