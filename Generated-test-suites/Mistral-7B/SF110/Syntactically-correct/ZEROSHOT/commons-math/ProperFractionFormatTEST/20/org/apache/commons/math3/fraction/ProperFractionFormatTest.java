import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat intFormat;
 private NumberFormat decimalFormat;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 intFormat = NumberFormat.getNumberInstance(Locale.US);
 decimalFormat = NumberFormat.getNumberInstance(Locale.US);
 decimalFormat.setMaximumFractionDigits(3);
 }

 @Test
 public void testDefaultConstructor() {
 ProperFractionFormat format = new ProperFractionFormat();
 assertNotNull(format.getWholeFormat());
 assertNotNull(format.getNumeratorFormat());
 assertNotNull(format.getDenominatorFormat());
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(new BigInteger("1234567890"), new BigInteger("987654321"));
 String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 String expected = "123.456.678.901 / 987.654.321.001";
 assertEquals(expected, result);

 fraction = new Fraction(new BigDecimal("1/2"), new BigDecimal("3/2"));
 result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 expected = "1/2";
 assertEquals(expected, result);
 }

 @Test
 public void testParse() {
 String source = "123 1/2 / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(new BigInteger("123"), new BigInteger("1")), fraction);
 pos.setIndex(pos.getIndex() + 1);
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(new BigDecimal("1/2"), new BigDecimal("2")), fraction);
 pos.setIndex(pos.getIndex() + 1);
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(new BigInteger("987"), new BigInteger("3")), fraction);
 pos.setIndex(pos.getIndex() + 1);
 fraction = properFractionFormat.parse(source, pos);
 assertEquals(new Fraction(new BigDecimal("3/4"), new BigDecimal("4")), fraction);
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullWholeNumber() {
 String source = "  / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 format.setWholeFormat(null);
 try {
 format.parse(source, pos);
 fail();
 } catch (NullArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullNumerator() {
 String source = "123  / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 format.setNumeratorFormat(null);
 try {
 format.parse(source, pos);
 fail();
 } catch (NullArgumentException e) {
 // expected
 }
 }

 @Test(expected = NullArgumentException.class)
 public void testParseNullDenominator() {
 String source = "123 1/2 /  / 3/4";
 ParsePosition pos = new ParsePosition(0);
 ProperFractionFormat format = new ProperFractionFormat();
 format.setDenominatorFormat(null);
 try {
 format.parse(source, pos);
 fail();
 } catch (NullArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNegativeWholeNumber() {
 String source = "-123 1/2 / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNegativeNumerator() {
 String source = "123 -1/2 / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNegativeDenominator() {
 String source = "123 1/2 / -987 3/4";
 ParsePosition pos = new ParsePosition(0);
 try {
 ProperFractionFormat format = new ProperFractionFormat();
 Fraction fraction = format.parse(source, pos);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetWholeFormat() {
 NumberFormat format = new DecimalFormat("#.###");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setWholeFormat(format);
 Fraction fraction = new Fraction(new BigDecimal("123.456"), new BigDecimal("1"));
 String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 String expected = "123.456";
 assertEquals(expected, result);
 }

 @Test
 public void testSetNumeratorFormat() {
 NumberFormat format = new DecimalFormat("#.###");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setNumeratorFormat(format);
 Fraction fraction = new Fraction(new BigDecimal("123.456"), new BigDecimal("1"));
 String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 String expected = "123.456";
 assertEquals(expected, result);
 }

 @Test
 public void testSetDenominatorFormat() {
 NumberFormat format = new DecimalFormat("#.###");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat();
 properFractionFormat.setDenominatorFormat(format);
 Fraction fraction = new Fraction(new BigDecimal("1"), new BigDecimal("123.456"));
 String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 String expected = "1 / 123.456";
 assertEquals(expected, result);
 }

 @Test
 public void testSetAllFormats() {
 NumberFormat wholeFormat = new DecimalFormat("#,##0");
 NumberFormat numeratorFormat = new DecimalFormat("#.###");
 NumberFormat denominatorFormat = new DecimalFormat("#.###");
 ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction fraction = new Fraction(new BigDecimal("1234567890.123456"), new BigDecimal("987654321.987654321"));
 String result = properFractionFormat.format(fraction, new StringBuffer(), null).toString();
 String expected = "1,234,567,890.123.456.678.901 / 987,654,321.987.654.321.001";
 assertEquals(expected, result);
 }

 @Test
 public void testParseWithBigInteger() {
 String source = "123 1/2 / 987 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 BigInteger whole = ((BigInteger) fraction.getWholeNumber()).abs();
 BigInteger numerator = (BigInteger) fraction.getNumerator();
 BigInteger denominator = (BigInteger) fraction.getDenominator();
 BigInteger expectedWhole = new BigInteger("123");
 BigInteger expectedNumerator = new BigInteger("1");
 BigInteger expectedDenominator = new BigInteger("2");
 assertEquals(expectedWhole, whole);
 assertEquals(expectedNumerator, numerator);
 assertEquals(expectedDenominator, denominator);
 }

 @Test
 public void testParseWithBigDecimal() {
 String source = "123.456 1/2 / 987.654.321 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = properFractionFormat.parse(source, pos);
 BigDecimal whole = (BigDecimal) fraction.getWholeNumber();
 BigDecimal numerator = (BigDecimal) fraction.getNumerator();
 BigDecimal denominator = (BigDecimal) fraction.getDenominator();
 BigDecimal expectedWhole = new BigDecimal("123.456");
 BigDecimal expectedNumerator = new BigDecimal("1");
 BigDecimal expectedDenominator = new BigDecimal("2");
 assertEquals(expectedWhole, Precision.round(whole, 3));
 assertEquals(expectedNumerator, numerator);
 assertEquals(expectedDenominator, denominator);
 }
}