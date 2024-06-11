import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private ProperFractionFormat properFractionFormatWithCustomFormats;

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String formattedString = new StringBuffer().append(3).append(" ").append("/ ").append(4).append(" ")
 .append(properFractionFormat.format(fraction, new StringBuffer(), null)).toString();
 assertEquals("3 / 4", formattedString);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMinimumFractionDigits(1);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
 ProperFractionFormat properFractionFormatWithCustomFormats = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(1, 3);
 String formattedString = new StringBuffer().append(1).append(" ").append("/ ").append(3).append(" ")
 .append(properFractionFormatWithCustomFormats.format(fraction, new StringBuffer(), null)).toString();
 assertEquals("1 1/3", formattedString);
 }

 @Test
 public void testParse() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 Fraction fraction = properFractionFormat.parse("5/8", new ParsePosition(0, null));
 assertEquals(new Fraction(5, 8), fraction);

 ProperFractionFormat properFractionFormatWithCustomFormats = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
 Fraction fractionWithWhole = properFractionFormatWithCustomFormats.parse("5 1/3", new ParsePosition(0, null));
 assertEquals(new Fraction(6, 3), fractionWithWhole);
 }

 @Test
 public void testParseInvalidInput() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 ParsePosition pos = new ParsePosition(0, null);
 assertNull(properFractionFormat.parse("5", pos));
 assertNull(properFractionFormat.parse("5/", pos));
 assertNull(properFractionFormat.parse("5/-", pos));
 }

 @Test
 public void testParseInvalidInputWithCustomFormats() {
 ProperFractionFormat properFractionFormatWithCustomFormats = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
 ParsePosition pos = new ParsePosition(0, null);
 assertNull(properFractionFormatWithCustomFormats.parse("5", pos));
 assertNull(properFractionFormatWithCustomFormats.parse("5/", pos));
 assertNull(properFractionFormatWithCustomFormats.parse("5/-", pos));
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 properFractionFormat.setWholeFormat(wholeFormat);
 Fraction fraction = new Fraction(1, 2);
 String formattedString = new StringBuffer().append(wholeFormat.format(1)).append(" ").append("/ ")
 .append(properFractionFormat.format(fraction, new StringBuffer(), null).toString().split("/")[1])
 .append(" ")
 .append(properFractionFormat.format(fraction, new StringBuffer(), null)).toString();
 assertEquals("1 1/2", formattedString);
 }
}