import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer(100);
        String expected = "1 / 2";
        format.format(fraction, result, new FieldPosition(0, 0));
        assertEquals(expected, result.toString());
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer(100);
        String expected = "-3 / 4"; // testing negative whole number
        format.setWholeFormat(NumberFormat.getNumberInstance().setMinimumFractionDigits(1).setNegativePrefix(""));
        format.format(fraction, result, new FieldPosition(0, 0));
        assertEquals(expected, result.toString());
    }

    // Add edge cases and exception handling test cases here
}