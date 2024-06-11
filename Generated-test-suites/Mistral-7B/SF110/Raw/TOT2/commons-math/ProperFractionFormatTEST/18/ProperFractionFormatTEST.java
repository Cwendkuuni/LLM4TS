import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        String expected = "1 1/2";
        String actual = pff.format(fraction, toAppendTo, pos).toString();
        assertEquals(expected, actual);
        toAppendTo = new StringBuffer();
        fraction = new Fraction(1, 1);
        expected = "1";
        actual = pff.format(fraction, toAppendTo, pos).toString();
        assertEquals(expected, actual);
    }
}