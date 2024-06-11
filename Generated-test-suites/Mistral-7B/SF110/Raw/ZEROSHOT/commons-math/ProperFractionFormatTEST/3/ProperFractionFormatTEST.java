import junit.framework.TestCase;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest extends TestCase {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat integerFormat;
 private NumberFormat decimalFormat;

 @Before
 public void setUp() {
 super.setUp();

 properFractionFormat = new ProperFractionFormat();
 integerFormat = NumberFormat.getNumberInstance(Locale.US);
 decimalFormat = NumberFormat.getNumberInstance(Locale.US);
 decimalFormat.setMinimumFractionDigits(2);
 decimalFormat.setMaximumFractionDigits(2);
 }

 @Test
 public void testDefaultFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testWholeFormat() {
 Fraction fraction = new Fraction(2, 3);
 String expected = "2 1/3";
 String actual = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithWhole() {
 String input = "2 1/3";
 Fraction expected = new Fraction(2, 3);
 Fraction actual = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithoutWhole() {
 String input = "1/3";
 Fraction expected = new Fraction(1, 3);
 Fraction actual = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidInput() {
 String input = "1 a";
 ProperFractionFormat format = new ProperFractionFormat(integerFormat, decimalFormat, decimalFormat);
 Fraction result = new Fraction();
 ParsePosition pos = new ParsePosition(0);
 format.parse(input, result, pos);
 fail("Should throw NumberFormatException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 try {
 format.setWholeFormat(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetIntegerFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(integerFormat);
 Fraction fraction = new Fraction(3, 2);
 String expected = "3 1/2";
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetDecimalFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(decimalFormat);
 Fraction fraction = new Fraction(1, 7);
 String expected = "1.00 1/7";
 String actual = format.format(fraction, new StringBuffer(), null).toString();
 assertEquals(expected, actual);
 }
}