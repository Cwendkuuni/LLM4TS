import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat();
 }

 @Test
 public void testProperFractionFormatEmpty() {
 String input = "";
 String expectedOutput = "";
 String output = new StringBuffer().append(properFractionFormat.format(new Fraction(0, 1), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 assertNull(properFractionFormat.parse(input, new ParsePosition(0)));
 }

 @Test
 public void testProperFractionFormatWholeNumber() {
 String input = "1";
 String expectedOutput = "1";
 String output = new StringBuffer().append(properFractionFormat.format(new Fraction(1, 1), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 1), parsedFraction);
 }

 @Test
 public void testProperFractionFormatSimpleFraction() {
 String input = "1/2";
 String expectedOutput = "1 / 2";
 String output = new StringBuffer().append(properFractionFormat.format(new Fraction(1, 2), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 2), parsedFraction);
 }

 @Test
 public void testProperFractionFormatComplexFraction() {
 String input = "3/4 + 2/5";
 String expectedOutput = "3 / 4 + 2 / 5";
 String output = new StringBuffer().append(properFractionFormat.format(new Fraction("3/4+2/5", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = properFractionFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction("3/4+2/5", null), parsedFraction);
 }

 @Test(expected = NullArgumentException.class)
 public void testProperFractionFormatNullWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat(null, getNumberFormat(), getNumberFormat());
 try {
 format.parse("1", new ParsePosition(0));
 } catch (ParsePositionException e) {
 fail("Expected NullArgumentException, but got ParsePositionException");
 }
 ProperFractionFormat format1 = new ProperFractionFormat(properFractionFormat);
 String input = "1";
 String output = new StringBuffer().append(format1.format(new Fraction(1, 1), new StringBuffer(), null)).toString();
 assertEquals("1", output);
 Fraction parsedFraction = format1.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 1), parsedFraction);
 }

 @Test
 public void testProperFractionFormatSetWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.####");
 ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat, getNumberFormat(), getNumberFormat());
 String input = "1";
 String expectedOutputWholeFormat = "1,   ";
 String outputWholeFormat = new StringBuffer().append(formatWithWholeFormat.format(new Fraction(1, 1), new StringBuffer(), null)).toString();
 assertEquals(expectedOutputWholeFormat, outputWholeFormat);
 String input2 = "1/2";
 String expectedOutput = "1 / 2";
 String output = new StringBuffer().append(formatWithWholeFormat.format(new Fraction(1, 2), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction1 = formatWithWholeFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 2), parsedFraction1);
 }

 @Test
 public void testProperFractionFormatSetWholeFormatWithNonNumericValue() {
 ProperFractionFormat format = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.####");
 ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat, getNumberFormat(), getNumberFormat());
 String input = "1a";
 String expectedOutput = "1";
 String output = new StringBuffer().append(formatWithWholeFormat.format(new Fraction(1, 1), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = formatWithWholeFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 1), parsedFraction);
 }

 @Test
 public void testProperFractionFormatParseInvalidInput() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input1 = "1";
 ParsePosition pos1 = new ParsePosition(0);
 Fraction parsedFraction1 = format.parse(input1, pos1);
 assertEquals(new Fraction(1, 1), parsedFraction1);
 assertEquals(0, pos1.getIndex());
 String input2 = "1/0";
 ParsePosition pos2 = new ParsePosition(0);
 Fraction parsedFraction2 = format.parse(input2, pos2);
 assertNull(parsedFraction2);
 assertEquals(0, pos2.getIndex());
 String input3 = "1/-2";
 ParsePosition pos3 = new ParsePosition(0);
 Fraction parsedFraction3 = format.parse(input3, pos3);
 assertNull(parsedFraction3);
 assertEquals(0, pos3.getIndex());
 String input4 = "1/2+1";
 ParsePosition pos4 = new ParsePosition(0);
 Fraction parsedFraction4 = format.parse(input4, pos4);
 assertNull(parsedFraction4);
 assertEquals(0, pos4.getIndex());
 String input5 = "1/2/3";
 ParsePosition pos5 = new ParsePosition(0);
 Fraction parsedFraction5 = format.parse(input5, pos5);
 assertNull(parsedFraction5);
 assertEquals(0, pos5.getIndex());
 String input6 = "1/2/0";
 ParsePosition pos6 = new ParsePosition(0);
 Fraction parsedFraction6 = format.parse(input6, pos6);
 assertNull(parsedFraction6);
 assertEquals(0, pos6.getIndex());
 }

 @Test
 public void testProperFractionFormatParseComplexInput() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1 1/2 + 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction parsedFraction = format.parse(input, pos);
 assertEquals(new Fraction("1 1/2 + 2/3", null), parsedFraction);
 assertEquals(input.length(), pos.getIndex());
 }

 @Test
 public void testProperFractionFormatFormatAndParseRoundTrip() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1/3";
 String expectedOutput = "1 / 3";
 String output = new StringBuffer().append(format.format(new Fraction(1, 3), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 3), parsedFraction);
 input = "1 1/2 + 2/3";
 expectedOutput = "1 1 / 2 + 2 / 3";
 output = new StringBuffer().append(format.format(new Fraction("1 1/2 + 2/3", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction("1 1/2 + 2/3", null), parsedFraction);
 input = "1/-2";
 expectedOutput = "1 / -2";
 output = new StringBuffer().append(format.format(new Fraction(1, -2), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, -2), parsedFraction);
 input = "2/0";
 expectedOutput = "Error parsing rational number: Denominator may not be zero";
 output = null;
 parsedFraction = format.parse(input, new ParsePosition(0));
 assertNull(parsedFraction);
 }

 @Test
 public void testProperFractionFormatFormatAndParseComplexRoundTrip() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1 1/2 + 2/3";
 String expectedOutput = "1 1 / 2 + 2 / 3";
 String output = new StringBuffer().append(format.format(new Fraction("1 1/2 + 2/3", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction("1 1/2 + 2/3", null), parsedFraction);
 input = "1/-2 + 1/3";
 expectedOutput = "1 / -2 + 1 / 3";
 output = new StringBuffer().append(format.format(new Fraction("1/-2+1/3", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction("1/-2+1/3", null), parsedFraction);
 input = "1/0";
 expectedOutput = "Error parsing rational number: Denominator may not be zero";
 output = null;
 parsedFraction = format.parse(input, new ParsePosition(0));
 assertNull(parsedFraction);
 }

 @Test
 public void testProperFractionFormatFormatAndParseComplexRoundTripWithGivenPrecision() {
 ProperFractionFormat format = new ProperFractionFormat();
 String input = "1.4142135625/2";
 double expectedOutput = 0.7071067811865475244;
 String output = new StringBuffer().append(format.format(new Fraction(1, 2).add(new Fraction(1, 4).multiply(new BigRational("100000", "2"))), new StringBuffer(), null, new MathContext(10000, MathContext.UNLIMITED))).toString();
 assertEquals(String.format("%.15f", expectedOutput), output);
 Fraction parsedFraction = format.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 2).add(new Fraction(1, 4).multiply(new BigRational("100000", "2"))), parsedFraction);
 }

 @Test
 public void testProperFractionFormatFormatRoundTripWithCustomWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("#,##0.####");
 ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat, getNumberFormat(), getNumberFormat());
 String input = "1/3";
 String expectedOutputWholeFormat = "1,   ";
 String outputWholeFormat = new StringBuffer().append(formatWithWholeFormat.format(new Fraction(1, 3), new StringBuffer(), null)).toString();
 assertEquals(expectedOutputWholeFormat, outputWholeFormat);
 String expectedOutput = "1 / 3";
 String output = new StringBuffer().append(format.format(new Fraction(1, 3), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = formatWithWholeFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction(1, 3), parsedFraction);
 }

 @Test
 public void testProperFractionFormatFormatAndParseComplexRoundTripWithCustomWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 ProperFractionFormat formatWithWholeFormat = new ProperFractionFormat(wholeFormat, getNumberFormat(), getNumberFormat());
 String input = "1 1/2 + 2/3";
 String expectedOutputWholeFormat = "1,  1 / 2 + 2 / 3";
 String outputWholeFormat = new StringBuffer().append(formatWithWholeFormat.format(new Fraction("1 1/2 + 2/3", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutputWholeFormat, outputWholeFormat);
 String expectedOutput = "1 1 / 2 + 2 / 3";
 String output = new StringBuffer().append(format.format(new Fraction("1 1/2 + 2/3", null), new StringBuffer(), null)).toString();
 assertEquals(expectedOutput, output);
 Fraction parsedFraction = formatWithWholeFormat.parse(input, new ParsePosition(0));
 assertEquals(new Fraction("1 1/2 + 2/3", null), parsedFraction);
 }

 private NumberFormat getNumberFormat() {
 return new DecimalFormat("#.##########");
 }
}