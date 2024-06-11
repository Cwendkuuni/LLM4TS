import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat test = new ProperFractionFormat();
 assertNotNull(test);
 }

 @Test
 public void testConstructorWithFormat() {
 NumberFormat format = new DecimalFormat("#0");
 ProperFractionFormat test = new ProperFractionFormat(format, new DecimalFormat("#"), new DecimalFormat("#"));
 assertNotNull(test);
 }

 @Test
 public void testConstructorWithThreeFormats() {
 NumberFormat wholeFormat = new DecimalFormat("#,##0");
 NumberFormat numeratorFormat = new DecimalFormat("#");
 NumberFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat test = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertNotNull(test);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 4);
 ProperFractionFormat test = new ProperFractionFormat();
 String expected = "3 ⁄ 4";
 StringBuffer toAppendTo = new StringBuffer();
 test.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithWholeNumber() {
 Fraction fraction = new Fraction(5, 1);
 ProperFractionFormat test = new ProperFractionFormat();
 String expected = "5";
 StringBuffer toAppendTo = new StringBuffer();
 test.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithNegativeNumerator() {
 Fraction fraction = new Fraction(-3, 4);
 ProperFractionFormat test = new ProperFractionFormat();
 String expected = "-3 ⁄ 4";
 StringBuffer toAppendTo = new StringBuffer();
 test.format(fraction, toAppendTo, null);
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidInput() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "abc";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = test.parse(source, pos);
 assertNull(result);
 }

 @Test
 public void testParseValidInputWithoutWholePart() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = test.parse(source, pos);
 assertEquals(new Fraction(3, 4), result);
 }

 @Test
 public void testParseValidInputWithWholePart() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "5/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = test.parse(source, pos);
 assertEquals(new Fraction(5, 4), result);
 }

 @Test
 public void testParseValidInputWithNegativeNumeratorAndDenominator() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "-3/-5";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = test.parse(source, pos);
 assertEquals(new Fraction(3, 5), result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumerator() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "3/b";
 ParsePosition pos = new ParsePosition(0);
 test.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominator() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "3/0";
 ParsePosition pos = new ParsePosition(0);
 test.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominatorWithNegativeNumerator() {
 ProperFractionFormat test = new ProperFractionFormat();
 String source = "-3/0";
 ParsePosition pos = new ParsePosition(0);
 test.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat format = new DecimalFormat("#,##0");
 ProperFractionFormat test = new ProperFractionFormat();
 test.setWholeFormat(format);
 assertEquals(format, test.getWholeFormat());
 }

}