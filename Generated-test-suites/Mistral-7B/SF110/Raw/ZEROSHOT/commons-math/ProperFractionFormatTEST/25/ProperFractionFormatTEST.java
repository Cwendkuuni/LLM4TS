import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat integerFormat;
 private NumberFormat decimalFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 integerFormat = new DecimalFormat("#");
 decimalFormat = new DecimalFormat("#.#");
 }

 @Test
 public void testProperFractionFormatDefault() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "1 2/3";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormatCustom() {
 ProperFractionFormat customFormat = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 fraction = new Fraction(2, 3);
 expected = "1 2/3";
 actual = customFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormatWholePart() {
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 0/2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test
 public void testProperFractionFormatNegativeNumerator() {
 Fraction fraction = new Fraction(-2, 3);
 String expected = "-1 2/3";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testProperFractionFormatNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, decimalFormat, decimalFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProperFractionFormatNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, null, decimalFormat);
 }

 @Test
 public void testParseProperFraction() {
 String source = "1 2/3";
 Fraction expected = new Fraction(2, 3);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseProperFractionWithWholePart() {
 String source = "2 3/4";
 Fraction expected = new Fraction(7, 4);
 Fraction actual = properFractionFormat.parse(source, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidProperFraction() {
 String source = "2 3/4abc";
 ProperFractionFormat.parse(source, new ParsePosition(0));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseProperFractionWithNegativeDenominator() {
 String source = "1 2/-3";
 ProperFractionFormat.parse(source, new ParsePosition(0));
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(integerFormat);
 Fraction fraction = new Fraction(4, 2);
 String expected = "2 0";
 String actual = format.format(fraction, new StringBuffer(), null);
 assertEquals(expected, actual);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNullFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(null);
 }
}