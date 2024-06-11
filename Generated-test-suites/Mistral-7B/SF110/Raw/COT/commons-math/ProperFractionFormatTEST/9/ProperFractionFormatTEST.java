import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat;
 private ProperFractionFormat properFractionFormat;

 @BeforeClass
 public static void setupClass() {
 defaultNumberFormat = NumberFormatUtils.createNumberFormat();
 }

 @Before
 public void setup() {
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, defaultNumberFormat);
 }

 // Test constructor without arguments
 @Test
 public void testProperFractionFormatDefaultConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertNotNull(pff);
 }

 // Test constructor with NumberFormat argument
 @Test
 public void testProperFractionFormatConstructor() {
 ProperFractionFormat pff = new ProperFractionFormat(defaultNumberFormat);
 assertNotNull(pff);
 }

 // Test constructor with three NumberFormat arguments
 @Test
 public void testProperFractionFormatThreeArgumentsConstructor() {
 NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat();
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, defaultNumberFormat, defaultNumberFormat);
 assertNotNull(pff);
 }

 // Test method format
 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(2, 3);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.format(fraction, toAppendTo, null);
 String formattedFraction = toAppendTo.toString();
 assertEquals("2/3", formattedFraction);

 fraction = new Fraction(5, 4);
 toAppendTo = new StringBuffer();
 pff = new ProperFractionFormat();
 pff.format(fraction, toAppendTo, null);
 formattedFraction = toAppendTo.toString();
 assertEquals("5/4", formattedFraction);

 fraction = new Fraction(1, 1);
 toAppendTo = new StringBuffer();
 pff = new ProperFractionFormat();
 pff.format(fraction, toAppendTo, null);
 formattedFraction = toAppendTo.toString();
 assertEquals("1", formattedFraction);
 }

 // Test getWholeFormat method
 @Test
 public void testGetWholeFormat() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, defaultNumberFormat, defaultNumberFormat);
 assertEquals(wholeFormat, pff.getWholeFormat());
 }

 // Test parse method with valid input
 @Test
 public void testParseValidInput() {
 String source = "1 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), parsedFraction);
 pos.setIndex(source.length());

 source = "5 4/5";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(5, 4), parsedFraction);

 source = "1";
 parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 1), parsedFraction);
 }

 // Test parse method with invalid input (whitespace only)
 @Test(expected = NumberFormatException.class)
 public void testParseWhitespaceOnly() {
 String source = " ";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.parse(source, pos);
 }

 // Test parse method with invalid input (whole number only)
 @Test(expected = NumberFormatException.class)
 public void testParseWholeNumberOnly() {
 String source = "5";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.parse(source, pos);
 }

 // Test parse method with invalid input (numerator is negative number)
 @Test(expected = NumberFormatException.class)
 public void testParseNegativeNumerator() {
 String source = "1 -1/2";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.parse(source, pos);
 }

 // Test parse method with invalid input (denominator is negative number)
 @Test(expected = NumberFormatException.class)
 public void testParseNegativeDenominator() {
 String source = "1 2/-3";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.parse(source, pos);
 }

 // Test parse method with invalid input (whole number and fraction format mixed)
 @Test(expected = NumberFormatException.class)
 public void testParseMixedInput() {
 String source = "1 2/3 4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.parse(source, pos);
 }

 // Test setWholeFormat method with null argument
 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullArgument() {
 ProperFractionFormat pff = new ProperFractionFormat();
 try {
 pff.setWholeFormat(null);
 } catch (NullArgumentException e) {
 assertEquals("WHOLE_FORMAT", e.getKey());
 }
 }
}