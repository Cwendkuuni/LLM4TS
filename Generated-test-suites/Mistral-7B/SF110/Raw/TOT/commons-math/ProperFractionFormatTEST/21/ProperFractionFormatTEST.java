import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.Precision;
import org.junit.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private Fraction fraction;

 @Before
 public void setUp() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 DecimalFormat numeratorFormat = new DecimalFormat("#");
 DecimalFormat denominatorFormat = new DecimalFormat("#");
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 String formatted = new StringBuffer().append(3).append(' ').append(1).append(" / ").append(2).toString();
 String expected = "3 1 / 2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(3, 2);
 String expected = "3 1 / 2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatZeroFraction() {
 Fraction fraction = new Fraction(0, 1);
 String expected = "0";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 String expected = "-1 1 / 2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testFormatInvalidFraction() {
 Fraction fraction = new Fraction(0, 0);
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 fail("Should throw MathIllegalArgumentException for invalid fraction");
 }

 @Test
 public void testParseValidFraction() {
 String source = "3 1 / 2";
 Fraction expected = new Fraction(3, 2);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "3";
 Fraction expected = new Fraction(3, 1);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "3 1 / 2";
 Fraction expected = new Fraction(3, 2);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseImproperFraction() {
 String source = "7 2 / 3";
 Fraction expected = new Fraction(7, 3);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumberWithLeadingZero() {
 String source = "03";
 Fraction expected = new Fraction(3, 1);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInvalidFractionWithLeadingZero() {
 String source = "03 1 / 2";
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertNull(actual);
 }

 @Test
 public void testParseInvalidFraction() {
 String source = "3 / 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 0);
 }

 @Test
 public void testParseInvalidFraction2() {
 String source = "3 a / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 0);
 }

 @Test
 public void testSetWholeFormat() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat newProperFractionFormat = new ProperFractionFormat();
 newProperFractionFormat.setWholeFormat(wholeFormat);
 assertEquals(wholeFormat, newProperFractionFormat.getWholeFormat());
 }
}