import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Before
 public void setUp() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @Test
 public void testEmptyString() {
 StringBuffer result = new StringBuffer();
 Fraction fraction = new Fraction();
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("", result.toString());
 }

 @Test
 public void testFormatWholeNumber() {
 StringBuffer result = new StringBuffer();
 Fraction fraction = new Fraction(3);
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("3", result.toString());
 }

 @Test
 public void testFormatProperFraction() {
 StringBuffer result = new StringBuffer();
 Fraction fraction = new Fraction(3, 4);
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("3 / 4", result.toString());
 }

 @Test
 public void testFormatMixedFraction() {
 StringBuffer result = new StringBuffer();
 Fraction fraction = new Fraction(2, 5);
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("2 2 / 5", result.toString());
 }

 @Test
 public void testFormatLargeFraction() {
 StringBuffer result = new StringBuffer();
 Fraction fraction = new Fraction(new BigInteger("1234567890"), new BigInteger("987654321"));
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("1234567890 / 987654321", result.toString());
 }

 @Test
 public void testParseWholeNumber() {
 Fraction fraction = format.parse("3", new ParsePosition(0));
 assertEquals(3, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test
 public void testParseProperFraction() {
 Fraction fraction = format.parse("3 / 4", new ParsePosition(0));
 assertEquals(3, fraction.getNumerator());
 assertEquals(4, fraction.getDenominator());
 }

 @Test
 public void testParseMixedFraction() {
 Fraction fraction = format.parse("2 2 / 5", new ParsePosition(0));
 assertEquals(2, fraction.getNumerator());
 assertEquals(5, fraction.getDenominator());
 }

 @Test
 public void testParseLargeFraction() {
 Fraction fraction = format.parse("1234567890 / 987654321", new ParsePosition(0));
 assertEquals(new BigInteger("1234567890"), fraction.getNumerator());
 assertEquals(new BigInteger("987654321"), fraction.getDenominator());
 }

 @Test
 public void testParseInvalidWholeNumber() {
 expectedException.expect(NumberFormatException.class);
 expectedException.expectMessage("For input string: \"abc\"");
 format.parse("abc", new ParsePosition(0));
 }

 @Test
 public void testParseInvalidNumerator() {
 expectedException.expect(NumberFormatException.class);
 expectedException.expectMessage("For input string: \"3 / def\"");
 format.parse("3 / def", new ParsePosition(0));
 }

 @Test
 public void testParseInvalidDenominator() {
 expectedException.expect(NumberFormatException.class);
 expectedException.expectMessage("For input string: \"3 / -2\"");
 format.parse("3 / -2", new ParsePosition(0));
 }

 @Test
 public void testParseInvalidFormat() {
 expectedException.expect(NumberFormatException.class);
 expectedException.expectMessage("For input string: \"3 / / 2\"");
 format.parse("3 / / 2", new ParsePosition(0));
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat newWholeFormat = new DecimalFormat("#,##0");
 format.setWholeFormat(newWholeFormat);
 Fraction fraction = new Fraction(3);
 StringBuffer result = new StringBuffer();
 format.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("3", result.toString());
}
}