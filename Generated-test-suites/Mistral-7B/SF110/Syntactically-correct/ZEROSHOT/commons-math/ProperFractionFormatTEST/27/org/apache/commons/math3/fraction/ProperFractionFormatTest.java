import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.math.BigDecimal;
import org.apache.commons.math3.util.math.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Test
 public void testProperFractionFormatInstance() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testProperFractionFormatWithCustomFormats() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("#.#");
 NumberFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testFormatFraction() {
 Fraction fraction = new Fraction(3, 4);
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 StringBuffer toAppendTo = new StringBuffer();
 FieldPosition pos = new FieldPosition(0, 0);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("3 / 4", toAppendTo.toString());

 fraction = new Fraction(8, 9);
 toAppendTo.setLength(0);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("8 / 9", toAppendTo.toString());

 fraction = new Fraction(2, 1);
 toAppendTo.setLength(0);
 properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("2", toAppendTo.toString());
 }

 @Test
 public void testGetWholeFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
 assertNotNull(wholeFormat);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 fail("NullArgumentException expected");
 } catch (NullArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseFraction() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);

 // Test parsing whole number
 String source = "2";
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(2, 1), parsedFraction);

 // Test parsing proper fraction
 source = "3/4";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3, 4), parsedFraction);

 // Test parsing improper fraction
 source = "7/4";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2) + new Fraction(3, 4), parsedFraction);

 // Test parsing fraction with leading zero
 source = "0 1/2";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);

 // Test parsing fraction with trailing zero
 source = "3/4.";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3, 4), parsedFraction);

 // Test parsing invalid fraction
 source = "3/0";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertNull(parsedFraction);
 }

 @Test(expected = ParseException.class)
 public void testParseFractionWithInvalidInput() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);

 // Test parsing invalid input with whole number
 String source = "invalidNumber";
 properFractionFormat.parse(source, pos);
 fail("ParseException expected");

 // Test parsing invalid input with improper fraction
 source = "invalidNumber 1/2";
 properFractionFormat.parse(source, pos);
 fail("ParseException expected");
 }

 @Test
 public void testParseFractionWithBigDecimal() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0);
 BigDecimal num = new BigDecimal("1.5");
 BigDecimal denom = new BigDecimal("3");
 Fraction parsedFraction = properFractionFormat.parse("3/" + num.toString() + " " + denom.toString(), pos);
 Fraction expectedFraction = new Fraction(3, 3).add(new Fraction(1, 2));
 assertEquals(expectedFraction, parsedFraction);
 }

 @Test(expected = NumberFormatException.class)
 public void testSetWholeFormatWithInvalidFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(new NumberFormatter()); // Invalid Format
 fail("NumberFormatException expected");
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}