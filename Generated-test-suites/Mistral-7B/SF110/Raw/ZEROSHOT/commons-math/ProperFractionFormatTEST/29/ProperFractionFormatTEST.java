import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;
import org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        pff.format(fraction, sb, fp);
        assertEquals("1 / 2", sb.toString());
    }

    @Test
    public void testConstructorWithFormat() {
        ProperFractionFormat pff = new ProperFractionFormat(new DecimalFormat("#"));
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        pff.format(fraction, sb, fp);
        assertEquals("3 / 4", sb.toString());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(1, 2);
        StringBuffer sb1 = new StringBuffer();
        FieldPosition fp1 = new FieldPosition(0, 0);
        pff.format(fraction1, sb1, fp1);
        assertEquals("1 / 2", sb1.toString());

        Fraction fraction2 = new Fraction(3, 4);
        StringBuffer sb2 = new StringBuffer();
        FieldPosition fp2 = new FieldPosition(0, 0);
        pff.format(fraction2, sb2, fp2);
        assertEquals("3 / 4", sb2.toString());

        Fraction fraction3 = new Fraction(-5, 8);
        StringBuffer sb3 = new StringBuffer();
        FieldPosition fp3 = new FieldPosition(0, 0);
        try {
            pff.format(fraction3, sb3, fp3);
            fail("Expected NumberFormatException for negative proper fraction");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();

        Fraction fraction1 = pff.parse("1", new ParsePosition(0));
        assertEquals(new Fraction(1, 1), fraction1);

        Fraction fraction2 = pff.parse("1/2", new ParsePosition(0));
        assertEquals(new Fraction(1, 2), fraction2);

        Fraction fraction3 = pff.parse("3/4", new ParsePosition(0));
        assertEquals(new Fraction(3, 4), fraction3);

        Fraction fraction4 = pff.parse("12/5", new ParsePosition(0));
        assertEquals(new Fraction(12, 5), fraction4);

        Fraction fraction5 = pff.parse("3/-2", new ParsePosition(0));
        assertEquals(new Fraction(-3, 2), fraction5);

        Fraction fraction6 = pff.parse("12/0", new ParsePosition(0));
        assertEquals(null, fraction6);

        Fraction fraction7 = pff.parse("12/abcd", new ParsePosition(0));
        assertEquals(null, fraction7);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("#");
        pff.setWholeFormat(wholeFormat);

        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0, 0);
        pff.format(fraction, sb, fp);
        assertEquals("3 / 4", sb.toString());

        NumberFormat newWholeFormat = new DecimalFormat("0");
        pff.setWholeFormat(newWholeFormat);
        StringBuffer sb2 = new StringBuffer();
        FieldPosition fp2 = new FieldPosition(0, 0);
        pff.format(fraction, sb2, fp2);
        assertEquals("0 / 3 / 4", sb2.toString());
    }

}