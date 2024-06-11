import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private NumberFormat defaultNumberFormat;
 private ProperFractionFormat properFractionFormat;

 @Before
 public void setUp() {
 defaultNumberFormat = NumberFormat.getDefaultInstance();
 properFractionFormat = new ProperFractionFormat(defaultNumberFormat, defaultNumberFormat, defaultNumberFormat);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(4, 5);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0, 0);
 String formatted = properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1 4/5", formatted.toString());

 fraction = new Fraction(1, 1);
 toAppendTo = new StringBuffer();
 formatted = properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("1", formatted.toString());

 fraction = new Fraction(-2, 3);
 toAppendTo = new StringBuffer();
 formatted = properFractionFormat.format(fraction, toAppendTo, pos);
 assertEquals("-2/3", formatted.toString());
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testFormatWithNegativeDenominator() {
 Fraction fraction = new Fraction(1, -2);
 StringBuffer toAppendTo = new StringBuffer();
 ProperFractionFormat.FormatPosition pos = new ProperFractionFormat.FormatPosition(0, 0, 0);
 properFractionFormat.format(fraction, toAppendTo, pos);
 }

 @Test
 public void testParse() {
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse("1 2/3", pos);
 assertEquals(new Fraction(1, 2), fraction);
 pos.setIndex(0);
 fraction = properFractionFormat.parse("1/2", pos);
 assertEquals(new Fraction(1, 2), fraction);
 pos.setIndex(0);
 fraction = properFractionFormat.parse("-1 1/2", pos);
 assertEquals(new Fraction(-1, 2), fraction);
 pos.setIndex(0);
 fraction = properFractionFormat.parse("1", pos);
 assertEquals(new Fraction(1, 1), fraction);

 pos.setIndex(0);
 try {
 fraction = properFractionFormat.parse("2/0", pos);
 fail();
 } catch (ParseException e) {
 // expected
 }

 pos.setIndex(0);
 try {
 fraction = properFractionFormat.parse("2/-3", pos);
 fail();
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithWholePart() {
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse("3 4/5", pos);
 assertEquals(new Fraction(3, 4, 5), fraction);
 pos.setIndex(0);
 fraction = properFractionFormat.parse("3", pos);
 assertEquals(new Fraction(3), fraction);
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 ProperFractionFormat newProperFractionFormat = new ProperFractionFormat(defaultNumberFormat, wholeFormat, defaultNumberFormat);
 assertEquals("1", newProperFractionFormat.getWholeFormat().format(1));
 }
}