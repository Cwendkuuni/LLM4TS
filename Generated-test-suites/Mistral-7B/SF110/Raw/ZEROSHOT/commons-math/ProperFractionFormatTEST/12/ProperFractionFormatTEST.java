import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.ParseException;
import org.apache.commons.math3.exception.MaxAbsValueExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.math.BigDecimal;

public class ProperFractionFormatTest {

 private ProperFractionFormat pff;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Test
 public void testDefaultConstructor() {
 pff = new ProperFractionFormat();
 }

 @Test
 public void testFormatWithDefaults() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 pff.format(fraction, toAppendTo, pos);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithCustomFormats() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 DecimalFormat numeratorFormat = new DecimalFormat("#.##");
 DecimalFormat denominatorFormat = new DecimalFormat("#.##");
 pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(3, 7);
 String expected = "1 2 / 3 1 4 5 1 2 1 / 7";
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 pff.format(fraction, toAppendTo, pos);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetWholeFormat() {
 pff = new ProperFractionFormat();
 assertEquals("Default whole format", pff.getWholeFormat(), FractionFormat.getDefaultNumberFormat());
 pff = new ProperFractionFormat(new DecimalFormat("#"));
 assertEquals("Custom whole format", pff.getWholeFormat(), new DecimalFormat("#"));
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNull() {
 pff = new ProperFractionFormat();
 pff.setWholeFormat(null);
 }

 @Test
 public void testParseWithDefaults() {
 String source = "1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithCustomFormats() {
 String source = "1 2 / 3 1 4 5 1 2 1 / 7";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(15, 7);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidInput1() {
 String source = "a / b";
 ParsePosition pos = new ParsePosition(0);
 pff.parse(source, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidInput2() {
 String source = "1 / a";
 ParsePosition pos = new ParsePosition(0);
 pff.parse(source, pos);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testParseWithDenominatorTooLarge() {
 String source = "1 / 2147483649";
 ParsePosition pos = new ParsePosition(0);
 pff.parse(source, pos);
 }

 @Test(expected = MaxAbsValueExceededException.class)
 public void testParseWithNumeratorTooLarge() {
 String source = "2147483648 / 1";
 ParsePosition pos = new ParsePosition(0);
 pff.parse(source, pos);
 }

 @Test
 public void testParseWithNegativeNumerator() {
 String source = "-1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(-1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithNegativeDenominator() {
 String source = "1 / -2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithMixedNegativeSigns() {
 String source = "1 / -3 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithLeadingAndTrailingWhitespaces() {
 String source = " 1 / 2 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithLeadingZeros() {
 String source = "0 0 / 1 0 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(0, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithTrailingZeros() {
 String source = "1 / 2 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(1, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithDecimalPoint() {
 String source = "1.5 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 2);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithLeadingDecimalPoint() {
 String source = ".5 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(5, 4);
 Fraction actual = pff.parse(source, pos);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithMultipleFractions() {
 String source = "1 / 2, 3 / 4, 5 / 6";
 ParsePosition pos = new ParsePosition(0);
 Fraction[] expected = new Fraction[] { new Fraction(1, 2), new Fraction(3, 4), new Fraction(5, 6) };
 Fraction[] actual = pff.parse(source, pos, new Fraction[3]);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithBigFractions() {
 Fraction fraction1 = new Fraction(BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal.valueOf(2));
 Fraction fraction2 = new Fraction(BigDecimal.valueOf(Long.MAX_VALUE + 1), BigDecimal.valueOf(2));
 String expected1 = new BigDecimal(Long.MAX_VALUE).toPlainString() + " / 2";
 String expected2 = new BigDecimal(Long.MAX_VALUE + 1).toPlainString() + " / 2";
 StringBuffer toAppendTo1 = new StringBuffer();
 StringBuffer toAppendTo2 = new StringBuffer();
 FieldPosition pos1 = new FieldPosition(0, 0);
 FieldPosition pos2 = new FieldPosition(0, 0);
 pff.format(fraction1, toAppendTo1, pos1);
 pff.format(fraction2, toAppendTo2, pos2);
 String actual1 = toAppendTo1.toString();
 String actual2 = toAppendTo2.toString();
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 }
}