import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.text.ParsePosition;
import java.text.NumberFormat;
import java.math.BigInteger;

public class ProperFractionFormatTest {

 private ProperFractionFormat format;
 private NumberFormat wholeFormat, numeratorFormat, denominatorFormat;

 @Before
 public void setUp() {
 format = new ProperFractionFormat();
 wholeFormat = NumberFormatUtils.createNumberFormat("0");
 numeratorFormat = NumberFormatUtils.createNumberFormat("0.0");
 denominatorFormat = NumberFormatUtils.createNumberFormat("0");
 }

 @Test
 public void testDefaultConstructor() {
 format = new ProperFractionFormat();
 Assert.assertNotNull(format.getWholeFormat());
 Assert.assertEquals(FractionFormat.getDefaultNumberFormat(), format.getNumeratorFormat());
 Assert.assertEquals(FractionFormat.getDefaultNumberFormat(), format.getDenominatorFormat());
 }

 @Test
 public void testNumberFormatConstructor() {
 format = new ProperFractionFormat(wholeFormat);
 Assert.assertEquals(wholeFormat, format.getWholeFormat());
 Assert.assertEquals(FractionFormat.getDefaultNumberFormat(), format.getNumeratorFormat());
 Assert.assertEquals(FractionFormat.getDefaultNumberFormat(), format.getDenominatorFormat());
 }

 @Test
 public void testCustomFormatConstructor() {
 format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
 Assert.assertEquals(wholeFormat, format.getWholeFormat());
 Assert.assertEquals(numeratorFormat, format.getNumeratorFormat());
 Assert.assertEquals(denominatorFormat, format.getDenominatorFormat());
 }

 @Test
 public void testFormat() {
 Fraction fraction = new Fraction(3, 4);
 StringBuffer toAppendTo = new StringBuffer();
 format.format(fraction, toAppendTo, new FieldPosition(0, 0));
 String result = toAppendTo.toString();
 String expected = "1 3/4";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testParse() {
 String source = "1 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 Assert.assertEquals(new Fraction(3, 4), fraction);
 pos.setIndex(pos.getErrorIndex());
 String source2 = "1 3/-4";
 Fraction fraction2 = format.parse(source2, pos);
 Assert.assertNull(fraction2);
 }

 @Test
 public void testSetWholeFormat() {
 ProperFractionFormat format = new ProperFractionFormat();
 NumberFormat newWholeFormat = NumberFormatUtils.createNumberFormat("#,##0");
 format.setWholeFormat(newWholeFormat);
 String source = "1 3/4";
 ParsePosition pos = new ParsePosition(0);
 Fraction fraction = format.parse(source, pos);
 String result = format.format(fraction, new StringBuffer(), new FieldPosition(0, 0)).toString();
 String expected = "1, 3/4";
 Assert.assertEquals(expected, result);
 }
}