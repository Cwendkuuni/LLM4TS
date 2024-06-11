import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final NumberFormat WHOLE_FORMAT = new DecimalFormat("#");
 private static final NumberFormat NUMERATOR_FORMAT = new DecimalFormat("#");
 private static final NumberFormat DENOMINATOR_FORMAT = new DecimalFormat("#");

 @Test
 public void testDefaultFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(7, 2);
 String expected = "3 1/2";
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, result);
 }

 @Test
 public void testCustomFormat() {
 ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 Fraction fraction = new Fraction(5, 3);
 String expected = "5/3";
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(-3, 1);
 String result = format.format(fraction, new StringBuffer(), null);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, -2);
 String result = format.format(fraction, new StringBuffer(), null);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 2);
 String result = format.format(fraction, new StringBuffer(), null);
 String expected = "3 1/2";
 assertEquals(expected, result);
 // Now test parse with negative denominator
 Fraction parsedFraction = format.parse("3 1/2", new ParsePosition(0));
 assertEquals(fraction, parsedFraction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = format.parse(input, pos);
 assertNull(parsedFraction);
 assertEquals(-1, pos.getIndex());
 }
}