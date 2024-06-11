import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 }

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertEquals(properFractionFormat, pff);
 }

 @Test
 public void testConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertEquals(wholeFormat, pff.getWholeFormat());
 assertEquals(numeratorFormat, pff.getNumeratorFormat());
 assertEquals(denominatorFormat, pff.getDenominatorFormat());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat pff = new ProperFractionFormat();
 try {
 pff.setWholeFormat(null);
 } catch (NullArgumentException ex) {
 assertEquals("WHOLE_FORMAT", ex.getKey());
 fail("Should not be able to set null format");
 }
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(2, 3);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals("1 2/3", toAppendTo.toString());

 fraction = new Fraction(-3, 2);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals("-1 3/2", toAppendTo.toString());

 fraction = new Fraction(0, 1);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals("0", toAppendTo.toString());

 fraction = new Fraction(4, 4);
 toAppendTo = new StringBuffer();
 pff.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals("1", toAppendTo.toString());
}

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidInput() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "abcd";
 try {
 pff.parse(source, pos);
 fail("Should not be able to parse invalid input");
 } catch (NumberFormatException ex) {
 // expected
 }
}

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidWhole() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "1abcd 2/3";
 try {
 pff.parse(source, pos);
 fail("Should not be able to parse invalid whole part");
 } catch (NumberFormatException ex) {
 // expected
 }
}

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "1 2abcd / 3";
 try {
 pff.parse(source, pos);
 fail("Should not be able to parse invalid numerator");
 } catch (NumberFormatException ex) {
 // expected
 }
}

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source = "1 2/3abcd";
 try {
 pff.parse(source, pos);
 fail("Should not be able to parse invalid denominator");
 } catch (NumberFormatException ex) {
 // expected
 }
}

 @Test
 public void testParse() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;

 source = "1 2/3";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(2, 3), result);
 assertEquals(6, pos.getIndex());
 source = "1 2/-3";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(2, 3), result.negate());
 assertEquals(6, pos.getIndex());
 source = "1 -2/3";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(-2, 3), result);
 assertEquals(6, pos.getIndex());
 source = "1 3/2";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result.negate());
 assertEquals(6, pos.getIndex());
 source = "1 3/-2";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(6, pos.getIndex());
 source = "1 0/1";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(0, 1), result);
 assertEquals(2, pos.getIndex());
 source = "1 1/1";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 1), result);
 assertEquals(3, pos.getIndex());
 source = "1 4/4";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 1), result);
 assertEquals(5, pos.getIndex());

 // Test with big numbers
 source = "1 1234567890123456789/1234567890123456789";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1234567890123456789L, 1234567890123456789L), result);
 assertEquals(34, pos.getIndex());
 }

 @Test
 public void testParseWithWhole() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;

 source = "1 2/3 4";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(4, 1), result);
 assertEquals(9, pos.getIndex());

 source = "4 2/3 1";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(9, pos.getIndex());

 source = "1 2/3 4 5";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(4, 1), result);
 assertEquals(12, pos.getIndex());
 }

 @Test
 public void testParseRounding() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 // Test with positive numbers
 source = "1 1.999999999/2.000000001";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(21, pos.getIndex());
 source = "1 2.999999999/3.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(22, pos.getIndex());
 source = "1 3.999999999/4.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(23, pos.getIndex());
 source = "1 4.999999999/5.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result);
 assertEquals(24, pos.getIndex());
 // Test with negative numbers
 source = "1 -1.999999999/2.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result.negate());
 assertEquals(21, pos.getIndex());
 source = "1 -2.999999999/3.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result.negate());
 assertEquals(22, pos.getIndex());
 source = "1 -3.999999999/4.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result.negate());
 assertEquals(23, pos.getIndex());
 source = "1 -4.999999999/5.000000001";
 result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 2), result.negate());
 assertEquals(24, pos.getIndex());
}

 @Test
 public void testParseWithBigWhole() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1234567890123456789 2/3";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(2, 3), result);
 assertEquals(16, pos.getIndex());
 }

 @Test
 public void testParseWithBigNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1 1234567890123456789/2";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1234567890123456789L, 2L), result);
 assertEquals(18, pos.getIndex());
 }

 @Test
 public void testParseWithBigDenominator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1 2/1234567890123456789";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1, 1234567890123456789L), result);
 assertEquals(11, pos.getIndex());
 }

 @Test
 public void testParseWithBigWholeAndNumerator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1234567890123456789 1234567890123456789/1234567890123456789";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1234567890123456789L, 1234567890123456789L), result);
 assertEquals(40, pos.getIndex());
 }

 @Test
 public void testParseWithBigWholeAndNumeratorAndDenominator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1234567890123456789 1234567890123456789/1234567890123456789/1234567890123456789";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(1234567890123456789L, MathUtils.multiplyExact(MathUtils.multiplyExact(1234567890123456789L, 1234567890123456789L))), result);
 assertEquals(56, pos.getIndex());
 }

 @Test
 public void testParseWithVeryBigValues() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1.0d-307 1.0d-308/1.0d-307";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(Math.pow(10, -308), Math.pow(10, -307)), result, 0.0);
 assertEquals(11, pos.getIndex());
 }

 @Test
 public void testParseWithVeryBigNegativeValues() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 String source;
 source = "1.0d-307 -1.0d-308/1.0d-307";
 Fraction result = pff.parse(source, pos);
 assertEquals(new Fraction(Math.pow(10, -308), Math.pow(10, -307)).negate(), result, 0.0);
 assertEquals(11, pos.getIndex());
 }
}