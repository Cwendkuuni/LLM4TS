import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat FORMAT = new ProperFractionFormat();

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(4, 5);
 String expected = "2 2/5";
 String actual = FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatProperFractionWithWholePart() {
 Fraction fraction = new Fraction(7, 3);
 String expected = "2 1 1/3";
 String actual = FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatProperFractionZero() {
 Fraction fraction = new Fraction(0, 1);
 String expected = "0";
 String actual = FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatProperFractionNegative() {
 Fraction fraction = new Fraction(-2, 3);
 String expected = "-1 1/3";
 String actual = FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatProperFractionRecurring() {
 Fraction fraction = new Fraction("1/3", "2");
 String expected = "0 1/2";
 String actual = FORMAT.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatProperFractionNullFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.format(null, toAppendTo, null);
 }

 @Test
 public void testParseProperFraction() {
 String source = "2 2/5";
 Fraction expected = new Fraction(4, 5);
 Fraction actual = FORMAT.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFractionWithWholePart() {
 String source = "2 1 1/3";
 Fraction expected = new Fraction(3, 3);
 Fraction actual = FORMAT.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFractionZero() {
 String source = "0";
 Fraction expected = new Fraction(0, 1);
 Fraction actual = FORMAT.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFractionNegative() {
 String source = "-1 1/3";
 Fraction expected = new Fraction(-1, 3);
 Fraction actual = FORMAT.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFractionRecurring() {
 String source = "0 1/2";
 Fraction expected = new Fraction("1/3", "2");
 Fraction actual = FORMAT.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testParseProperFractionInvalid() {
 String source = "2 2/0";
 ParsePosition pos = new ParsePosition(0);
 FORMAT.parse(source, pos);
 fail("Expected ParseException");
 }

 @Test(expected = NumberFormatException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 } catch (NullArgumentException e) {
 // Expected
 }
 }

}