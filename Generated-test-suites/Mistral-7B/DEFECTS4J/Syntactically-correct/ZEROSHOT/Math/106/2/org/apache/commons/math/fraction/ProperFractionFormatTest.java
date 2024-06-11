import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simplify.NumberFormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.text.NumberFormat;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 testFormat(format, 1, 2, "1 2/3");
 testParse(format, "1 2/3");
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat wholeFormat = NumberFormatFactory.getInstance("0.## %");
 NumberFormat numeratorFormat = NumberFormatFactory.getInstance("#");
 NumberFormat denominatorFormat = NumberFormatFactory.getInstance("#");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, -3, 2, "-3 2/3");
 testParse(format, "-3 2/3");
 }

 @Test
 public void testCustomWholeFormat() {
 NumberFormat wholeFormat = NumberFormatFactory.getInstance("#,##0");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, -123, 4, "-123 1/4");
 testParse(format, "-123 1/4");
 }

 @Test
 public void testCustomNumeratorFormat() {
 NumberFormat numeratorFormat = NumberFormatFactory.getInstance("#,##0.##");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, 123, 4, "123 1/4");
 testParse(format, "123 1/4");
 }

 @Test
 public void testCustomDenominatorFormat() {
 NumberFormat denominatorFormat = NumberFormatFactory.getInstance("#,##");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 testFormat(format, 1, 123, "1 123/1");
 testParse(format, "1 123/1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNumberFormat() {
 NumberFormat wholeFormat = null;
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(123, 456);
 format = new ProperFractionFormat();
 String expected = "123 456/1";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormatNegativeWhole() {
 Fraction fraction = new Fraction(-123, 456);
 format = new ProperFractionFormat();
 String expected = "-123 456/1";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testParse() {
 Fraction fraction = null;
 String input = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 fraction = format.parse(input, pos);
 assertEquals(new Fraction(1, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWhole() {
 Fraction fraction = null;
 String input = "1 2/3x";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 fraction = format.parse(input, pos);
 fail("Expected ParseException: Unparseable number: 'x'");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidNumerator() {
 Fraction fraction = null;
 String input = "1 2/3x";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 fraction = format.parse(input, pos);
 fail("Expected ParseException: Unparseable number: 'x'");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDenominator() {
 Fraction fraction = null;
 String input = "1 2/0";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 fraction = format.parse(input, pos);
 fail("Expected ParseException: Division by zero");
 }

 @Test
 public void testParseNegativeWhole() {
 Fraction fraction = null;
 String input = "-1 2/3";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 fraction = format.parse(input, pos);
 assertEquals(new Fraction(-1, 2), fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseNegativeWholeAndNumerator() {
 Fraction fraction = null;
 String input = "-1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 try {
 fraction = format.parse(input, pos);
 fail("Expected ParseException: Invalid input: ' '-1 -1/2'");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseBigDecimal() {
 Fraction fraction = null;
 String input = "123.456 123456/1000";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 BigDecimal whole = new BigDecimal("123.456");
 BigDecimal numerator = new BigDecimal("123456");
 BigDecimal denominator = new BigDecimal("1000");
 Fraction expected = new Fraction(whole.multiply(denominator).toBigIntegerExact(), denominator.toBigIntegerExact());
 fraction = format.parse(input, pos);
 assertEquals(expected, fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testParseBigInteger() {
 Fraction fraction = null;
 String input = "123 123456/1";
 ParsePosition pos = new ParsePosition(0);
 format = new ProperFractionFormat();
 BigInteger whole = new BigInteger("123");
 BigInteger numerator = new BigInteger("123456");
 BigInteger denominator = new BigInteger("1");
 Fraction expected = new Fraction(whole, denominator);
 fraction = format.parse(input, pos);
 assertEquals(expected, fraction);
 assertEquals(input.length(), pos.getIndex());
 }

 private void testFormat(ProperFractionFormat format, int whole, int numerator, String expected) {
 Fraction fraction = new Fraction(whole, numerator);
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 private void testParse(ProperFractionFormat format, String input) {
 Fraction fraction = null;
 ParsePosition pos = new ParsePosition(0);
 fraction = format.parse(input, pos);
 assertNotNull(fraction);
 }
}