import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simplifier.StringSimplifier;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat integerFormat;
 private NumberFormat decimalFormat;

 @Test
 public void testDefaultConstructor() {
 properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("1 2/3", properFractionFormat, new Fraction(2, 3));
 }

 @Test
 public void testCustomWholeFormatConstructor() {
 integerFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
 decimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 decimalFormat.setMinimumFractionDigits(2);
 decimalFormat.setMaximumFractionDigits(2);
 properFractionFormat = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 testFormatAndParse("-1 2/3", properFractionFormat, new Fraction(-2, 3));
 }

 @Test
 public void testCustomNumberFormatConstructor() {
 integerFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
 decimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 decimalFormat.setMinimumFractionDigits(2);
 decimalFormat.setMaximumFractionDigits(2);
 ProperFractionFormat customFormat = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 testFormatAndParse("123 2/3", customFormat, new Fraction(123, 1));
 }

 @Test
 public void testFormatAndParseWithNegativeWhole() {
 integerFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
 decimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 decimalFormat.setMinimumFractionDigits(2);
 decimalFormat.setMaximumFractionDigits(2);
 properFractionFormat = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 testFormatAndParse("-1 2/3", properFractionFormat, new Fraction(-2, 3));
 }

 @Test
 public void testFormatAndParseWithZeroNumerator() {
 properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("0 1/2", properFractionFormat, new Fraction(0, 1));
 }

 @Test
 public void testFormatAndParseWithZeroDenominator() {
 properFractionFormat = new ProperFractionFormat();
 testFormatAndParse("1 0", properFractionFormat, new Fraction(1, 0));
 assertThrows(ArithmeticException.class, () -> properFractionFormat.parse("1 0", new ParsePosition(0)));
 }

 @Test
 public void testFormatAndParseWithNegativeNumeratorAndDenominator() {
 integerFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
 decimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
 decimalFormat.setMinimumFractionDigits(2);
 decimalFormat.setMaximumFractionDigits(2);
 ProperFractionFormat customFormat = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 testFormatAndParse("-1 -2/3", customFormat, new Fraction(-3, 2));
 }

 @Test
 public void testFormatAndParseWithFractionAsString() {
 ProperFractionFormat customFormat = new ProperFractionFormat();
 testFormatAndParse("1 1/2", customFormat, new Fraction(1, 2));
 }

 @Test
 public void testFormatAndParseWithBigDecimalAndBigInteger() {
 ProperFractionFormat customFormat = new ProperFractionFormat();
 Fraction fraction1 = new Fraction(BigInteger.valueOf(1), BigDecimal.valueOf(2));
 String formattedString = customFormat.format(fraction1, new StringBuffer(), null);
 Fraction parsedFraction = customFormat.parse(formattedString, new ParsePosition(0));
 assertEquals(fraction1, parsedFraction);
 }

 @Test
 public void testFormatAndParseWithVeryLargeFraction() {
 ProperFractionFormat customFormat = new ProperFractionFormat();
 BigInteger numerator = BigInteger.valueOf(Long.MAX_VALUE);
 BigInteger denominator = BigInteger.valueOf(Long.MAX_VALUE + 1);
 Fraction fraction1 = new Fraction(numerator, denominator);
 String formattedString = customFormat.format(fraction1, new StringBuffer(), null);
 Fraction parsedFraction = customFormat.parse(formattedString, new ParsePosition(0));
 assertEquals(fraction1, parsedFraction);
 }

 @Test
 public void testFormatAndParseWithVerySmallFraction() {
 ProperFractionFormat customFormat = new ProperFractionFormat();
 BigDecimal numerator = new BigDecimal("0.000000000000000001");
 BigInteger denominator = BigInteger.valueOf(Long.MAX_VALUE);
 Fraction fraction1 = new Fraction(numerator, denominator);
 String formattedString = customFormat.format(fraction1, new StringBuffer(), null);
 Fraction parsedFraction = customFormat.parse(formattedString, new ParsePosition(0));
 assertEquals(fraction1, parsedFraction);
 }

 private void testFormatAndParse(String input, ProperFractionFormat format, Fraction expectedFraction) {
 String output = format.format(expectedFraction, new StringBuffer(), null).toString();
 assertEquals(output, input);
 Fraction parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(expectedFraction, parsedFraction);
 }

 private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
 StringSimplifier simplifier = new StringSimplifier();
 int index = pos.getIndex();
 while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
 index++;
 pos.setIndex(index);
 }
 }

 private char parseNextCharacter(String source, ParsePosition pos) {
 char c;
 if (pos.getIndex() < source.length()) {
 c = source.charAt(pos.getIndex());
 pos.setIndex(pos.getIndex() + 1);
 } else {
 c = 0;
 }
 return c;
}
}