import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private final ProperFractionFormat properFractionFormat = new ProperFractionFormat();

 @Test
 public void testFormatProperFraction() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 String actual = new StringBuffer().append(properFractionFormat.format(fraction, new StringBuffer(), null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMixedFraction() {
 Fraction fraction = new Fraction(4, 3);
 String expected = "1 1/3";
 String actual = new StringBuffer().append(properFractionFormat.format(fraction, new StringBuffer(), null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWholeNumber() {
 int wholeNumber = 5;
 Fraction fraction = new Fraction(wholeNumber, 1);
 String expected = String.valueOf(wholeNumber);
 String actual = new StringBuffer().append(properFractionFormat.format(fraction, new StringBuffer(), null)).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testFormatNullFraction() {
 StringBuffer toAppendTo = new StringBuffer();
 String expectedErrorMessage = "fraction is markedly null";
 try {
 properFractionFormat.format(null, toAppendTo, null);
 } catch (NullPointerException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testParseProperFraction() {
 String source = "1/2";
 Fraction expected = new Fraction(1, 2);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMixedFraction() {
 String source = "1 1/3";
 Fraction expected = new Fraction(1, 3);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWholeNumber() {
 String source = "5";
 Fraction expected = new Fraction(5, 1);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test(expected = ParsePosition.ParseException.class)
 public void testParseInvalidFraction() {
 String source = "1/0";
 ParsePosition pos = new ParsePosition(0);
 try {
 properFractionFormat.parse(source, pos);
 } catch (ParsePosition.ParseException e) {
 // Expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullSource() {
 String source = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 properFractionFormat.parse(source, pos);
 } catch (NullArgumentException e) {
 String expectedMessage = "source is markedly null";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWholeFormatNullFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 try {
 properFractionFormat.setWholeFormat(null);
 } catch (IllegalArgumentException e) {
 String expectedMessage = "format is markedly null";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

}