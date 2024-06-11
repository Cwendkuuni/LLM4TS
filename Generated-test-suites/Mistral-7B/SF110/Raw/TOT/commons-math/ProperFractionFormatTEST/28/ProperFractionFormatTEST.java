import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat numberFormat;
 private ProperFractionFormat properFractionFormat;

 @Before
 public void setUp() {
 numberFormat = new DecimalFormat("#.##");
 properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 }

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("1/2", result);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("3/4", result);
 }

 @Test
 public void testFormatWholeNumber() {
 Fraction fraction = new Fraction(3);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("3", result);
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("-1/2", result);
 }

 @Test
 public void testFormatLargeFraction() {
 Fraction fraction = new Fraction(1000, 3000);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("333 1/3", result);
 }

 @Test(expected = NullArgumentException.class)
 public void testFormatNullFraction() {
 Fraction fraction = null;
 StringBuffer expectedResult = new StringBuffer();
 String result = properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0)).toString();
 fail("Expected NullArgumentException but got: " + result);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), fraction);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3, 4), fraction);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "3";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3), fraction);
 }

 @Test
 public void testParseNegativeFraction() {
 String source = "-1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, 2), fraction);
 }

 @Test
 public void testParseFractionWithWholePart() {
 String source = "3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(3, 2), fraction);
 assertNotNull(pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFraction() {
 String source = "1/0";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 fail("Expected ParseException but got: " + fraction);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidWholePart() {
 String source = "3 a";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 fail("Expected ParseException but got: " + fraction);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullString() {
 String source = null;
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 fail("Expected NullArgumentException but got: " + fraction);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumberFormat() {
 NumberFormat numberFormat = new DecimalFormat("# %");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 String source = "1";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException but got: " + fraction);
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat wholeFormat = new DecimalFormat("#,##0");
 properFractionFormat.setWholeFormat(wholeFormat);
 Fraction fraction = new Fraction(1000, 3000);
 StringBuffer expectedResult = new StringBuffer();
 properFractionFormat.format(fraction, expectedResult, new FieldPosition(0, 0));
 String result = expectedResult.toString();
 assertEquals("333,333", result.substring(0, result.indexOf("/")).trim());
 }
}