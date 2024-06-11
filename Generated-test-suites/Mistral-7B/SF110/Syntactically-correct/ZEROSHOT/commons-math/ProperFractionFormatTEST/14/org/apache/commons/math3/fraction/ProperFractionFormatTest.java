import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Test
 public void testProperFractionFormat() {
 properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testProperFractionFormatWithCustomFormats() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat customProperFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertNotNull(customProperFractionFormat);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatWithNullWholeFormat() {
 properFractionFormat = new ProperFractionFormat(null, numeratorFormat, denominatorFormat);
 fail("Expected NullArgumentException");
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 3);
 properFractionFormat = new ProperFractionFormat();
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1 ⁄ 3", toAppendTo.toString());

 fraction = new Fraction(5, 6);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("5 ⁄ 6", toAppendTo.toString());

 fraction = new Fraction(2, 4);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1 ₂ ⁄ ₂ ₄", toAppendTo.toString());
 }

 @Test
 public void testParse() {
 Fraction parsedFraction;
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();

 // Test parsing integers
 parsedFraction = properFractionFormat.parse("1", new ParsePosition(0, 0));
 assertEquals(new Fraction(1, 1), parsedFraction);

 // Test parsing whole numbers with fraction
 parsedFraction = properFractionFormat.parse("1 1/2", new ParsePosition(0, 0));
 assertEquals(new Fraction(2, 2), parsedFraction);

 // Test parsing proper fractions
 parsedFraction = properFractionFormat.parse("1 1/3", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 3), parsedFraction);

 parsedFraction = properFractionFormat.parse("5 1/2", new ParsePosition(0, 0));
 assertEquals(new Fraction(7, 2), parsedFraction);

 parsedFraction = properFractionFormat.parse("1 3/4", new ParsePosition(0, 0));
 assertEquals(new Fraction(5, 4), parsedFraction);

 // Test parsing improper fractions
 parsedFraction = properFractionFormat.parse("1 1", new ParsePosition(0, 0));
 assertEquals(new Fraction(2, 1), parsedFraction);

 parsedFraction = properFractionFormat.parse("2 1/2", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 1), parsedFraction);

 parsedFraction = properFractionFormat.parse("3 1/3", new ParsePosition(0, 0));
 assertEquals(new Fraction(4, 1), parsedFraction);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidInput() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, 0);
 properFractionFormat.parse("InvalidInput", pos);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testSetWholeFormat() {
 wholeFormat = new DecimalFormat("#");
 properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setWholeFormat(wholeFormat);
 assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
 }
}