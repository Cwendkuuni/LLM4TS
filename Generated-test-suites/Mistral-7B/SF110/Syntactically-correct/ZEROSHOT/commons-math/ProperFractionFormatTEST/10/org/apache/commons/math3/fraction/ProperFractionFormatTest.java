import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

private ProperFractionFormat getProperFractionFormat() {
return new ProperFractionFormat();
}

@Test
public void testDefaultConstructor() {
ProperFractionFormat format = getProperFractionFormat();
assertNotNull(format);
}

@Test
public void testConstructorWithFormat() {
NumberFormat wholeFormat = new DecimalFormat("#");
ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
assertNotNull(format);
assertEquals(wholeFormat, format.getWholeFormat());
}

@Test
public void testFormat() {
Fraction fraction = new Fraction(3, 4);
String expected = "1 3/4";
ProperFractionFormat format = getProperFractionFormat();
StringBuffer toAppendTo = new StringBuffer();
format.format(fraction, toAppendTo, new FieldPosition(0, 0));
assertEquals(expected, toAppendTo.toString());
}

@Test
public void testParse() {
ProperFractionFormat format = getProperFractionFormat();
String source = "1 3/4";
Fraction expected = new Fraction(1, 4);
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(source.length(), pos.getIndex());
}

@Test(expected = NullArgumentException.class)
public void testParseWithNullFormat() {
ProperFractionFormat format = getProperFractionFormat();
String source = "1 3/4";
ParsePosition pos = new ParsePosition(0);
format.setWholeFormat(null);
Fraction result = format.parse(source, pos);
fail("NullArgumentException expected");
}

@Test
public void testSetWholeFormat() {
ProperFractionFormat format = getProperFractionFormat();
NumberFormat wholeFormat = new DecimalFormat("#");
format.setWholeFormat(wholeFormat);
assertEquals(wholeFormat, format.getWholeFormat());
}

@Test
public void testFormatWithZeroFraction() {
Fraction fraction = new Fraction(0, 1);
String expected = "0";
ProperFractionFormat format = getProperFractionFormat();
StringBuffer toAppendTo = new StringBuffer();
format.format(fraction, toAppendTo, new FieldPosition(0, 0));
assertEquals(expected, toAppendTo.toString());
}

@Test
public void testFormatWithNegativeNumerator() {
Fraction fraction = new Fraction(-3, 4);
String expected = "3/4";
ProperFractionFormat format = getProperFractionFormat();
StringBuffer toAppendTo = new StringBuffer();
format.format(fraction, toAppendTo, new FieldPosition(0, 0));
assertEquals(expected, toAppendTo.toString());
}

@Test
public void testFormatWithNegativeDenominator() {
Fraction fraction = new Fraction(3, -4);
String expected = "-3/4";
ProperFractionFormat format = getProperFractionFormat();
StringBuffer toAppendTo = new StringBuffer();
format.format(fraction, toAppendTo, new FieldPosition(0, 0));
assertEquals(expected, toAppendTo.toString());
}

@Test
public void testParseWithWhitespace() {
ProperFractionFormat format = getProperFractionFormat();
String source = " 1 3 / 4 ";
Fraction expected = new Fraction(1, 4);
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(source.length(), pos.getIndex());
}

@Test
public void testParseWithWhitespaceAndSlash() {
ProperFractionFormat format = getProperFractionFormat();
String source = " 1 3 / 4 ";
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(source.length(), pos.getIndex());
}

@Test(expected = NumberFormatException.class)
public void testParseWithInvalidInput() {
ProperFractionFormat format = getProperFractionFormat();
String source = "abc";
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
fail("NumberFormatException expected");
}

@Test(expected = NumberFormatException.class)
public void testParseWithInvalidInputWithWhitespace() {
ProperFractionFormat format = getProperFractionFormat();
String source = " abc 1 3 / 4 ";
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
fail("NumberFormatException expected");
}

@Test
public void testParseWithNegativeWhole() {
ProperFractionFormat format = getProperFractionFormat();
String source = "-1 3/4";
Fraction expected = new Fraction(-1, 4);
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(-1, result.getNumerator().intValue());
assertEquals(4, result.getDenominator().intValue());
}

@Test
public void testParseWithNegativeNumeratorAndDenominator() {
ProperFractionFormat format = getProperFractionFormat();
String source = "-3/-4";
Fraction expected = new Fraction(3, 4);
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(3, result.getNumerator().intValue());
assertEquals(4, result.getDenominator().intValue());
}

@Test
public void testParseWithLargeFraction() {
ProperFractionFormat format = getProperFractionFormat();
String source = "1234567890 1234567890 / 1234567890";
Fraction expected = new Fraction(1234567890, 1234567890);
ParsePosition pos = new ParsePosition(0);
Fraction result = format.parse(source, pos);
assertEquals(expected, result);
assertEquals(source.length(), pos.getIndex());
}

@Test
public void testFormatWithZeroDenominator() {
try {
ProperFractionFormat format = getProperFractionFormat();
Fraction fraction = new Fraction(0, 0);
StringBuffer toAppendTo = new StringBuffer();
format.format(fraction, toAppendTo, new FieldPosition(0, 0));
fail("ArithmeticException expected");
} catch (ArithmeticException e) {
// Expected
}
}
}