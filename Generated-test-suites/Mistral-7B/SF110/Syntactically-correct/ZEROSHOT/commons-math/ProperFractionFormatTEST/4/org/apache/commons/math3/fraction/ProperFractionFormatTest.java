import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 @Test
 public void testProperFractionFormatConstructor() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullConstructor() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(null);
 }

 @Test
 public void testProperFractionFormatDefaultConstruction() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 assertNotNull(properFractionFormat.getWholeFormat());
 assertNotNull(properFractionFormat.getNumeratorFormat());
 assertNotNull(properFractionFormat.getDenominatorFormat());
 }

 @Test
 public void testProperFractionFormatCustomConstruction() {
 NumberFormat wholeFormat = new DecimalFormat("#");
 NumberFormat numeratorFormat = new DecimalFormat("#");
 NumberFormat denominatorFormat = new DecimalFormat("#");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertSame(wholeFormat, properFractionFormat.getWholeFormat());
 assertSame(numeratorFormat, properFractionFormat.getNumeratorFormat());
 assertSame(denominatorFormat, properFractionFormat.getDenominatorFormat());
 }

 @Test
 public void testFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 Formatter formatter = new Formatter(toAppendTo, Locale.ENGLISH);
 properFractionFormat.format(fraction, formatter, new FieldPosition(0, 0));
 String formattedString = toAppendTo.toString();
 assertEquals("1 3/4", formattedString);

 fraction = new Fraction(1, 2);
 toAppendTo = new StringBuffer();
 formatter = new Formatter(toAppendTo, Locale.ENGLISH);
 properFractionFormat.format(fraction, formatter, new FieldPosition(0, 0));
 formattedString = toAppendTo.toString();
 assertEquals("1/2", formattedString);

 fraction = new Fraction(-3, 4);
 toAppendTo = new StringBuffer();
 formatter = new Formatter(toAppendTo, Locale.ENGLISH);
 try {
 properFractionFormat.format(fraction, formatter, new FieldPosition(0, 0));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 formattedString = toAppendTo.toString();
 assertEquals("", formattedString);
}

 @Test
 public void testParse() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 String source = "1 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), fraction);
 pos.setIndex(source.length());

 source = "1/2";
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(1, 2), fraction);
 pos.setIndex(source.length());

 source = "-3 2/4";
 try {
 fraction = properFractionFormat.parse(source, pos);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 pos.setIndex(source.length());

 source = "1 3/2";
 try {
 fraction = properFractionFormat.parse(source, pos);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 pos.setIndex(source.length());

 source = "1 0/4";
 try {
 fraction = properFractionFormat.parse(source, pos);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 pos.setIndex(source.length());

 source = "2 1/3";
 Fraction expectedFraction = new Fraction(2, 3);
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(expectedFraction, fraction);
 pos.setIndex(source.length());

 source = "1 4/2";
 expectedFraction = new Fraction(2, 1);
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(expectedFraction, fraction);
 pos.setIndex(source.length());

 source = "1 3/-2";
 try {
 fraction = properFractionFormat.parse(source, pos);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 pos.setIndex(source.length());
}

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 NumberFormat wholeFormat = new DecimalFormat("#");
 properFractionFormat.setWholeFormat(wholeFormat);
 assertSame(wholeFormat, properFractionFormat.getWholeFormat());
}

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setWholeFormat(null);
}
}