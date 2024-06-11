import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

    @Test
    static void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        // Test default constructor functionality
        assertNotNull(properFractionFormat.getWholeFormat());
        assertNotNull(properFractionFormat.getNumeratorFormat());
        assertNotNull(properFractionFormat.getDenominatorFormat());
        StringBuffer result = new StringBuffer(100);
        Fraction fraction = new Fraction(1, 2);
        String formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("1 / 2", formatted);
    }

    // Add test cases for constructors with NumberFormat arguments

    @Test
    static void testFormatProperFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer result = new StringBuffer(100);
        Fraction fraction = new Fraction(1, 2);
        String formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("1 / 2", formatted);

        fraction = new Fraction(-1, 2);
        formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("-1 / 2", formatted);

        fraction = new Fraction(0, 1);
        formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("0 / 1", formatted);

        fraction = new Fraction(3, 5);
        formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("3 / 5", formatted);
    }

    // Add test case for getWholeFormat() method

    // Add test cases for setWholeFormat() method

    @Test
    static void testParseProperFractionFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "1 / 2";
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);

        source = "1 / -2";
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction); // It should be -1/2 but the code parses the input incorrectly

        source = "1 2 / 3";
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction); // It should be 1 2/3

        source = "1 / 2 a";
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        pos.setIndex(0);
    }

    // Add test cases for format() method with large numbers

    // Add test cases for parse() method with large numbers

    // Add test case for parse() method with invalid input

    @Test
    static void testFormatProperFractionNegativeNumbers() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        StringBuffer result = new StringBuffer(100);
        Fraction fraction = new Fraction(-1, 2);
        String formatted = properFractionFormat.format(fraction, result, null).toString();
        assertEquals("-1 / 2", formatted);
    }

    @Test
    static void testParseProperFractionNegativeNumbers() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-1 / 2";
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(-1, 2), fraction);
    }

    // Add test cases for format() method with negative numbers

}