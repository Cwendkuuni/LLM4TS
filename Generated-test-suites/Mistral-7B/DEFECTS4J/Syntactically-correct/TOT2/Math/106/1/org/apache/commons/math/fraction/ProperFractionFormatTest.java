import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer(256);
        Assert.assertEquals("1 1/2", format.format(fraction, result, null));
    }

    @Test
    public void testProperFractionFormatCustomConstructor() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = new Fraction(3, 4);
        StringBuffer result = new StringBuffer(256);
        Assert.assertEquals("3 1/4", format.format(fraction, result, null));
    }

    @Test
    public void testProperFractionFormatParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1 1/2", new ParsePosition(0));
        Assert.assertEquals(new Fraction(1, 2), fraction);

        Fraction fraction2 = format.parse("3 1/4", new ParsePosition(0));
        Assert.assertEquals(new Fraction(3, 4), fraction2);

        Fraction fraction3 = format.parse("1/2", new ParsePosition(0));
        Assert.assertEquals(new Fraction(1, 2), fraction3);

        Fraction fraction4 = format.parse("3/4", new ParsePosition(0));
        Assert.assertEquals(new Fraction(3, 4), fraction4);

        Fraction fraction5 = format.parse("-3 1/2", new ParsePosition(0));
        Assert.assertEquals(new Fraction(-7, 2), fraction5);

        ParsePosition pos = new ParsePosition(0);
        String source = "-3 -1/2";
        format.parse(source, pos);
        Assert.assertFalse("Invalid fraction format: " + source, fraction5 != null);
        Assert.assertEquals(-1, pos.getIndex());
    }
}