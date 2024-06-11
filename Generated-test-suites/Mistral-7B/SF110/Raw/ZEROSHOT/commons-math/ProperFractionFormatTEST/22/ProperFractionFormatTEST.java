import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat integerFormat;
 private NumberFormat defaultNumeratorFormat;
 private NumberFormat defaultDenominatorFormat;

 @Before
 public void setup() {
 properFractionFormat = new ProperFractionFormat();
 integerFormat = NumberFormat.getIntegerInstance();
 defaultNumeratorFormat = new DecimalFormat("#");
 defaultDenominatorFormat = new DecimalFormat("#");
 }

 @Test
 public void testEmptyString() {
 StringBuffer expected = new StringBuffer();
 StringBuffer result = properFractionFormat.format(new Fraction(0, 1), expected, new FieldPosition(0, 0));
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testZeroFraction() {
 String expected = "0";
 String result = properFractionFormat.format(new Fraction(0, 1), new StringBuffer(), new FieldPosition(0, 0)).toString();
 assertEquals(expected, result);
 }

 @Test
 public void testSimpleProperFraction() {
 String expected = "1 1 / 1";
 String result = properFractionFormat.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString();
 assertEquals(expected, result);
 }

 @Test
 public void testSimpleImproperFraction() {
 String expected = "2 1 / 1";
 String result = properFractionFormat.format(new Fraction(2, 1), new StringBuffer(), new FieldPosition(0, 0)).toString();
 assertEquals(expected, result);
 }

 @Test
 public void testSimpleMixedFraction() {
 String expected = "1 2 / 1";
 String result = properFractionFormat.format(new Fraction(2, 1), new StringBuffer(), new FieldPosition(0, 0)).toString();
 assertEquals(expected, result);
 }

 @Test
 public void testNegativeFraction() {
 String expected = "-1 1 / 1";
 String result = properFractionFormat.format(new Fraction(-1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString();
 assertEquals(expected, result);
 }

 @Test(expected = NullArgumentException.class)
 public void testNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, defaultNumeratorFormat, defaultDenominatorFormat);
 assertNull(format.getWholeFormat());
 try {
 format.setWholeFormat(null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 //Expected
 }
 }

 @Test
 public void testParseSimpleProperFraction() {
 Fraction expected = new Fraction(1, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse("1 1 / 1", pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseSimpleImproperFraction() {
 Fraction expected = new Fraction(2, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse("2 1 / 1", pos);
 assertEquals(expected, result);
 }

 @Test
 public void testParseSimpleMixedFraction() {
 Fraction expected = new Fraction(2, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction result = properFractionFormat.parse("1 2 / 1", pos);
 assertEquals(expected, result);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullString() {
 Fraction result = properFractionFormat.parse(null, new ParsePosition(0));
 fail("Expected NullArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFraction() {
 ParsePosition pos = new ParsePosition(0);
 try {
 properFractionFormat.parse("1 /", pos);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //Expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testParseWithNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, defaultNumeratorFormat, defaultDenominatorFormat);
 ParsePosition pos = new ParsePosition(0);
 try {
 format.parse("1 1 / 1", pos);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 //Expected
 }
 }
}