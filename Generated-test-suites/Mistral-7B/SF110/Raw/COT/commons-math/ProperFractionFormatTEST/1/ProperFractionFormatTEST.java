import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = properFractionFormat.parse("3 5/7", new ParsePosition(0, null));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(3, fraction.getNumerator().intValue());
        Assert.assertEquals(5, fraction.getDenominator().intValue());
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
        Fraction fraction = properFractionFormat.parse("3 5/7", new ParsePosition(0, null));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(3, fraction.getNumerator().intValue());
        Assert.assertEquals(5, fraction.getDenominator().intValue());
    }

    @Test
    public void testConstructorWithWholeFormatNumeratorFormatDenominatorFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = properFractionFormat.parse("3 5/7", new ParsePosition(0, null));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(3, fraction.getNumerator().intValue());
        Assert.assertEquals(5, fraction.getDenominator().intValue());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 5);
        StringBuffer stringBuffer = new StringBuffer();
        properFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0, 0, 0));
        Assert.assertEquals("3 5/7", stringBuffer.toString());
    }

    @Test
    public void testParseWithWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = formatter.parse("3", new ParsePosition(0, null));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(3, fraction.getNumerator().intValue());
        Assert.assertEquals(1, fraction.getDenominator().intValue());
    }

    @Test
    public void testParseWithWholeNumberAndFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        Fraction fraction = formatter.parse("3 1/2", new ParsePosition(0, null));
        Assert.assertNotNull(fraction);
        Assert.assertEquals(3, fraction.getNumerator().intValue());
        Assert.assertEquals(2, fraction.getDenominator().intValue());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseWithNullWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ProperFractionFormat formatter = new ProperFractionFormat(null);
        Fraction fraction = formatter.parse("3 5/7", new ParsePosition(0, null));
    }

}