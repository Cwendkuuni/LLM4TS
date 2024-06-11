import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 format = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat();
 numeratorFormat = NumberFormatUtils.createNumberFormat();
 denominatorFormat = NumberFormatUtils.createNumberFormat();
 }

 @Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 assertNotNull(format.getWholeFormat());
 assertNotNull(format.getNumeratorFormat());
 assertNotNull(format.getDenominatorFormat());
 }

 @Test
 public void testConstructor() {
 format = new ProperFractionFormat(numeratorFormat, denominatorFormat, wholeFormat);
 assertSame(numeratorFormat, format.getNumeratorFormat());
 assertSame(denominatorFormat, format.getDenominatorFormat());
 assertSame(wholeFormat, format.getWholeFormat());
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 String result = toAppendTo.toString();
 assertEquals("3 / 4", result);

 fraction = new Fraction(5, 2);
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 result = toAppendTo.toString();
 assertEquals("5 / 2", result);

 fraction = new Fraction(1, 1);
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 result = toAppendTo.toString();
 assertEquals("1", result);

 fraction = new Fraction(-5, 2);
 toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, null);
 result = toAppendTo.toString();
 assertEquals("-5 / 2", result);
 }

 @Test
 public void testParse() {
 String source = "3";
 Fraction fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(3, 1), fraction);

 source = "3 / 4";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(3, 4), fraction);

 source = "3 / 2";
 try {
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 fail();
 } catch (ParseException e) {
 // expected
 }

 source = "-3 / 4";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(-3, 4), fraction);

 source = "17 / 3";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(17, 3), fraction);

 source = "1 / 1";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(1, 1), fraction);

 source = "0 / 1";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertNull(fraction);

 source = "1 / 0";
 try {
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 fail();
 } catch (MathIllegalArgumentException e) {
 // expected
 }

 source = "1 / -1";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(1, -1), fraction);

 source = "1 / 2 + 1 / 2";
 try {
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 fail();
 } catch (ParseException e) {
 // expected
 }

 source = "1 / (2 + 3)";
 try {
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 fail();
 } catch (ParseException e) {
 // expected
 }

 source = "3 / (2 + 3)";
 fraction = format.parse(source, new ParsePosition(0, source.length()));
 assertEquals(new Fraction(3, 5), fraction);
 }

 @Test
 public void testSetWholeFormat() {
 format.setWholeFormat(wholeFormat);
 assertSame(wholeFormat, format.getWholeFormat());
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 format.setWholeFormat(null);
 fail();
 }

 @Test
 public void testToString() {
 format.setWholeFormat(NumberFormatUtils.createPercentFormat());
 format.setNumeratorFormat(NumberFormatUtils.createNumberFormat());
 format.setDenominatorFormat(NumberFormatUtils.createNumberFormat());
 assertEquals("ProperFractionFormat[wholeFormat=PercentFormat, numeratorFormat=NumberFormat, denominatorFormat=NumberFormat]", format.toString());
 }

}