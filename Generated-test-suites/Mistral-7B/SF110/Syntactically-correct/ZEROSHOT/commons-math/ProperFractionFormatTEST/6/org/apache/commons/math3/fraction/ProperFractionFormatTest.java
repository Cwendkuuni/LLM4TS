import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

 @Test
 public void testProperFractionFormatEmpty() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String result = pff.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0));
 assertEquals("", result.toString());
 }

 @Test
 public void testProperFractionFormatZero() {
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(0, 1);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 String result = pff.format(fraction, toAppendTo, pos).toString();
 assertEquals("0", result);
 }

 @Test
 public void testProperFractionFormatOne() {
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 1);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 String result = pff.format(fraction, toAppendTo, pos).toString();
 assertEquals("1", result);
 }

 @Test
 public void testProperFractionFormatWhole() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ProperFractionFormat wholeFormat = new DecimalFormat("#");
 pff.setWholeFormat(wholeFormat);
 Fraction fraction = new Fraction(2, 1);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 String result = pff.format(fraction, toAppendTo, pos).toString();
 assertEquals("2", result);
 }

 @Test
 public void testProperFractionFormatSimple() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ProperFractionFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat numeratorFormat = new DecimalFormat("#");
 ProperFractionFormat denominatorFormat = new DecimalFormat("#");
 pff.setWholeFormat(wholeFormat);
 pff.setNumeratorFormat(numeratorFormat);
 pff.setDenominatorFormat(denominatorFormat);
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 String result = pff.format(fraction, toAppendTo, pos).toString();
 assertEquals("3/4", result);
 }

 @Test
 public void testProperFractionFormatNegativeNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ProperFractionFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat numeratorFormat = new DecimalFormat("#");
 ProperFractionFormat denominatorFormat = new DecimalFormat("#");
 pff.setWholeFormat(wholeFormat);
 pff.setNumeratorFormat(numeratorFormat);
 pff.setDenominatorFormat(denominatorFormat);
 Fraction fraction = new Fraction(-3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 String result = pff.format(fraction, toAppendTo, pos).toString();
 assertEquals("-3/4", result);
 }

 @Test
 public void testProperFractionFormatParseSimple() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(3, 4), result);
 }

 @Test
 public void testProperFractionFormatParseWhole() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "2";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(2, 1), result);
 }

 @Test
 public void testProperFractionFormatParseSingleNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "1";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 1), result);
 }

 @Test
 public void testProperFractionFormatParseMultiDigitNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "17";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(17, 1), result);
 }

 @Test
 public void testProperFractionFormatParseWholeWithSlash() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "2/";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertNull(result);
 }

 @Test
 public void testProperFractionFormatParseNumeratorWithSlash() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "3/";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertNull(result);
 }

 @Test(expected = NumberFormatException.class)
 public void testProperFractionFormatParseNegativeNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "-3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction result = pff.parse(source, pos);
 assertNull(result);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullFormat() {
 ProperFractionFormat pff = new ProperFractionFormat(null, null, null);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullWholeFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.setWholeFormat(null);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullNumeratorFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.setNumeratorFormat(null);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullDenominatorFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.setDenominatorFormat(null);
 }
}