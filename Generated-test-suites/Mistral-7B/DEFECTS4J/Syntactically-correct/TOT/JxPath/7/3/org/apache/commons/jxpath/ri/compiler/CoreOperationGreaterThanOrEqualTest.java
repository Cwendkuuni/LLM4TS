import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.DoubleNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 Expression arg1 = new DoubleNode(2.5);
 Expression arg2 = new DoubleNode(3.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSetUtil.createInfoSet();
 EvalContext context = new EvalContext(is);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThan() {
 Expression arg1 = new DoubleNode(1.5);
 Expression arg2 = new DoubleNode(3.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSetUtil.createInfoSet();
 EvalContext context = new EvalContext(is);

 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, operation.getSymbol());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArgument() {
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, new DoubleNode(3.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRightArgument() {
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new DoubleNode(3.0), null);
 }

 @Test
 public void testZeroAndZero() {
 Expression arg1 = new DoubleNode(0.0);
 Expression arg2 = new DoubleNode(0.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSetUtil.createInfoSet();
 EvalContext context = new EvalContext(is);

 Object result = operation.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testPositiveAndNegative() {
 Expression arg1 = new DoubleNode(-1.5);
 Expression arg2 = new DoubleNode(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSetUtil.createInfoSet();
 EvalContext context = new EvalContext(is);

 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNegativeAndNegative() {
 Expression arg1 = new DoubleNode(-3.0);
 Expression arg2 = new DoubleNode(-2.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 InfoSet is = InfoSetUtil.createInfoSet();
 EvalContext context = new EvalContext(is);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }
}