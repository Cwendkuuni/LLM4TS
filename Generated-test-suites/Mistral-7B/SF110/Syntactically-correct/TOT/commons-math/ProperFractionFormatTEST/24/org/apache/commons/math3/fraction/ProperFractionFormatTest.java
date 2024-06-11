import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat getDefaultNumberFormat() {
 return NumberFormatUtils.createNumberFormat();
 }

 @Test
 public void testFormatProperFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 StringBuffer result = new StringBuffer(100);
 format.format(fraction, result, null);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatMixedFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 4);
 String expected = "3/4";
 StringBuffer result = new StringBuffer(100);
 format.format(fraction, result, null);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(5);
 String expected = "5";
 StringBuffer result = new StringBuffer(100);
 format.format(fraction, result, null);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatLargeFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(123456, 78901);
 String expected = "1666666666666666666/4636850350000000001";
 StringBuffer result = new StringBuffer(300);
 format.format(fraction, result, null);
 assertEquals(expected, result.toString());
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testFormatNegativeFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(-1, 2);
 StringBuffer result = new StringBuffer(100);
 format.format(fraction, result, null);
 fail("Expected MathIllegalArgumentException when formatting negative fraction");
 }

 @Test
 public void testParseProperFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction expected = new Fraction(1, 2);
 ParsePosition pos = new ParsePosition(0);
 String source = "1/2";
 Fraction result = format.parse(source, pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseMixedFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction expected = new Fraction(3, 4);
 ParsePosition pos = new ParsePosition(0);
 String source = "3/4";
 Fraction result = format.parse(source, pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction expected = new Fraction(5);
 ParsePosition pos = new ParsePosition(0);
 String source = "5";
 Fraction result = format.parse(source, pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLargeFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction expected = new Fraction(123456, 78901);
 ParsePosition pos = new ParsePosition(0);
 String source = "1666666666666666666/4636850350000000001";
 Fraction result = format.parse(source, pos);
 assertEquals(expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidFraction() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction result = format.parse("3/0", null);
 fail("Expected NumberFormatException when parsing invalid fraction");
 }

 @Test
 public void testParseInvalidWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "3a";
 Fraction result = format.parse(source, pos);
 assertEquals(null, result);
 pos.setIndex(source.length());
 assertEquals(1, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "1/-2";
 Fraction result = format.parse(source, pos);
 assertEquals(null, result);
 pos.setIndex(source.length());
 assertEquals(2, pos.getErrorIndex());
 }

 @Test
 public void testParseInvalidDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "1/0";
 Fraction result = format.parse(source, pos);
 assertEquals(null, result);
 pos.setIndex(source.length());
 assertEquals(1, pos.getErrorIndex());
 }

 @Test
 public void testParseFractionWithWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction expected = new Fraction(3, 2);
 ParsePosition pos = new ParsePosition(0);
 String source = "3 1/2";
 Fraction result = format.parse(source, pos);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatAndParseRoundtrip() {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 3);
 String expected = "1/3";
 StringBuffer resultFormat = new StringBuffer(100);
 format.format(fraction, resultFormat, null);
 Fraction resultParse = format.parse(resultFormat.toString(), null);
 assertEquals(fraction, resultParse);
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat();
 format.setWholeFormat(wholeFormat);
 String expected = "100";
 Number number = new BigDecimal("100");
 Number result = format.getWholeFormat().parse(expected, null);
 assertEquals(number, result);
 }
}