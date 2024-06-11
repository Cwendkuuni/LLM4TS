import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProperFractionFormatTest {

 private NumberFormat numberFormat;
 private FractionFormat properFractionFormat;

 @Test
 public void testProperFractionFormatConstructor() {
 numberFormat = new DecimalFormat("#");
 properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 }

 @Test
 public void testFormatSimpleFraction() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testFormatFractionWithWholePart() {
 Fraction fraction = new Fraction(3, 2);
 String expected = "1 1 / 2";
 StringBuffer toAppendTo = new StringBuffer();
 properFractionFormat.format(fraction, toAppendTo, new FieldPosition(0, 0));
 assertEquals(expected, toAppendTo.toString());
 }

 @Test
 public void testParseSimpleFraction() {
 Fraction fraction = properFractionFormat.parse("1 / 2", new ParsePosition(0, 0));
 assertEquals(new Fraction(1, 2), fraction);
 }

 @Test
 public void testParseFractionWithWholePart() {
 Fraction fraction = properFractionFormat.parse("1 1 / 2", new ParsePosition(0, 0));
 assertEquals(new Fraction(3, 2), fraction);
 }

 @Test
 public void testParseInvalidInput() {
 Fraction fraction = properFractionFormat.parse("1 / 2 a", new ParsePosition(0, 0));
 assertNull(fraction);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 NumberFormat wholeFormat = null;
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.setWholeFormat(wholeFormat);
 }
}