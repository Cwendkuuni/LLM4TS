import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat numberFormat;
 private ProperFractionFormat properFractionFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 numberFormat = new DecimalFormat("#.##");
 properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 }

 @Test
 public void testFormatProperFraction() {
 // Test a simple proper fraction
 Fraction fraction = new Fraction(2, 3);
 String expected = "0 2 / 3";
 String actual = new StringBuffer().append(properFractionFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);

 // Test a fraction with a whole part
 fraction = new Fraction(4, 3);
 expected = "1 1 / 3";
 actual = new StringBuffer().append(properFractionFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);

 // Test a fraction with a negative numerator
 fraction = new Fraction(-2, 3);
 expected = "0 -2 / 3";
 actual = new StringBuffer().append(properFractionFormat.format(fraction, null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullArgumentException.class)
 public void testFormatNullFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 Fraction fraction = null;
 properFractionFormat.format(fraction, toAppendTo, null);
 }

 @Test
 public void testParseProperFraction() {
 // Test a simple proper fraction
 String source = "0 2 / 3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 Fraction expected = new Fraction(2, 3);
 assertEquals(expected, actual);

 // Test a fraction with a whole part
 source = "1 1 / 3";
 pos = new ParsePosition(0);
 actual = properFractionFormat.parse(source, pos);
 expected = new Fraction(1, 3);
 assertEquals(expected, actual);

 // Test a fraction with a negative numerator
 source = "0 -2 / 3";
 pos = new ParsePosition(0);
 actual = properFractionFormat.parse(source, pos);
 expected = new Fraction(-2, 3);
 assertEquals(expected, actual);

 // Test an invalid input
 source = "0 2 / 3ab";
 pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse(source, pos);
 assertNull(result);
 // check the parse position
 assertEquals(5, pos.getIndex());
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullString() {
 String source = null;
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse(source, pos);
 }
}