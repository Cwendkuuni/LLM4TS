import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtil;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = NumberFormatUtil.createNumberInstance(NumberFormat.Style.DECIMAL, Locale.US);
 numeratorFormat = NumberFormatUtil.createNumberInstance(NumberFormat.Style.DECIMAL, Locale.US);
 denominatorFormat = NumberFormatUtil.createNumberInstance(NumberFormat.Style.DECIMAL, Locale.US);
 }

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertEquals(pff.getClass(), ProperFractionFormat.class);
 }

 @Test
 public void testConstructorWithFormat() {
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat);
 assertEquals(pff.getWholeFormat(), wholeFormat);
 }

 @Test
 public void testConstructorWithThreeFormats() {
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertEquals(pff.getWholeFormat(), wholeFormat);
 assertEquals(pff.getNumeratorFormat(), numeratorFormat);
 assertEquals(pff.getDenominatorFormat(), denominatorFormat);
 }

 @Test
 public void testFormatMethodWithValidInput() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer expectedOutput = new StringBuffer();
 properFractionFormat.format(fraction, expectedOutput, new FieldPosition(0, 0));
 String actualOutput = expectedOutput.toString();
 String expectedResult = "1 1 / 2";
 assertEquals(expectedResult, actualOutput);
 }

 @Test
 public void testFormatMethodWithZeroWholePart() {
 Fraction fraction = new Fraction(2, 4);
 StringBuffer expectedOutput = new StringBuffer();
 properFractionFormat.format(fraction, expectedOutput, new FieldPosition(0, 0));
 String actualOutput = expectedOutput.toString();
 String expectedResult = "1 / 2";
 assertEquals(expectedResult, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatMethodWithNegativeWholePart() {
 Fraction fraction = new Fraction(-3, 4);
 StringBuffer expectedOutput = new StringBuffer();
 properFractionFormat.format(fraction, expectedOutput, new FieldPosition(0, 0));
 }

 @Test
 public void testParseMethodWithProperFraction() {
 String source = "1 1 / 2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expectedOutput = new Fraction(1, 1, 2);
 Fraction actualOutput = properFractionFormat.parse(source, pos);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testParseMethodWithImproperFraction() {
 String source = "1 1 / 3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expectedOutput = new Fraction(1, 4, 3);
 Fraction actualOutput = properFractionFormat.parse(source, pos);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMethodWithInvalidInput() {
 String source = "1 1 / 0";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMethodWithNegativeNumerator() {
 String source = "1 -1 / 2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMethodWithWhitespaceBetweenComponents() {
 String source = "1 1 / 2 ";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullInput() {
 ProperFractionFormat pff = new ProperFractionFormat();
 try {
 pff.setWholeFormat(null);
 } catch (NullArgumentException e) {
 assertEquals(e.getLocalizedMessage(), "WHOLE_FORMAT cannot be null.");
 }
 }
}