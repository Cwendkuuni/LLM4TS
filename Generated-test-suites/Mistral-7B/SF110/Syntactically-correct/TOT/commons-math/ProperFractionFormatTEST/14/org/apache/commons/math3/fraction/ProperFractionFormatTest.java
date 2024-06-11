import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat, numeratorFormat, denominatorFormat;

 @Test
 public void testProperFractionFormatDefault() {
 properFractionFormat = new ProperFractionFormat();
 Fraction fraction = new Fraction(1, 2);
 String formattedFraction = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals("1/2", formattedFraction);
 }

 @Test
 public void testProperFractionFormatCustom() {
 properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 Fraction fraction = new Fraction(3, 4);
 String formattedFraction = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals("3/4", formattedFraction);
 }

 @Test
 public void testProperFractionFormatWholeNumber() {
 Fraction fraction = new Fraction(1, 1);
 properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"));
 String formattedFraction = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals("1", formattedFraction);
 }

 @Test
 public void testProperFractionFormatNegativeNumerator() {
 Fraction fraction = new Fraction(-1, 2);
 properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 String formattedFraction = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals("-1/2", formattedFraction);
 }

 @Test
 public void testProperFractionFormatInvalidInput() {
 String source = "Invalid Input";
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(null, parsedFraction);
 }

 @Test
 public void testProperFractionFormatInvalidInputWithWhitespace() {
 String source = "Invalid Input   ";
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(null, parsedFraction);
 }

 @Test
 public void testProperFractionFormatInvalidInputWithWholeNumber() {
 String source = "123/456";
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(null, parsedFraction);
 }

 @Test
 public void testProperFractionFormatInvalidInputWithNegativeDenominator() {
 String source = "1/-2";
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(null, parsedFraction);
 }

 @Test
 public void testProperFractionFormatInvalidInputWithMixedFormat() {
 String source = "2 apples / 3 oranges";
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(new DecimalFormat("#"), new DecimalFormat("#"), new DecimalFormat("#"));
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = properFractionFormat.parse(source, pos);
 assertEquals(null, parsedFraction);
 }

 @Before
 public void setUp() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }
}