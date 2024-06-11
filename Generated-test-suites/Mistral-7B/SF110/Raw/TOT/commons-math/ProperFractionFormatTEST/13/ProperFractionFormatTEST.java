import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private Fraction fraction;

 @Test
 public void testFormatProperFraction() {
 Fraction inputFraction = new Fraction(4, 9);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "1 3/4";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testFormatMixedNumber() {
 Fraction inputFraction = new Fraction(5, 3);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "1 1 2/3";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testFormatZeroFraction() {
 Fraction inputFraction = new Fraction(0, 1);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "0";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testFormatNegativeFraction() {
 Fraction inputFraction = new Fraction(-3, 4);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "-3/4";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testFormatLargeFraction() {
 Fraction inputFraction = new Fraction(12345, 6789);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "1860 339/6789";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testFormatWholeNumber() {
 Fraction inputFraction = new Fraction(3);
 ProperFractionFormat properFormat = new ProperFractionFormat();
 String expectedOutput = "3";
 StringBuffer outputBuffer = new StringBuffer();
 properFormat.format(inputFraction, outputBuffer, null);
 assertEquals(expectedOutput, outputBuffer.toString());
 }

 @Test
 public void testParseProperFraction() {
 String inputString = "1 3/4";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = new Fraction(4, 9);
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertEquals(expectedOutput, resultFraction);
 }

 @Test
 public void testParseMixedNumber() {
 String inputString = "1 1 2/3";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = new Fraction(5, 3);
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertEquals(expectedOutput, resultFraction);
 }

 @Test
 public void testParseZeroFraction() {
 String inputString = "0";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = new Fraction(0, 1);
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertEquals(expectedOutput, resultFraction);
 }

 @Test
 public void testParseNegativeFraction() {
 String inputString = "-3/4";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = new Fraction(-3, 4);
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertEquals(expectedOutput, resultFraction);
 }

 @Test
 public void testParseLargeFraction() {
 String inputString = "1860 339/6789";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = new Fraction(12345, 6789);
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertEquals(expectedOutput, resultFraction);
 }

 @Test
 public void testParseInvalidInput() {
 String inputString = "1 3/0";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = null;
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertNull(resultFraction);
 assertTrue(pos.getIndex() == 0);
 }

 @Test
 public void testParseInvalidCharacter() {
 String inputString = "1 a/2";
 ProperFractionFormat properFormat = new ProperFractionFormat();
 Fraction expectedOutput = null;
 ParsePosition pos = new ParsePosition(0);
 Fraction resultFraction = properFormat.parse(inputString, pos);
 assertNull(resultFraction);
 assertTrue(pos.getIndex() == 0);
 }
}