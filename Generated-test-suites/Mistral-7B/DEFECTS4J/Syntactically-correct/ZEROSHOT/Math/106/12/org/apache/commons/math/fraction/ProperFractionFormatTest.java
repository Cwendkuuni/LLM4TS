import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.simplifier.FractionFormat;
import org.apache.commons.text.simplifier.FractionFormat.ParsePosition;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat;
 private NumberFormat intFormat;
 private NumberFormat decimalFormat;
 private FractionFormat properFractionFormat;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 defaultNumberFormat = NumberFormat.getDefault();
 intFormat = NumberFormat.getIntegerInstance();
 decimalFormat = NumberFormat.getNumberInstance();
 decimalFormat.setMinimumFractionDigits(2);
 properFractionFormat = new FractionFormat(decimalFormat, decimalFormat, intFormat);
 }

 @Test
 public void testDefaultConstructor() {
 FractionFormat format = new FractionFormat();
 assertEquals("1/2", format.format(new Fraction(1, 2)));
 }

 @Test
 public void testCustomConstructor() {
 NumberFormat customFormat = NumberFormat.getNumberInstance();
 customFormat.setMinimumFractionDigits(1);
 FractionFormat format = new FractionFormat(customFormat, customFormat, customFormat);
 assertEquals("1/2", format.format(new Fraction(1, 2)));
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1 1/2";
 String result = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFormatNegativeWholeNumber() {
 Fraction fraction = new Fraction(-3, 2);
 String expected = "-3 1/2";
 String result = properFractionFormat.format(fraction, new StringBuffer(), null);
 assertEquals(expected, result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNegativeFraction() {
 Fraction fraction = new Fraction(-1, 2);
 String result = properFractionFormat.format(fraction, new StringBuffer(), null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParse() {
 String source = "-3 1/2";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(-3, 2);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(expected, fraction);
 assertEquals(0, pos.getIndex());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidWholeNumber() {
 String source = "3 a";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidNumerator() {
 String source = "3 2 a";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominator() {
 String source = "3 2 /";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidWholeAndNumerator() {
 String source = "3 a 2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidDenominatorMinus() {
 String source = "3 2 -";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseInvalidDenominatorZero() {
 String source = "3 2 0";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertNull(fraction);
 assertEquals(3, pos.getIndex());
 }

 @Test
 public void testParseInvalidWholeAndDenominator() {
 String source = "3 a 2";
 ParsePosition pos = new ParsePosition(0);Fraction fraction = properFractionFormat.parse(source, pos);
 assertNull(fraction);
 assertEquals(3, pos.getIndex());
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidWholeNegativeAndDenominator() {
 String source = "-3 a 2";
 ParsePosition pos = new ParsePosition(0);
 properFractionFormat.parse(source, pos);
 fail("Expected NumberFormatException");
 }
}