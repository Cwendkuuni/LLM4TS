import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private static final NumberFormat DEFAULT_NUMBER_FORMAT = NumberFormat.getNumberInstance();
 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat();
 private static final ProperFractionFormat CUSTOM_FORMAT = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 String actual = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);

 fraction = new Fraction(-3, 2);
 expected = "-3 / 2";
 actual = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
 NumberFormat denominatorFormat = NumberFormat.getPercentInstance();
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 Fraction fraction = new Fraction(1, 2);
 String expected = "1 % % 2";
 String actual = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);

 fraction = new Fraction(-3, 2);
 expected = "-3 % % 2";
 actual = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidWholeAndNumeratorFormat() {
 String source = "1 a/b";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 assertNull(fraction);
 assertEquals(0, pos.getIndex());
 assertEquals(6, pos.getErrorIndex()); // 'a' character
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDenominatorFormat() {
 String source = "1 / a";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 assertNull(fraction);
 assertEquals(1, pos.getIndex()); // '1' character
 assertEquals(2, pos.getErrorIndex()); // '/' character
 }

 @Test
 public void testParseValid() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 assertEquals(new Fraction(3, 2), fraction);
 assertEquals(7, pos.getIndex()); // last character examined: '2'
 }

 @Test
 public void testParseNegative() {
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 assertEquals(new Fraction(-3, 2), fraction);
 assertEquals(7, pos.getIndex()); // last character examined: '2'
 }

 @Test
 public void testParseInvalid() {
 String source = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 assertNull(fraction);
 assertEquals(0, pos.getIndex());
 assertEquals(1, pos.getErrorIndex()); // '-' character
 }

 @Test
 public void testFormatParseRoundTrip() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 String expected = source;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatParseRoundTripNegative() {
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 String expected = source;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatParseRoundTripCustom() {
 String source = "3 % % 2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 String expected = source;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatParseRoundTripCustomNegative() {
 String source = "-3 % % 2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT, DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 String expected = source;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatParseRoundTripWithJaroWinklerDistance() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat(DEFAULT_NUMBER_FORMAT);
 Fraction fraction = format.parse(source, pos);
 String expected = source;
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 JaroWinklerDistance jwd = new JaroWinklerDistance();
 double similarity = jwd.distance(expected, actual);
 assertTrue(similarity > 0.95);
 }
}