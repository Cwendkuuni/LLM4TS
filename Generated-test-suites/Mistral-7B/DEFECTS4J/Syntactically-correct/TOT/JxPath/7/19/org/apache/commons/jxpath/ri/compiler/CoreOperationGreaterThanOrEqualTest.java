import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqualWithBothNumbersLessThan() {
 Expression arg1 = new ConstantExpression(1.0);
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 Object result = expression.computeValue(context);
 assertFalse("1.0 >= 2.0 should be false", result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanOrEqualWithBothNumbersEqual() {
 Expression arg1 = new ConstantExpression(2.0);
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 Object result = expression.computeValue(context);
 assertTrue("2.0 >= 2.0 should be true", result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanOrEqualWithFirstNumberGreaterThan() {
 Expression arg1 = new ConstantExpression(3.0);
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 Object result = expression.computeValue(context);
 assertTrue("3.0 >= 2.0 should be true", result.equals(Boolean.TRUE));
 }

 @Test(expected = ClassCastException.class)
 public void testGreaterThanOrEqualWithInvalidArgumentType() {
 Expression arg1 = new StringConstantExpression("two");
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 expression.computeValue(context);
 fail("Argument types mismatch should cause ClassCastException");
 }

 @Test
 public void testGreaterThanOrEqualWithNaN() {
 Expression arg1 = new ConstantExpression(Double.NaN);
 Expression arg2 = new ConstantExpression(2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 Object result = expression.computeValue(context);
 assertFalse("NaN >= 2.0 should be false", result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanOrEqualWithNegativeNumbers() {
 Expression arg1 = new ConstantExpression(-1.0);
 Expression arg2 = new ConstantExpression(-2.0);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 JXPathContext context = new JXPathContext(new InfoSet(), new EvalContext());
 Object result = expression.computeValue(context);
 assertTrue("-1.0 >= -2.0 should be true", result.equals(Boolean.TRUE));
 }

 @Test
 public void testGreaterThanOrEqualWithNullContext() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(2.0), new ConstantExpression(2.0));
 JXPathContext context = new JXPathContext(null, new EvalContext());
 Object result = expression.computeValue(context);
 assertNull("computeValue should return null when context is null", result);
 }
}