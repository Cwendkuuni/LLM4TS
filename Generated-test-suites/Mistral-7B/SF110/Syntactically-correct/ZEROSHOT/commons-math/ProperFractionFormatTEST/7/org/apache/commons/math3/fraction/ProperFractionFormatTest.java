import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat numberFormat;
 private ProperFractionFormat properFractionFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 numberFormat = new DecimalFormat();
 properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 }

 @Test
 public void testFormatZero() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(0, 1);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("0", toAppendTo.toString());
 }

 @Test
 public void testFormatOne() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(1, 1);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1", toAppendTo.toString());
 }

 @Test
 public void testFormatProperFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(3, 4);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("3/4", toAppendTo.toString());
 }

 @Test
 public void testFormatImproperFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(5, 2);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("2 1/2", toAppendTo.toString());
 }

 @Test
 public void testFormatMixedNumber() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(7, 2);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("3 1/2", toAppendTo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNegativeNumber() {
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(-3, 2);
 properFractionFormat.format(fraction, toAppendTo, pos);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseZero() {
 Fraction fraction = properFractionFormat.parse("0", new ParsePosition(0, 0));
 assertEquals(new Fraction(0, 1), fraction);
 }

 @Test
 public void testParseOne() {
 Fraction fraction = properFractionFormat.parse("1", new ParsePosition(0, 0));
 assertEquals(new Fraction(1, 1), fraction);
 }

 @Test
 public void testParseProperFraction() {
 Fraction fraction = properFractionFormat.parse("3/4", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 4), fraction);
 }

 @Test
 public void testParseImproperFraction() {
 Fraction fraction = properFractionFormat.parse("2 1/2", new ParsePosition(0, 0));
 assertEquals(new Fraction(2, 2), fraction.getNumerator());
 assertEquals(new Fraction(1, 2), fraction.getDenominator());
 }

 @Test
 public void testParseMixedNumber() {
 Fraction fraction = properFractionFormat.parse("3 1/2", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 1), fraction);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidInput() {
 Fraction fraction = properFractionFormat.parse("3/4/2", new ParsePosition(0, 0));
 fail("Expected ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseNegativeNumber() {
 Fraction fraction = properFractionFormat.parse("-3/2", new ParsePosition(0, 0));
 fail("Expected ParseException");
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat numberFormat = new DecimalFormat();
 ProperFractionFormat properFractionFormat1 = new ProperFractionFormat();
 properFractionFormat1.format(new Fraction(1, 2), new StringBuffer(), null);
 assertEquals("1/2", properFractionFormat1.getFormattedFraction(new Fraction(1, 2)).toString());
 ProperFractionFormat properFractionFormat2 = new ProperFractionFormat();
 properFractionFormat2.setWholeFormat(numberFormat);
 properFractionFormat2.format(new Fraction(1, 2), new StringBuffer(), null);
 assertEquals("1", properFractionFormat2.getFormattedFraction(new Fraction(1, 2)).toString());
 }

 @Test
 public void testParseWithPrecision() {
 Fraction fraction = properFractionFormat.parse("0.123456789", new ParsePosition(0, 0));
 assertTrue(Precision.isClose(fraction.getNumerator().doubleValue(), 0.123456789, 1e-10));
 assertTrue(Precision.isClose(fraction.getDenominator().doubleValue(), 1.0, 1e-10));
 }

 @Test
 public void testFormatWithNumberFormat() {
 NumberFormat numberFormat = new DecimalFormat("#0.##########");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0);
 Fraction fraction = new Fraction(3, 7);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1 2/7", toAppendTo.toString());
 }
}