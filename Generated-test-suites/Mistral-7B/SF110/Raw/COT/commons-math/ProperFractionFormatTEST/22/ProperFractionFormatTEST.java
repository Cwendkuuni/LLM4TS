import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ParseException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = new DecimalFormat();
 numeratorFormat = new DecimalFormat();
 denominatorFormat = new DecimalFormat();
 }

 @Test
 public void testConstructor() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testConstructorWithFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat());
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testConstructorWithWholeFormatNumeratorFormatDenominatorFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertNotNull(properFractionFormat);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.ProperFractionFormat format = new ProperFractionFormat();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String result = toAppendTo.toString();
 assertEquals("3/4", result);
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
 fail();
 } catch (NullArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse() {
 String source = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 pos.setIndex(pos.getIndex() + source.length());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidFormat() {
 String source = "1 2/0";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat.parse(source, pos);
 fail();
 }

 @Test
 public void testParseWithZeroNumerator() {
 String source = "0/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(0), parsedFraction);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testParseWithNegativeNumber() {
 String source = "-2/3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat.parse(source, pos);
 fail();
 }

 @Test
 public void testParseWithLeadingWhitespace() {
 String source = " 1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithTrailingWhitespace() {
 String source = "1 2/3 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithMultipleWhitespaces() {
 String source = " 1 2 / 3 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithLeadingZero() {
 String source = "0 1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(0, 1), parsedFraction);
 }

 @Test
 public void testParseWithTrailingZero() {
 String source = "1 2/3 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithMultipleWhiteAndZero() {
 String source = "0 1 2 / 3 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(0, 1), parsedFraction);
 }

 @Test
 public void testParseWithFractionPartOnly() {
 String source = "2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(0, 2), parsedFraction);
 }

 @Test
 public void testParseWithWholeAndFractionPart() {
 String source = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholePart() {
 String source = "-1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, 2), parsedFraction);
 }

 @Test
 public void testParseWithFractionPartDenominatorZero() {
 String source = "1 0/3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartNumeratorNegative() {
 String source = "1 -2/3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartDenominatorNegative() {
 String source = "1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartMixedDenominatorAndNumeratorSigns() {
 String source = "1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartNumeratorAndDenominatorSameSign() {
 String source = "1 2/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testParseWithFractionPartNumeratorAndDenominatorDifferentSigns() {
 String source = "1 2/-2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, -2), parsedFraction);
 }

 @Test
 public void testParseWithLargeFractionPart() {
 String source = "12345678901234567890 12345678901234567890/12345678901234567890";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(12345678901234567890L, 12345678901234567890L), parsedFraction);
 }

 @Test
 public void testParseWithFractionPartNumeratorLargerThanDenominator() {
 String source = "1 1/-2";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (ArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartDenominatorLargerThanNumerator() {
 String source = "1 2/1";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (ArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartDenominatorZeroAndNegative() {
 String source = "1 0/-3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithFractionPartNegativeDenominator() {
 String source = "1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPart() {
 String source = "-1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, 2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartDenominatorZero() {
 String source = "-1 0/3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartNumeratorNegative() {
 String source = "-1 -2/3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartDenominatorNegative() {
 String source = "-1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, -2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartMixedDenominatorAndNumeratorSigns() {
 String source = "-1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, -2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartNumeratorAndDenominatorSameSign() {
 String source = "-1 2/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, 2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartNumeratorAndDenominatorDifferentSigns() {
 String source = "-1 2/-2";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, -2), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndLargeFractionPart() {
 String source = "-12345678901234567890 12345678901234567890/12345678901234567890";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-12345678901234567890L, 12345678901234567890L), parsedFraction);
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartNumeratorLargerThanDenominator() {
 String source = "-1 1 1/-2";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (ArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartDenominatorLargerThanNumerator() {
 String source = "-1 2/1";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (ArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartDenominatorZeroAndNegative() {
 String source = "-1 0/-3";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat.parse(source, pos);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseWithNegativeWholeAndFractionPartNegativeDenominator() {
 String source = "-1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = ProperFractionFormat.parse(source, pos);
 assertEquals(new Fraction(-1, -2), parsedFraction);
 }
}