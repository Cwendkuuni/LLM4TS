import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setup() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 }

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedNumber() {
 Fraction fraction = new Fraction(3, 2);
 String expected = "1 1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 Fraction fraction = new Fraction(4);
 String expected = "4";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 String expected = "-1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testFormatWithNullFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(null, toAppendTo, null);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), fraction);
 pos.next();
 }

 @Test
 public void testParseMixedNumber() {
 String source = "1 1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3, 2), fraction);
 pos.next();
 }

 @Test
 public void testParseWholeNumber() {
 String source = "4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(4), fraction);
 pos.next();
 }

 @Test
 public void testParseNegativeFraction() {
 String source = "-1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, 2), fraction);
 pos.next();
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullSource() {
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(null, pos);
 }

 @Test(expected = ParsePosition.ParseFailedException.class)
 public void testParseInvalidSource() {
 String source = "1 / 3 + 4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 properFractionFormat.setWholeFormat(null);
 }
}