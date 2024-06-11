import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat defaultNumberFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 defaultNumberFormat = FractionFormat.getDefaultNumberFormat();
 }

 @Test
 public void testConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertNotNull(pff);

 ProperFractionFormat pffWithFormat = new ProperFractionFormat(defaultNumberFormat);
 assertNotNull(pffWithFormat);
 }

 @Test
 public void testFormatMethodWithZeroFraction() {
 StringBuffer result = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(0, 1);
 pff.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("0 / 1", result.toString());
 }

 @Test
 public void testFormatMethodWithPositiveFraction() {
 StringBuffer result = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 4);
 pff.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("3 / 4", result.toString());
 }

 @Test
 public void testFormatMethodWithNegativeFraction() {
 StringBuffer result = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(-3, 4);
 pff.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("-3 / 4", result.toString());
 }

 @Test
 public void testFormatMethodWithWholePart() {
 StringBuffer result = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(4, 1);
 pff.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("4", result.toString());
 }

 @Test
 public void testFormatMethodWithLargeNumeratorAndDenominator() {
 StringBuffer result = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 Fraction fraction = new Fraction(1234567, 987654321);
 pff.format(fraction, result, new FieldPosition(0, 0));
 assertEquals("1234567 / 987654321", result.toString());
 }

 @Test
 public void testParseMethodWithWholeNumber() {
 Fraction result = properFractionFormat.parse("4", new ParsePosition(0, 0));
 assertEquals(new Fraction(4, 1), result);
 }

 @Test
 public void testParseMethodWithFraction() {
 Fraction result = properFractionFormat.parse("3/4", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 4), result);
 }

 @Test
 public void testParseMethodWithWhitespace() {
 Fraction result = properFractionFormat.parse(" 3 / 4 ", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 4), result);
 }

 @Test
 public void testParseMethodWithLeadingMinus() {
 Fraction result = properFractionFormat.parse("-3/4", new ParsePosition(0, 0));
 assertEquals(new Fraction(-3, 4), result);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseMethodWithNullFormat() {
 ProperFractionFormat pff = new ProperFractionFormat(null);
 String input = "3/4";
 Fraction result = pff.parse(input, new ParsePosition(0, 0));
 fail("Parse method should throw NullArgumentException for null format.");
 }

 @Test(expected = NullArgumentException.class)
 public void testParseMethodWithNullInput() {
 ProperFractionFormat pff = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, 0);
 String input = null;
 Fraction result = pff.parse(input, pos);
 fail("Parse method should throw NullArgumentException for null input.");
 }

 @Test(expected = NullArgumentException.class)
 public void testParseMethodWithNullParsePosition() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String input = "3/4";
 ParsePosition pos = null;
 Fraction result = pff.parse(input, pos);
 fail("Parse method should throw NullArgumentException for null parse position.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMethodWithInvalidFractionFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String input = "3/0";
 ParsePosition pos = new ParsePosition(0, 0);
 Fraction result = pff.parse(input, pos);
 fail("Parse method should throw IllegalArgumentException for invalid fraction format.");
 pos.setIndex(pos.getErrorIndex());
 assertEquals("3/0", input.substring(0, pos.getIndex()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMethodWithNegativeDenominator() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String input = "3/-4";
 ParsePosition pos = new ParsePosition(0, 0);
 Fraction result = pff.parse(input, pos);
 fail("Parse method should throw IllegalArgumentException for negative denominator.");
 pos.setIndex(pos.getErrorIndex());
 assertEquals("3/-4", input.substring(0, pos.getIndex()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 NumberFormat wholeFormat = null;
 pff.setWholeFormat(wholeFormat);
 }
}