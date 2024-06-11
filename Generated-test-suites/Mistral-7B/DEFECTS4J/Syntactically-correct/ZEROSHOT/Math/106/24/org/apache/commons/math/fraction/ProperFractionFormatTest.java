import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static final ProperFractionFormat DEFAULT_FORMAT = new ProperFractionFormat();
 private static final ProperFractionFormat CUSTOM_FORMAT = new ProperFractionFormat(new DecimalFormat("#,##0.##"));

 @Test
 public void testFormatAndParseDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String formatted = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 Fraction parsed = DEFAULT_FORMAT.parse(formatted, new ParsePosition(0));
 Assertions.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseCustomFormat() {
 Fraction fraction = new Fraction(BigDecimal.valueOf(1.5), BigInteger.valueOf(3));
 String formatted = CUSTOM_FORMAT.format(fraction, new StringBuffer(), null);
 Fraction parsed = CUSTOM_FORMAT.parse(formatted, new ParsePosition(0));
 Assertions.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseNegativeWholeNumber() {
 Fraction fraction = new Fraction(-3, 1);
 String formatted = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 Fraction parsed = DEFAULT_FORMAT.parse(formatted, new ParsePosition(0));
 Assertions.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseInvalidWholeNumber() {
 String invalidFormat = "2 -1/2";
 Assertions.assertThrows(NumberFormatException.class, () -> DEFAULT_FORMAT.format(new Fraction(-1, 2), new StringBuffer(), null), "Invalid whole number format: " + invalidFormat);
 Assertions.assertThrows(NumberFormatException.class, () -> DEFAULT_FORMAT.parse(invalidFormat, new ParsePosition(0)), "Invalid whole number format: " + invalidFormat);
 }

 @Test
 public void testFormatAndParseInvalidFraction() {
 String invalidFormat = "-3 -1/2";
 Assertions.assertThrows(NumberFormatException.class, () -> DEFAULT_FORMAT.format(new Fraction(-3, -1), new StringBuffer(), null), "Invalid fraction format: " + invalidFormat);
 Assertions.assertThrows(NumberFormatException.class, () -> DEFAULT_FORMAT.parse(invalidFormat, new ParsePosition(0)), "Invalid fraction format: " + invalidFormat);
 }

 @Test
 public void testFormatAndParseCustomFormatWithBigNumbers() {
 Fraction fraction = new Fraction(BigDecimal.TEN.pow(2), BigInteger.TEN.pow(3));
 String formatted = CUSTOM_FORMAT.format(fraction, new StringBuffer(), null);
 Fraction parsed = CUSTOM_FORMAT.parse(formatted, new ParsePosition(0));
 Assertions.assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseFormatChange() {
 Fraction fraction = new Fraction(1, 2);
 String formattedDefault = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 String formattedCustom = CUSTOM_FORMAT.format(fraction, new StringBuffer(), null);
 Fraction parsedDefault = DEFAULT_FORMAT.parse(formattedDefault, new ParsePosition(0));
 Fraction parsedCustom = CUSTOM_FORMAT.parse(formattedCustom, new ParsePosition(0));
 Assertions.assertEquals(fraction, parsedDefault);
 Assertions.assertEquals(fraction, parsedCustom);
 }

 @Test
 public void testFormatAndParseParsePosition() {
 Fraction fraction = new Fraction(1, 2);
 String formatted = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = DEFAULT_FORMAT.parse(formatted, pos);
 Assertions.assertEquals(fraction, parsed);
 Assertions.assertEquals(formatted.length(), pos.getIndex());
 }

 @Test
 public void testFormatAndParseLevenshteinDistance() {
 Fraction fraction = new Fraction(1, 2);
 String formattedDefault = DEFAULT_FORMAT.format(fraction, new StringBuffer(), null);
 String formattedCustom = CUSTOM_FORMAT.format(fraction, new StringBuffer(), null);
 int levenshteinDistanceDefault = new LevenshteinDistance().distance(formattedDefault, formattedDefault);
 int levenshteinDistanceCustom = new LevenshteinDistance().distance(formattedCustom, formattedCustom);
 int levenshteinDistanceDefaultCustom = new LevenshteinDistance().distance(formattedDefault, formattedCustom);
 System.out.println("Levenshtein Distance (Default to Default): " + levenshteinDistanceDefault);
 System.out.println("Levenshtein Distance (Custom to Custom): " + levenshteinDistanceCustom);
 System.out.println("Levenshtein Distance (Default to Custom): " + levenshteinDistanceDefaultCustom);
 Assertions.assertTrue(levenshteinDistanceDefault == 0 && levenshteinDistanceCustom == 0 && levenshteinDistanceDefaultCustom > 0);
 }

}