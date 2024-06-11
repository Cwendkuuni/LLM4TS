import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.ParseException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Precision;
import java.text.ParsePosition;
import java.text.NumberFormat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 wholeFormat = new DecimalFormat("#");
 numeratorFormat = new DecimalFormat("#");
 denominatorFormat = new DecimalFormat("#");
 properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 }

 @Test
 public void testFormatWithWholeNumber() {
 String expected = "2 ";
 String actual = new StringBuffer().append(properFractionFormat.format(new Fraction(2, 1), null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithProperFraction() {
 String expected = "1 3 / 4";
 String actual = new StringBuffer().append(properFractionFormat.format(new Fraction(1, 3), null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithImproperFraction() {
 String expected = "5 / 3";
 String actual = new StringBuffer().append(properFractionFormat.format(new Fraction(5, 3), null, null)).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithWholeNumber() {
 Fraction expected = new Fraction(2, 1);
 Fraction actual = properFractionFormat.parse("2", new ParsePosition(0, null));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithProperFraction() {
 Fraction expected = new Fraction(1, 3);
 Fraction actual = properFractionFormat.parse("1 3 / 4", new ParsePosition(0, null));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithImproperFraction() {
 Fraction expected = new Fraction(5, 3);
 Fraction actual = properFractionFormat.parse("5 / 3", new ParsePosition(0, null));
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseWithInvalidInput() {
 String invalidInput = "abc/123";
 properFractionFormat.parse(invalidInput, new ParsePosition(0, null));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidWholeNumber() {
 String invalidInput = "-1 3 / 4";
 ParsePosition pos = new ParsePosition(0, null);
 properFractionFormat.parse(invalidInput, pos);
 assertEquals(-1, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidNumerator() {
 String invalidInput = "1 a / 4";
 ParsePosition pos = new ParsePosition(0, null);
 properFractionFormat.parse(invalidInput, pos);
 assertEquals(-1, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidDenominator() {
 String invalidInput = "1 3 / -4";
 ParsePosition pos = new ParsePosition(0, null);
 properFractionFormat.parse(invalidInput, pos);
 assertEquals(-1, pos.getIndex());
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat newWholeFormat = new DecimalFormat("#,##0");
 ProperFractionFormat newProperFractionFormat = new ProperFractionFormat();
 newProperFractionFormat.setWholeFormat(newWholeFormat);
 String expected = "1, 3 ";
 String actual = new StringBuffer().append(newProperFractionFormat.format(new Fraction(1, 3), null, null)).toString();
 assertEquals(expected, actual);
 }
}