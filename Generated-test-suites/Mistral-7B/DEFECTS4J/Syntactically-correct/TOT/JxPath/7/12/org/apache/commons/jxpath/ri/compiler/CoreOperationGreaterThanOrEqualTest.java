import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 private static final double EPSILON = 0.001;

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertTrue(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualTrue() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertTrue(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualFalse() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(4.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithInfAndNaN() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(Double.POSITIVE_INFINITY)),
 new ConstantExpression(new Double(Double.NaN)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithNegInfAndNaN() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(Double.NEGATIVE_INFINITY)),
 new ConstantExpression(new Double(Double.NaN)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithNegInfAndFiniteNumber() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(Double.NEGATIVE_INFINITY)),
 new ConstantExpression(new Double(3.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithFiniteNumberAndInf() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(3.0)), new ConstantExpression(new Double(Double.POSITIVE_INFINITY)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithFiniteNumberAndZero() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(0.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertTrue(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithZeroAndFiniteNumber() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(0.0)), new ConstantExpression(new Double(5.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithNegativeNumberAndPositiveNumber() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(-2.0)), new ConstantExpression(new Double(3.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertFalse(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithPositiveNumberAndNegativeNumber() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(3.0)), new ConstantExpression(new Double(-2.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertTrue(expression.computeValue(context).booleanValue());
 }

 @Test
 public void testGreaterThanOrEqualWithEqualNumbers() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
 InfoSet infoSet = TestHelper.createInfoSet();
 EvalContext context = new EvalContext(infoSet);
 assertTrue(expression.computeValue(context).booleanValue());
 }
}