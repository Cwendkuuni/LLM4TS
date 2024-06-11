import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroDistance;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @BeforeAll
 public void setup() {
 // Create a ProperFractionFormat with default number formats
 properFractionFormat = new ProperFractionFormat();

 // Create custom number formats for testing
 wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumFractionDigits(2);
 wholeFormat.setMaximumFractionDigits(2);

 numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 numeratorFormat.setMinimumFractionDigits(2);
 numeratorFormat.setMaximumFractionDigits(2);

 denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 denominatorFormat.setMinimumFractionDigits(2);
 denominatorFormat.setMaximumFractionDigits(2);
 }

 @Test
 public void testDefaultFormat() {
 // Test formatting a Fraction using the default formats
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 StringFormattingTestHelper.assertEquals(expected, properFractionFormat, fraction, null);
 }

 @Test
 public void testCustomFormat() {
 // Test formatting a Fraction using custom formats
 ProperFractionFormat customFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(BigDecimal.valueOf(3), BigDecimal.valueOf(4));
 String expected = "3.00 / 4.00";
 StringFormattingTestHelper.assertEquals(expected, customFormat, fraction, null);
 }

 @Test
 public void testParse() {
 // Test parsing a string into a Fraction using the default formats
 String input = "1 1/2";
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 StringFormattingTestHelper.assertEquals(expected, parsed, 0.0);
 }

 @Test
 public void testParseInvalidWhole() {
 String input = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseInvalidNumerator() {
 String input = "1 1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseInvalidDenominator() {
 String input = "1 1/-2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseInvalidWholeAndNumerator() {
 String input = "1 -1 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseInvalidDenominatorNegative() {
 String input = "1 1/-2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = properFractionFormat.parse(input, pos);
 assertNull(parsed);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testParseMixedNumber() {
 String input = "1 1/2 + 1 1/3";
 Fraction expected1 = new Fraction(1, 2);
 Fraction expected2 = new Fraction(1, 3);
 Fraction[] fractions = {expected1, expected2};
 Fraction[] parsedFractions = new Fraction[2];
 int index = 0;
 ParsePosition pos = new ParsePosition(0);
 while (index < 2 && properFractionFormat.parse(input, pos) != null) {
 parsedFractions[index++] = properFractionFormat.parse(input, pos);
 }
 assertArrayEquals(expected1, parsedFractions[0]);
 assertArrayEquals(expected2, parsedFractions[1]);
 }

 @Test
 public void testParseMixedNumberWithWhitespace() {
 String input = "1 1 / 2 + 1 1 / 3";
 Fraction expected1 = new Fraction(1, 2);
 Fraction expected2 = new Fraction(1, 3);
 Fraction[] fractions = {expected1, expected2};
 Fraction[] parsedFractions = new Fraction[2];
 int index = 0;
 ParsePosition pos = new ParsePosition(0);
 while (index < 2 && properFractionFormat.parse(input, pos) != null) {
 parsedFractions[index++] = properFractionFormat.parse(input, pos);
 }
 assertArrayEquals(expected1, parsedFractions[0]);
 assertArrayEquals(expected2, parsedFractions[1]);
 }

 @Test
 public void testParseMixedNumberWithLeadingWhitespace() {
 String input = " 1 1 / 2 + 1 1 / 3";
 Fraction expected1 = new Fraction(1, 2);
 Fraction expected2 = new Fraction(1, 3);
 Fraction[] fractions = {expected1, expected2};
 Fraction[] parsedFractions = new Fraction[2];
 int index = 0;
 ParsePosition pos = new ParsePosition(0);
 while (index < 2 && properFractionFormat.parse(input, pos) != null) {
 parsedFractions[index++] = properFractionFormat.parse(input, pos);
 }
 assertArrayEquals(expected1, parsedFractions[0]);
 assertArrayEquals(expected2, parsedFractions[1]);
 }

 @Test
 public void testParseMixedNumberWithTrailingWhitespace() {
 String input = "1 1 / 2 + 1 1 / 3 ";
 Fraction expected1 = new Fraction(1, 2);
 Fraction expected2 = new Fraction(1, 3);
 Fraction[] fractions = {expected1, expected2};
 Fraction[] parsedFractions = new Fraction[2];
 int index = 0;
 ParsePosition pos = new ParsePosition(0);
 while (index < 2 && properFractionFormat.parse(input, pos) != null) {
 parsedFractions[index++] = properFractionFormat.parse(input, pos);
 }
 assertArrayEquals(expected1, parsedFractions[0]);
 assertArrayEquals(expected2, parsedFractions[1]);
 }

 @Test
 public void testJaroDistance() {
 String input1 = "1 1/2";
 String input2 = "1 1/2";
 double expected = 1.0;
 double actual = new JaroDistance().similarity(input1, input2);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testJaroDistanceDifferentWhitespace() {
 String input1 = "1 1 / 2";
 String input2 = "1 1/2";
 double expected = 0.9414;
 double actual = new JaroDistance().similarity(input1, input2);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testJaroDistanceMixedNumber() {
 String input1 = "1 1 / 2 + 1 1 / 3";
 String input2 = "1 1/2 + 1 1/3";
 double expected = 0.9414;
 double actual = new JaroDistance().similarity(input1, input2);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testJaroDistanceMixedNumberWhitespace() {
 String input1 = " 1 1 / 2 + 1 1 / 3";
 String input2 = "1 1/2 + 1 1/3 ";
 double expected = 0.9414;
 double actual = new JaroDistance().similarity(input1, input2);
 assertEquals(expected, actual, 0.001);
 }
}

class StringFormattingTestHelper {

 public static void assertEquals(String expected, ProperFractionFormat format, Fraction fraction, FieldPosition pos) {
 String actual = format.format(fraction, new StringBuffer(), pos).toString();
 assertEquals(expected, actual);
 }
}