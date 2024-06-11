import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.text.similarity.LevenshteinDistance;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ProperFractionFormatTest {

 private final ProperFractionFormat format = new ProperFractionFormat();

 @Test
 public void testFormatEmptyFraction() {
 String result = format.format(new Fraction(0, 1), new StringBuffer(), null);
 assertEquals("0 / 1", result.toString());
 }

 @Test
 public void testFormatZeroFraction() {
 String result = format.format(new Fraction(0, 0), new StringBuffer(), null);
 assertNull(result);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-3, 4);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("-3 / 4", result.toString());
 }

 @Test
 public void testFormatSimpleFraction() {
 Fraction fraction = new Fraction(1, 2);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("1 / 2", result.toString());
 }

 @Test
 public void testFormatComplexFraction() {
 Fraction fraction = new Fraction(11, 22);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("11 / 22", result.toString());
 }

 @Test
 public void testFormatComplexFractionWithWholePart() {
 Fraction fraction = new Fraction(11, 22);
 fraction = new Fraction(2, 1).add(fraction);
 String result = format.format(fraction, new StringBuffer(), null);
 assertEquals("2 + 11 / 22", result.toString());
 }

 @Test
 public void testParseSimpleFraction() {
 Fraction fraction = format.parse("1 / 2", new ParsePosition(0, ""));
 assertEquals(new Fraction(1, 2), fraction);
 }

 @Test
 public void testParseComplexFraction() {
 Fraction fraction = format.parse("11 / 22", new ParsePosition(0, ""));
 assertEquals(new Fraction(11, 22), fraction);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidFraction() {
 format.parse("1 a / 2", new ParsePosition(0, ""));
 }

 @Test
 public void testParseFractionWithWholePart() {
 Fraction fraction = format.parse("2 + 11 / 22", new ParsePosition(0, ""));
 assertEquals(new Fraction(2, 1).add(new Fraction(11, 22)), fraction);
 }

 @Test
 public void testLevenshteinDistance() {
 Fraction fraction = new Fraction(3, 4);
 String expected = format.format(fraction, new StringBuffer(), null).toString();
 String incorrect = "3/4";
 String incorrect2 = "3 / 4";
 int levenshteinDistance = new LevenshteinDistance().apply(expected, incorrect);
 int levenshteinDistance2 = new LevenshteinDistance().apply(expected, incorrect2);
 assertTrue(Precision.abs(levenshteinDistance, 1.0) <= 1.0);
 assertTrue(Precision.abs(levenshteinDistance2, 1.0) <= 1.0);
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat newFormat = new ProperFractionFormat();
 NumberFormat format = new DecimalFormat("#");
 newFormat.setWholeFormat(format);
 assertNotSame(format, newFormat.getWholeFormat());
 assertEquals(format, newFormat.getWholeFormat());
 }
}