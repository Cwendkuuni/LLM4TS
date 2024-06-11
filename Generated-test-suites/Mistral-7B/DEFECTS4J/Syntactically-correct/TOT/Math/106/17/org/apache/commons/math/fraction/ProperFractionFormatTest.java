import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProperFractionFormatTest {

 private static final String DEFAULT_FORMAT = "%d %02d/%02d";
 private static final ProperFractionFormat DEFAULT_FORMATTER = new ProperFractionFormat(new DecimalFormat(DEFAULT_FORMAT));

 @Test
 public void testFormatDefault() {
 Fraction fraction = new Fraction(4, 7);
 String expected = "2/7";
 StringBuilder appendTo = new StringBuilder();
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCustom() {
 Fraction fraction = new Fraction(5, 8);
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("0#");
 NumberFormat denominatorFormat = new DecimalFormat("0#");
 ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "-5/8";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNegative() {
 Fraction fraction = new Fraction(1, 2);
 fraction.setNegative(true);
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("0#");
 NumberFormat denominatorFormat = new DecimalFormat("0#");
 ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "-1/2";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNumeratorNegative() {
 Fraction fraction = new Fraction(1, 2);
 fraction.setNumeratorNegative(true);
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("0#");
 NumberFormat denominatorFormat = new DecimalFormat("0#");
 ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "1/-2";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDenominatorNegative() {
 Fraction fraction = new Fraction(1, 2);
 fraction.setDenominatorNegative(true);
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("0#");
 NumberFormat denominatorFormat = new DecimalFormat("0#");
 ProperFractionFormat formatter = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "1/2";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertNotEquals(expected, actual);
 }

 @Test
 public void testFormatImproper() {
 Fraction fraction = new Fraction(8, 3);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 String expected = "2 2/3";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatImproperNegative() {
 Fraction fraction = new Fraction(-8, 3);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 String expected = "-2 2/3";
 StringBuilder appendTo = new StringBuilder();
 formatter.format(fraction, appendTo, null);
 String actual = appendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDefault() {
 Fraction expected = new Fraction(1, 3);
 String input = "1/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseCustom() {
 Fraction expected = new Fraction(5, 8);
 String input = "-5/8";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("0#"), new DecimalFormat("0#"));
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNegative() {
 Fraction expected = new Fraction(-1, 2);
 String input = "-1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseNumeratorNegative() {
 Fraction expected = new Fraction(1, -2);
 String input = "1/-2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDenominatorNegative() {
 Fraction expected = new Fraction(1, 2);
 String input = "1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("0#"), new DecimalFormat("0#"));
 formatter.setDenominatorFormat(new DecimalFormat("#"));
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseImproper() {
 Fraction expected = new Fraction(2, 3);
 String input = "2 2/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseImproperNegative() {
 Fraction expected = new Fraction(-2, 3);
 String input = "-2 2/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInvalidWhole() {
 String input = "1a/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 1);
 }

 @Test
 public void testParseInvalidNumerator() {
 String input = "1/3a";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 2);
 }

 @Test
 public void testParseInvalidDenominator() {
 String input = "1/3/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 3);
 }

 @Test
 public void testParseInvalidWholeNegative() {
 String input = "-1a/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 1);
 }

 @Test
 public void testParseInvalidNumeratorNegative() {
 String input = "1/-3a";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 2);
 }

 @Test
 public void testParseInvalidDenominatorNegative() {
 String input = "1/3/-3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 3);
 }

 @Test
 public void testParseInvalidWholeNegativeNumerator() {
 String input = "-1/3a";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 2);
 }

 @Test
 public void testParseInvalidWholeNumeratorNegative() {
 String input = "1/-3a";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 2);
 }

 @Test
 public void testParseInvalidWholeDenominatorNegative() {
 String input = "1/3/-3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 4);
 }

 @Test
 public void testParseInvalidWholeNegativeDenominator() {
 String input = "-1/3/-3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat formatter = DEFAULT_FORMATTER;
 Fraction actual = formatter.parse(input, pos);
 assertNull(actual);
 assertEquals(pos.getIndex(), 4);
 }
}