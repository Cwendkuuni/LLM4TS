import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

 private static NumberFormat getDefaultNumberFormat() {
 return NumberFormat.getNumberInstance(Locale.US);
 }

 @Test
 public void testDefaultFormat() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test format
 Fraction fraction = new Fraction(1, 2);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("1 1/2", formatted.toString());

 Fraction parsed = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 parsed = format.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(fraction, parsed);

 // test parse negative whole number
 fraction = new Fraction(-1, 2);
 formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("-1 1/2", formatted.toString());
 parsed = null;
 try {
 parsed = format.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(fraction, parsed);

 // test parse big numbers
 NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
 numberFormat.setMaxIntegerDigits(100);
 ProperFractionFormat formatWithBigNumbers = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
 Fraction bigFraction = new Fraction(new BigInteger("12345678901234567890"), new BigInteger("98765432109876543210"));
 String bigFormatted = formatWithBigNumbers.format(bigFraction, new StringBuffer(), null);
 assertEquals("12345678901234567890 98765432109876543210", bigFormatted.toString());
 parsed = null;
 try {
 parsed = formatWithBigNumbers.parse(bigFormatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(bigFraction, parsed);
 }

 @Test
 public void testCustomFormat() {
 NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
 wholeFormat.setMinimumFractionDigits(2);
 NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.US);
 NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.US);
 ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

 // test format
 Fraction fraction = new Fraction(1, 2);
 String formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("1.00 1/2", formatted.toString());

 Fraction parsed = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 parsed = format.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(fraction, parsed);

 // test parse negative whole number
 fraction = new Fraction(-1, 2);
 formatted = format.format(fraction, new StringBuffer(), null);
 assertEquals("-1.00 1/2", formatted.toString());
 parsed = null;
 try {
 parsed = format.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(fraction, parsed);

 // test parse big numbers
 wholeFormat.setMaxIntegerDigits(100);
 numeratorFormat.setMaxIntegerDigits(100);
 denominatorFormat.setMaxIntegerDigits(100);
 ProperFractionFormat formatWithBigNumbers = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Fraction bigFraction = new Fraction(new BigDecimal("12345678901234567890.12345678901234567890"), new BigDecimal("98765432109876543210.98765432109876543210"));
 String bigFormatted = formatWithBigNumbers.format(bigFraction, new StringBuffer(), null);
 assertEquals("12345678901234567890.12 98765432109876543210.98765432109876543210", bigFormatted.toString());
 parsed = null;
 try {
 parsed = formatWithBigNumbers.parse(bigFormatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(bigFraction, parsed);

 // test parse invalid fraction
 String invalidFraction = "-3 -1/2";
 ParsePosition pos2 = new ParsePosition(0);
 try {
 format.parse(invalidFraction, pos2);
 fail("parse did not throw NumberFormatException for invalid fraction");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseRounding() {
 ProperFractionFormat format = new ProperFractionFormat();

 // test rounding mode RoundsUp
 NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
 numberFormat.setRoundingMode(RoundingMode.UP);
 ProperFractionFormat formatWithRoundingUp = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);

 Fraction fraction = new Fraction(1, 3);
 String formatted = formatWithRoundingUp.format(fraction, new StringBuffer(), null);
 assertEquals("1 1/3", formatted.toString());

 Fraction roundedFraction = null;
 ParsePosition pos = new ParsePosition(0);
 try {
 roundedFraction = formatWithRoundingUp.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(new Fraction(1, 3), roundedFraction);

 // test rounding mode RoundsDown
 NumberFormat numberFormatDown = NumberFormat.getNumberInstance(Locale.US);
 numberFormatDown.setRoundingMode(RoundingMode.DOWN);
 ProperFractionFormat formatWithRoundingDown = new ProperFractionFormat(numberFormatDown, numberFormatDown, numberFormatDown);

 Fraction fractionDown = new Fraction(1, 3);
 formatted = formatWithRoundingDown.format(fractionDown, new StringBuffer(), null);
 assertEquals("1 1/3", formatted.toString());

 roundedFraction = null;
 try {
 roundedFraction = formatWithRoundingDown.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(new Fraction(new BigInteger("1"), new BigInteger("3")).subtract(BigInteger.ONE), roundedFraction);

 // test rounding mode HalfUp
 NumberFormat numberFormatHalfUp = NumberFormat.getNumberInstance(Locale.US);
 numberFormatHalfUp.setRoundingMode(RoundingMode.HALF_UP);
 ProperFractionFormat formatWithRoundingHalfUp = new ProperFractionFormat(numberFormatHalfUp, numberFormatHalfUp, numberFormatHalfUp);

 fractionDown = new Fraction(1, 3);
 formatted = formatWithRoundingHalfUp.format(fractionDown, new StringBuffer(), null);
 assertEquals("1 1/3", formatted.toString());

 roundedFraction = null;
 try {
 roundedFraction = formatWithRoundingHalfUp.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(new Fraction(new BigInteger("1"), new BigInteger("3")), roundedFraction);

 fraction = new Fraction(new BigDecimal("1.5"), new BigDecimal("3"));
 formatted = formatWithRoundingHalfUp.format(fraction, new StringBuffer(), null);
 assertEquals("2 1/3", formatted.toString());

 roundedFraction = null;
 try {
 roundedFraction = formatWithRoundingHalfUp.parse(formatted, pos);
 } catch (NumberFormatException e) {
 fail("parse failed: " + e.getMessage());
 }
 assertEquals(new Fraction(new BigDecimal("2"), new BigDecimal("3")), roundedFraction);
 }
}