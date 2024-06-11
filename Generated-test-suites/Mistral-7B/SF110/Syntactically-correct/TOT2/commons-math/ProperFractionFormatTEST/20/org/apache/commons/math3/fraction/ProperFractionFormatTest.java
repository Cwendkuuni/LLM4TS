import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String expected = "2/3";
        assertEquals(expected, toAppendTo.toString());
    }
}