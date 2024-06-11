import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;
import org.junit.Assert.*;

public class ProperFractionFormatTest {

 private ProperFractionFormat properFractionFormat;
 private NumberFormat wholeFormat;
 private NumberFormat numeratorFormat;
 private NumberFormat denominatorFormat;

 @Before
 public void setUp() {
 properFractionFormat = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat();
 numeratorFormat = NumberFormatUtils.createNumberFormat();
 denominatorFormat = NumberFormatUtils.createNumberFormat();
 }

 @Test
 public void testConstructor1() {
 ProperFractionFormat pff = new ProperFractionFormat();
 assertNotNull(pff);
 }

 @Test
 public void testConstructor2() {
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat);
 assertNotNull(pff);
 }

 @Test
 public void testConstructor3() {
 ProperFractionFormat pff = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 assertNotNull(pff);
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(1, 2);
 StringBuffer sb = new StringBuffer();
 String formatted = properFractionFormat.format(fraction, sb, null);
 Assert.assertEquals("1/2", formatted.toString());
 }

 @Test
 public void testGetWholeFormat() {
 NumberFormat actualWholeFormat = properFractionFormat.getWholeFormat();
 Assert.assertSame(wholeFormat, actualWholeFormat);
 }

 @Test(expected = NullArgumentException.class)
 public void testSetWholeFormatNull() {
 ProperFractionFormat pff = new ProperFractionFormat();
 try {
 pff.setWholeFormat(null);
 fail("NullArgumentException not thrown");
 } catch (NullArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParse() {
 String source = "3 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 2);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertEquals(expected, actual);
 pos.next(); // next token is a whitespace
 Assert.assertEquals(3, pos.getIndex());
 }

 @Test
 public void testParseInvalidWholeNumber() {
 String source = "a 2/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseInvalidNumerator() {
 String source = "3 a/3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseInvalidDenominator() {
 String source = "3 2/-3";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseInvalidFormat() {
 String source = "3 2/3.5";
 ParsePosition pos = new ParsePosition(0);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertNull(actual);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseMultipleSpaces() {
 String source = "3 2/3 ";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 2);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertEquals(expected, actual);
 pos.next(); // next token is a whitespace
 pos.next(); // next token is a whitespace
 Assert.assertEquals(3, pos.getIndex());
 }

 @Test
 public void testParseMultipleWhitespacesAndSlashes() {
 String source = "3 2 / 3";
 ParsePosition pos = new ParsePosition(0);
 Fraction expected = new Fraction(3, 2);
 Fraction actual = properFractionFormat.parse(source, pos);
 assertEquals(expected, actual);
 pos.next(); // next token is a whitespace
 pos.next(); // next token is a slash
 Assert.assertEquals(3, pos.getIndex());
 }
}