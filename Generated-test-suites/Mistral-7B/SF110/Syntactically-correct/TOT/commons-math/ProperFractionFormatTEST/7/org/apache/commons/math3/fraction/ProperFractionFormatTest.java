import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

 private final NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("0");
 private final NumberFormat numeratorFormat = NumberFormatUtils.createNumberFormat("#");
 private final NumberFormat denominatorFormat = NumberFormatUtils.createNumberFormat("#");

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(4, 3);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "1 1 / 3";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testFormatMixedNumber() {
 Fraction fraction = new Fraction(5, 2);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "2 1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testFormatZeroFraction() {
 Fraction fraction = new Fraction(0, 1);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testFormatProperFractionWithWholePart() {
 Fraction fraction = new Fraction(6, 3);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "2 0 / 3";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testFormatProperFractionWithNegativeNumerator() {
 Fraction fraction = new Fraction(-2, 3);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 String expected = "-1 1 / 3";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testParseProperFraction() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = format.parse("1 1 / 3", new ParsePosition(0));
 assertEquals(new Fraction(1, 3), fraction);
 }

 @Test
 public void testParseMixedNumber() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = format.parse("2 1 / 2", new ParsePosition(0));
 assertEquals(new Fraction(5, 2), fraction);
 }

 @Test
 public void testParseZeroFraction() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = format.parse("", new ParsePosition(0));
 assertNull(fraction);
 }

 @Test
 public void testParseProperFractionWithWholePart() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = format.parse("2 0 / 3", new ParsePosition(0));
 assertEquals(new Fraction(2, 3), fraction);
 }

 @Test
 public void testParseProperFractionWithNegativeNumerator() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = format.parse("-1 1 / 3", new ParsePosition(0));
 assertEquals(new Fraction(-1, 3), fraction);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFormatException() {
 ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat, denominatorFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNumeratorFormatException() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, null, denominatorFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testNullDenominatorFormatException() {
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, null);
 }
}