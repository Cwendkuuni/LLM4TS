import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat format = new ProperFractionFormat();
 assertNotNull(format);
 }

 @Test
 public void testConstructorWithDefaultNumberFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 NumberFormat wholeFormat = format.getWholeFormat();
 assertNotNull(wholeFormat);
 assertTrue(wholeFormat instanceof DecimalFormat);
 // default values for NumberFormat are not specified in Javadoc
 assertEquals("123", wholeFormat.format(123));
 }

 @Test
 public void testConstructorWithCustomNumberFormat() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
 Number num = new BigDecimal("123");
 Number den = new BigDecimal("45");
 Fraction fraction = new Fraction(num, den);
 StringBuffer formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 String expected = "1 123 / 45";
 assertEquals(expected, formattedString.toString());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullArgument() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(null);
 fail("Should not reach this point");
 }

 @Test
 public void testFormatMethod() {
 DecimalFormat wholeFormat = new DecimalFormat("#");
 DecimalFormat numeratorFormat = new DecimalFormat("#");
 DecimalFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Number num = new BigDecimal("123");
 Number den = new BigDecimal("45");
 Fraction fraction = new Fraction(num, den);
 StringBuffer formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 String expected = "1 123 / 45";
 assertEquals(expected, formattedString.toString());

 num = new BigDecimal("-123");
 den = new BigDecimal("45");
 fraction = new Fraction(num, den);
 formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 expected = "-1 -123 / 45";
 assertEquals(expected, formattedString.toString());

 num = new BigDecimal("0");
 den = new BigDecimal("45");
 fraction = new Fraction(num, den);
 formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 assertEquals("0 / 45", formattedString.toString());

 num = new BigDecimal("1");
 den = new BigDecimal("1");
 fraction = new Fraction(num, den);
 formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 assertEquals("1 / 1", formattedString.toString());

 num = new BigDecimal("12345678901234567890");
 den = new BigDecimal("98765432109876543210");
 fraction = new Fraction(num, den);
 formattedString = new StringBuffer(100);
 format.format(fraction, formattedString, new FieldPosition(0, 0));
 // length of formatted string is not specified in Junit test
 // it depends on the configured NumberFormat for whole, numerator and denominator
 // expected value is just for demonstration purposes
 expected = "12345678901234567890 12345678901234567890 / 98765432109876543210";
 // uncomment the following line if you want to check the length of the formatted string
 //assertEquals(expected, formattedString.toString().substring(0, 100));
 assertEquals(expected, formattedString.toString());
 }

 @Test
 public void testParseMethod() {
 ProperFractionFormat format = new ProperFractionFormat();
 String source = "1 123 / 45";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(source, pos);
 Number num = result.getNumerator();
 Number den = result.getDenominator();
 Number numExpected = new BigDecimal("123");
 Number denExpected = new BigDecimal("45");
 assertEquals(numExpected, num);
 assertEquals(denExpected, den);
 assertEquals(0, pos.getIndex());

 source = "1 -123 / 45";
 pos.setIndex(0);
 result = format.parse(source, pos);
 num = result.getNumerator();
 den = result.getDenominator();
 numExpected = new BigDecimal("-123");
 denExpected = new BigDecimal("45");
 assertEquals(numExpected, num);
 assertEquals(denExpected, den);
 assertEquals(7, pos.getIndex());

 source = "0 / 45";
 pos.setIndex(0);
 result = format.parse(source, pos);
 assertEquals(0, result.getNumerator());
 assertEquals(new BigDecimal("45"), result.getDenominator());
 assertEquals(3, pos.getIndex());

 source = "1 / 1";
 pos.setIndex(0);
 result = format.parse(source, pos);
 assertEquals(new BigDecimal("1"), result.getNumerator());
 assertEquals(new BigDecimal("1"), result.getDenominator());
 assertEquals(3, pos.getIndex());

 source = "12345678901234567890 12345678901234567890 / 98765432109876543210";
 pos.setIndex(0);
 result = format.parse(source, pos);
 Number numExpectedLong = new BigDecimal("12345678901234567890");
 Number denExpectedLong = new BigDecimal("98765432109876543210");
 assertEquals(numExpectedLong, result.getNumerator());
 assertEquals(denExpectedLong, result.getDenominator());
 assertEquals(-1, pos.getErrorIndex());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMethodWithInvalidInput() {
 ProperFractionFormat format = new ProperFractionFormat();
 String source = "1 123 / 45a";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(source, pos);
 fail("Should not reach this point");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMethodWithInvalidWholeNumber() {
 ProperFractionFormat format = new ProperFractionFormat();
 String source = "1 a / 45";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(source, pos);
 fail("Should not reach this point");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMethodWithInvalidNumerator() {
 ProperFractionFormat format = new ProperFractionFormat();
 String source = "1 0 / 45";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(source, pos);
 fail("Should not reach this point");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseMethodWithInvalidDenominator() {
 ProperFractionFormat format = new ProperFractionFormat();
 String source = "1 123 / -45";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = format.parse(source, pos);
 fail("Should not reach this point");
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullNumberFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(null);
 fail("Should not reach this point");
 }
}