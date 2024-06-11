import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat, numeratorFormat, denominatorFormat;

 @Test
 public void testProperFractionFormat() {
  format = new ProperFractionFormat();
  wholeFormat = new DecimalFormat("#");
  numeratorFormat = new DecimalFormat("#");
  denominatorFormat = new DecimalFormat("#");
  format.setWholeFormat(wholeFormat);
  format.setNumeratorFormat(numeratorFormat);
  format.setDenominatorFormat(denominatorFormat);

 // Test proper fraction
 Fraction fraction = new Fraction(2, 3);
 String expected = "2 / 3";
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String actual = toAppendTo.toString();
 assertEquals(expected, actual);

 // Test fraction with whole part
 fraction = new Fraction(4, 3);
 expected = "4";
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 actual = toAppendTo.toString();
 assertEquals(expected, actual);
 toAppendTo.setLength(0);
 expected = "4 ";
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 actual = toAppendTo.toString();
 assertEquals(expected, actual);

 // Test negative numerator
 fraction = new Fraction(-2, 3);
 expected = "-2 / 3";
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 actual = toAppendTo.toString();
 assertEquals(expected, actual);

 // Test parse valid fraction
 String source = "2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsed = format.parse(source, pos);
 assertEquals(fraction, parsed);
 pos.setIndex(0);
 source = "4";
 parsed = format.parse(source, pos);
 assertEquals(new Fraction(4, 1), parsed);

 // Test parse invalid fraction
 source = "2/0";
 parsed = format.parse(source, pos);
 assertNull(parsed);
 pos.setIndex(0);
 source = "2/3a";
 parsed = format.parse(source, pos);
 assertNull(parsed);
 pos.setIndex(0);
 source = "2/3.";
 parsed = format.parse(source, pos);
 assertNull(parsed);
 }
}