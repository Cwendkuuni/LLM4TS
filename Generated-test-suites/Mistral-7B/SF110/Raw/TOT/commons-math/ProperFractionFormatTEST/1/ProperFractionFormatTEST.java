import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;

 @Test
 public void testFormatAndParseBasic() {
 Fraction fraction = new Fraction(1, 2);
 String formatted = new StringBuffer().append(4).append(" ").append(format.format(fraction, new StringBuffer(), null)).toString();
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseWholeNumber() {
 Fraction fraction = new Fraction(3, 1);
 String formatted = new StringBuffer().append(3).append(" ").append(format.format(fraction, new StringBuffer(), null)).toString();
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseNegativeNumerator() {
 Fraction fraction = new Fraction(-1, 2);
 String formatted = new StringBuffer().append(-4).append(" ").append(format.format(fraction, new StringBuffer(), null)).toString();
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseWholeNumberInFront() {
 Fraction fraction = new Fraction(3, 2);
 String formatted = new StringBuffer().append(3).append(" ").append(format.format(fraction, new StringBuffer(), null)).toString();
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 assertEquals(new Fraction(3, 2), parsed);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatWithNullFormat() {
 ProperFractionFormat pff = new ProperFractionFormat();
 pff.setWholeFormat(null);
 }

 @Test
 public void testParseInvalidCharacter() {
 ProperFractionFormat pff = new ProperFractionFormat();
 String source = "1 / a";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = pff.parse(source, pos);
 assertEquals(null, fraction);
 assertFalse(pos.atEnd());
 assertEquals(2, pos.getIndex());
 }

 @Test
 public void testFormatAndParseLargeNumber() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance();
 wholeFormat.setMaximumFractionDigits(10);
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, null, null);
 Fraction fraction = new Fraction((long)Math.pow(10, 20), (long)Math.pow(10, 10));
 String formatted = new StringBuffer().append(1).append(" ").append(pff.format(fraction, new StringBuffer(), null)).toString();
 Fraction parsed = pff.parse(formatted, new ParsePosition(0));
 assertEquals(fraction, parsed);
 }

 @Test
 public void testFormatAndParseMixedNegativeFractions() {
 Fraction fraction1 = new Fraction(-1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction fraction3 = new Fraction(-2, 3);
 String formatted = new StringBuffer().append(-3).append(" ").append(format.format(fraction1, new StringBuffer(), null))
 .append(" ").append(format.format(fraction2, new StringBuffer(), null))
 .append(" ").append(format.format(fraction3, new StringBuffer(), null)).toString();
 Fraction parsed = format.parse(formatted, new ParsePosition(0));
 Fraction expected = new Fraction((-3 * fraction1.getDenominator() + fraction1.getNumerator()) * MathUtils.copySign(1, fraction1.getDenominator()), fraction1.getDenominator())
 .add(new Fraction(fraction2.getNumerator(), fraction2.getDenominator()))
 .add(new Fraction(-2 * fraction3.getDenominator() + fraction3.getNumerator(), fraction3.getDenominator()));
 assertEquals(expected, parsed);
 }

}