import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFormat;
 private ProperFractionFormat wholeFormatOnly;

 @Before
 public void setup() {
 properFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 wholeFormatOnly = new ProperFractionFormat(new DecimalFormat("#"));
 }

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 int wholeNumber = 5;
 String expected = "5";
 StringBuffer toAppendTo = new StringBuffer();
 properFormat.format(new Fraction(wholeNumber, 1), toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatProperFractionWithWholeNumber() {
 Fraction fraction = new Fraction(2, 3);
 int wholeNumber = 5;
 String expected = "5 " + fraction.toString();
 StringBuffer toAppendTo = new StringBuffer();
 properFormat.format(new Fraction(wholeNumber + fraction.getNumerator(), fraction.getDenominator()), toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = ArithmeticException.class)
 public void testFormatInvalidFraction() {
 Fraction fraction = new Fraction(1, 0);
 StringBuffer toAppendTo = new StringBuffer();
 properFormat.format(fraction, toAppendTo, null);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1 / 2";
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFormat.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "5";
 Fraction expected = new Fraction(5, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFormat.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumberWithFraction() {
 String source = "5 1 / 2";
 Fraction expected = new Fraction(5, 1, new Fraction(1, 2));
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFormat.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidInput() {
 String source = "abcd";
 ParsePosition pos = new ParsePosition(0);
 properFormat.parse(source, pos);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumerator() {
 String source = "1 a/2";
 ParsePosition pos = new ParsePosition(0);
 properFormat.parse(source, pos);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominator() {
 String source = "1 / b";
 ParsePosition pos = new ParsePosition(0);
 properFormat.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat testFormat = new ProperFractionFormat();
 try {
 testFormat.setWholeFormat(null);
 } catch (IllegalArgumentException ex) {
 }
 assertEquals("WHOLE_FORMAT", ex.getMessage());
 }

 @Test
 public void testGetWholeFormat() {
 NumberFormat expected = new DecimalFormat("#");
 NumberFormat actual = properFormat.getWholeFormat();
 assertEquals(expected, actual);
 }
}