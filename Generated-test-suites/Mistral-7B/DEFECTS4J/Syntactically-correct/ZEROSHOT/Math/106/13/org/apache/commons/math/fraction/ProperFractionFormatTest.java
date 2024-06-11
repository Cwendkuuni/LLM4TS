import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static final long serialVersionUID = -6337346779577272307L;
 private static final int MAX_LENGTH = 20;

 @Test
 public void testProperFractionFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();

 // Test default constructor
 Fraction expected = new Fraction(1, 2);
 String actual = pff.format(expected, new StringBuffer(), null);
 Assert.assertEquals("1 / 2", actual);

 // Test custom constructor with default number formats
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat pff1 = new ProperFractionFormat(wholeFormat);
 expected = new Fraction(3, 4);
 actual = pff1.format(expected, new StringBuffer(), null);
 Assert.assertEquals("3 / 4", actual);

 // Test custom constructor with custom number formats
 NumberFormat wholeFormatCustom = NumberFormat.getNumberInstance(Locale.US);
 wholeFormatCustom.setMinimumFractionDigits(1);
 NumberFormat numeratorFormatCustom = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormatCustom = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat pff2 = new ProperFractionFormat(wholeFormatCustom, numeratorFormatCustom, denominatorFormatCustom);
 expected = new Fraction(1, 3);
 actual = pff2.format(expected, new StringBuffer(), null);
 Assert.assertEquals("1/3", actual);

 // Test format method with negative whole number
 expected = new Fraction(-3, 2);
 actual = pff.format(expected, new StringBuffer(), null);
 Assert.assertEquals("-3 / 2", actual);

 // Test format method with multiple white spaces
 expected = new Fraction(1, 2);
 actual = pff.format(expected, new StringBuffer(), null);
 Assert.assertEquals("1 / 2", actual);
 actual = pff.format(expected, new StringBuffer(), null).toString();
 Assert.assertEquals("1 / 2", actual);
 actual = pff.format(expected, new StringBuffer(), null).toString().substring(0, 10);
 Assert.assertEquals("1   / 2", actual);

 // Test parse method with valid inputs
 String[] inputs = {"1/2", "1 1/2", "1/3", "-1/2", "1 2/3", "-1 2/3", "3/2", "-3/2"};
 for (String input : inputs) {
 Fraction parsed = pff.parse(input, new ParsePosition(0));
 Assert.assertNotNull(parsed);
 }

 // Test parse method with invalid inputs
 String[] invalidInputs = {"1/0", "1/0.5", "1-1/2", "1/0-1/2", "1/2/2", "1/-2", "1/2/0"};
 for (String invalidInput : invalidInputs) {
 ParsePosition pos = new ParsePosition(0);
 pff.parse(invalidInput, pos);
 Assert.assertNull(pos.getIndex() > 0 ? pff.getErrorPosition() : null);
 }

 // Test parse method with custom number formats
 NumberFormat wholeFormatParse = NumberFormat.getNumberInstance(Locale.US);
 wholeFormatParse.setMinimumIntegerDigits(1);
 wholeFormatParse.setMinimumFractionDigits(1);
 NumberFormat numeratorFormatParse = NumberFormat.getNumberInstance(Locale.US);
 numeratorFormatParse.setMinimumFractionDigits(1);
 NumberFormat denominatorFormatParse = NumberFormat.getNumberInstance(Locale.US);
 denominatorFormatParse.setMinimumFractionDigits(1);
 ProperFractionFormat pffParse = new ProperFractionFormat(wholeFormatParse, numeratorFormatParse, denominatorFormatParse);
 String[] inputsParse = {"1.23/2.45", "1 2.3/2 4.5", "1/3.45", "-1/2.3", "1 2/3.45", "-1 2/3.45", "3.5/2", "-3.5/2"};
 for (String inputParse : inputsParse) {
 Fraction parsedParse = pffParse.parse(inputParse, new ParsePosition(0));
 Assert.assertNotNull(parsedParse);
 }

 // Test parse method with different number formats
 ProperFractionFormat pff3 = new ProperFractionFormat(new DecimalFormat("#,##0.##"), new DecimalFormat("#,##0.##"), new DecimalFormat("#,##0"));
 String input3 = "1,234.567/2,456.789";
 Fraction parsed3 = pff3.parse(input3, new ParsePosition(0));
 Assert.assertEquals(new Fraction(1234567, 2456789), parsed3);

 // Test parse method with custom number format and invalid input
 ProperFractionFormat pff4 = new ProperFractionFormat(new DecimalFormat("#,##0.##"), new DecimalFormat("#,##0.##"), new DecimalFormat("#,##"));
 String input4 = "1,234.567/2,456.789.1";
 ParsePosition pos4 = new ParsePosition(0);
 pff4.parse(input4, pos4);
 Assert.assertEquals(pos4.getIndex(), 0);
 Assert.assertNull(pos4.getErrorPosition());

 // Test parse method with custom number format and invalid fraction
 String input5 = "1,234.567/2,456.789A";
 ParsePosition pos5 = new ParsePosition(0);
 pff4.parse(input5, pos5);
 Assert.assertEquals(pos5.getIndex(), 0);
 Assert.assertNotNull(pos5.getErrorPosition());

 // Test parse method with custom number format and negative fraction
 String input6 = "-1,234.567/2,456.789";
 Fraction parsed6 = pff4.parse(input6, new ParsePosition(0));
 Assert.assertEquals(new Fraction(-1234567, 2456789), parsed6);

 // Test parse method with custom number format and negative whole number
 String input7 = "-1,234.567/2,456.789";
 Fraction parsed7 = pff4.parse(input7, new ParsePosition(0));
 Assert.assertEquals(new Fraction(-1234567, 2456789), parsed7);

 // Test parse method with custom number format and fraction with leading zero
 String input8 = "0 1/2";
 Fraction parsed8 = pff4.parse(input8, new ParsePosition(0));
 Assert.assertEquals(new Fraction(0, 1), parsed8);

 // Test parse method with custom number format and fraction with trailing zero
 String input9 = "1/2 0";
 Fraction parsed9 = pff4.parse(input9, new ParsePosition(0));
 Assert.assertEquals(new Fraction(1, 2), parsed9);

 // Test parse method with large fraction
 String input10 = "12345678901234567890/12345678901234567890";
 Fraction parsed10 = pff4.parse(input10, new ParsePosition(0));
 Assert.assertEquals(new Fraction(12345678901234567890L), parsed10);

 // Test parse method with large fraction and custom parse position
 ParsePosition pos10 = new ParsePosition(0);
 Fraction parsed10_1 = pff4.parse(input10, pos10);
 Assert.assertEquals(new Fraction(12345678901234567890L), parsed10_1);
 Assert.assertEquals(pos10.getIndex(), input10.length());

 // Test parse method with large fraction and custom parse position and error position
 ParsePosition pos10_1 = new ParsePosition(0);
 Fraction parsed10_2 = pff4.parse(input10 + "X", pos10_1);
 Assert.assertNull(parsed10_2);
 Assert.assertEquals(pos10_1.getIndex(), input10.length());
 Assert.assertEquals(pos10_1.getErrorIndex(), input10.length() + 1);
 }

 @Test
 public void testLevenshteinDistance() {
 String[] inputs = {"1/2", "1 1/2", "1/3", "-1/2", "1 2/3", "-1 2/3", "3/2", "-3/2"};
 ProperFractionFormat pff = new ProperFractionFormat();
 for (int i = 0; i < inputs.length; i++) {
 for (int j = i + 1; j < inputs.length; j++) {
 int distance = new LevenshteinDistance().apply(inputs[i], inputs[j]);
 System.out.println("Levenshtein Distance between " +inputs[i] + " and " + inputs[j] + " is " + distance);
 Assert.assertTrue(distance <= MAX_LENGTH);
 }
 }
 }

 @Test
 public void testConvertToBigDecimal() {
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 4);
 BigDecimal expected = new BigDecimal("3.75");
 BigDecimal actual = new BigDecimal(pff.format(fraction, new StringBuffer(), null).toString());
 Assert.assertEquals(expected.round(RoundingMode.HALF_UP), actual.round(RoundingMode.HALF_UP));
 }
}