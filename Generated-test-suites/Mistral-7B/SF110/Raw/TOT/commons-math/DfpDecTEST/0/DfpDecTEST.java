import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

 private final DfpFieldFactory factory = new DfpField(64); // Set radix digits to 64

 @Test
 public void testConstructorWithDouble() {
 DfpDec dfpDec = new DfpDec(factory, 123.45);
 assertEquals("123.45", dfpDec.toString());
 }

 @Test
 public void testConstructorWithDfp() {
 DfpDec dfpDec1 = new DfpDec(factory, new DfpDec(factory, 123.45));
 DfpDec dfpDec2 = new DfpDec(factory, dfpDec1);
 assertEquals(dfpDec1, dfpDec2);
 }

 @Test
 public void testRoundingWithDefaultMode() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(0);
 assertEquals(0, roundResult);
 Dfp expectedResult = new DfpDec(factory, "1.5000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundUp() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_UP);
 Dfp expectedResult = new DfpDec(factory, "2.0000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundDown() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_DOWN);
 Dfp expectedResult = new DfpDec(factory, "1.0000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundHalfUp() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_HALF_UP);
 Dfp expectedResult = new DfpDec(factory, "1.5000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundHalfDown() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_HALF_DOWN);
 Dfp expectedResult = new DfpDec(factory, "1.0000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundHalfEven() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_HALF_EVEN);
 Dfp expectedResult = new DfpDec(factory, "1.5000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithRoundHalfOdd() {
 DfpDec dfpDec = new DfpDec(factory, 1.5);
 int roundResult = dfpDec.round(Dfp.ROUND_HALF_ODD);
 Dfp expectedResult = new DfpDec(factory, "2.0000");
 assertEquals(expectedResult, dfpDec);
 }

 @Test
 public void testRoundingWithCustomDigits() {
 DfpDec dfpDec = new DfpDec(factory, 12345.6789);
 int roundResult = dfpDec.round(2);
 Dfp expectedResult = new DfpDec(factory, "12345.68");
 assertEquals(expectedResult, dfpDec);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidInput() {
 new DfpDec(factory, new byte[(int)Short.MAX_VALUE + 1]);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeRadixDigits() {
 DfpDec dfpDec = new DfpDec(factory, -1);
 }
}