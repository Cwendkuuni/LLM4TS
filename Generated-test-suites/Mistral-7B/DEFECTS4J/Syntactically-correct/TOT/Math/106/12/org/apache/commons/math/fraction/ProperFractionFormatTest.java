import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat();
 private static final ProperFractionFormat CUSTOM_FORMAT = new ProperFractionFormat(NumberFormat.getNumberInstance());

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 String formatted = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatted.toString());
 }

 @Test
 public void testCustomFormat() {
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 NumberFormat customNumeratorFormat = NumberFormat.getPercentInstance();
 NumberFormat customDenominatorFormat = NumberFormat.getPatternInstance();
 customDenominatorFormat.applyPattern("#");
 ProperFractionFormat customFormat = new ProperFractionFormat(customWholeFormat, customNumeratorFormat, customDenominatorFormat);
 Fraction fraction = new Fraction(1, 3);
 String expected = "1 % %%%";
 String formatted = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatted.toString());
 }

 @Test
 public void testFormatWholeNumber() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumIntegerDigits(2);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, getDefaultNumberFormat(), getDefaultNumberFormat());
 Fraction fraction = new Fraction(-5, 1);
 String expected = "-05 / 1";
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatted.toString());
 }

 @Test
 public void testFormatNegativeNumerator() {
 ProperFractionFormat format = DEFAULT_FORMAT;
 Fraction fraction = new Fraction(-3, 2);
 String expected = "-3 / 2";
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatted.toString());
 }

 @Test
 public void testFormatZero() {
 ProperFractionFormat format = DEFAULT_FORMAT;
 Fraction fraction = new Fraction(0, 1);
 String expected = "0 / 1";
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, formatted.toString());
 }

 @Test
 public void testFormatInvalidFraction() {
 ProperFractionFormat format = DEFAULT_FORMAT;
 String input = "-3 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertNull(fraction);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseWholeAndFraction() {
 ProperFractionFormat format = DEFAULT_FORMAT;
 String input = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(3, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseWithLeadingMinusSign() {
 ProperFractionFormat format = DEFAULT_FORMAT;
 String input = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(-7, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseWithCustomWholeFormat() {
 NumberFormat customWholeFormat = NumberFormat.getNumberInstance();
 customWholeFormat.setMinimumIntegerDigits(3);
 ProperFractionFormat format = new ProperFractionFormat(customWholeFormat, getDefaultNumberFormat(), getDefaultNumberFormat());
 String input = "123 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(123, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseWithCustomNumeratorFormat() {
 NumberFormat customNumeratorFormat = NumberFormat.getPatternInstance();
 customNumeratorFormat.applyPattern("0.##");
 ProperFractionFormat format = new ProperFractionFormat(getDefaultNumberFormat(), customNumeratorFormat, getDefaultNumberFormat());
 String input = "1.5 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 3), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseWithCustomDenominatorFormat() {
 NumberFormat customDenominatorFormat = NumberFormat.getPatternInstance();
 customDenominatorFormat.applyPattern("##%");
 ProperFractionFormat format = new ProperFractionFormat(getDefaultNumberFormat(), getDefaultNumberFormat(), customDenominatorFormat);
 String input = "1 2%";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }
}