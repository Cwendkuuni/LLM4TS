import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {

 private DfpField getField() {
 return new Dfp128Field();
 }

 @Test
 public void testConstructorWithFactory() {
 DfpDec dfpDec = new DfpDec(getField());
 Assert.assertEquals("Test for default constructor", 0.0, dfpDec.doubleValue(), 0.0);
 }

 @Test
 public void testConstructorWithFactoryAndDouble() {
 DfpDec dfpDec = new DfpDec(getField(), 5.123);
 Assert.assertEquals("Test for constructor with double", 5.123, dfpDec.doubleValue(), 0.001);
 }

 @Test
 public void testNewInstance() {
 DfpDec dfpDec = new DfpDec(getField(), new DfpDec(getField(), 5.123));
 Assert.assertEquals("Test for newInstance method", 5.123, dfpDec.doubleValue(), 0.001);
 }

 @Test
 public void testRoundMethod() {
 DfpDec dfpDec = new DfpDec(getField(), 4.56789);
 int result = dfpDec.round(Dfp.ROUND_UP);
 Assert.assertEquals("Test for round method with up rounding", 5, result);

 DfpDec dfpDec2 = new DfpDec(getField(), 4.56789);
 int result2 = dfpDec2.round(Dfp.ROUND_DOWN);
 Assert.assertEquals("Test for round method with down rounding", 4, result2);
 }

 @Test
 public void testNextAfterMethod() {
 DfpDec x = new DfpDec(getField(), 0.123456);
 DfpDec y = new DfpDec(getField(), 0.2);
 DfpDec result = x.nextAfter(y);
 Assert.assertEquals("Test for nextAfter method", 0.1235, result.doubleValue(), 0.001);
 }

 @Test
 public void testAddSubtractMethods() {
 DfpDec a = new DfpDec(getField(), 3.14159);
 DfpDec b = new DfpDec(getField(), 2.71828);
 DfpDec sum = a.add(b);
 Assert.assertEquals("Test for add method", 5.85987, sum.doubleValue(), 0.001);

 DfpDec diff = a.subtract(b);
 Assert.assertEquals("Test for subtract method", 0.42131, diff.doubleValue(), 0.001);
 }

 @Test
 public void testMultiplyDivideMethods() {
 DfpDec a = new DfpDec(getField(), 3.14159);
 DfpDec b = new DfpDec(getField(), 2.71828);
 DfpDec product = a.multiply(b);
 Assert.assertEquals("Test for multiply method", 8.688888888888888, product.doubleValue(), 0.001);

 DfpDec quotient = a.divide(b);
 Assert.assertEquals("Test for divide method", 1.192646849264685, quotient.doubleValue(), 0.001);
 }

 @Test
 public void testPowerMethod() {
 DfpDec base = new DfpDec(getField(), 2.0);
 DfpDec exponent = new DfpDec(getField(), 3.0);
 DfpDec result = base.power(exponent);
 Assert.assertEquals("Test for power method", 8.0, result.doubleValue(), 0.0);
 }
}