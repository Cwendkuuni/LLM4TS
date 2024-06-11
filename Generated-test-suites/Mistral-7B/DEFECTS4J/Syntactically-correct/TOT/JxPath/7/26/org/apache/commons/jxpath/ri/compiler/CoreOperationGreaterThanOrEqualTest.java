import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 private Expression arg1, arg2;
 private CoreOperationGreaterThanOrEqual expression;
 private EvalContext context;

 @Test
 public void testGreaterThanOrEqual() {
 arg1 = new SimpleNode(1.5);
 arg2 = new SimpleNode(2.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.TRUE, expression.computeValue(context));
 }

 @Test
 public void testLessThan() {
 arg1 = new SimpleNode(1.5);
 arg2 = new SimpleNode(2.5);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.FALSE, expression.computeValue(context));
 }

 @Test
 public void testEqual() {
 arg1 = new SimpleNode(2.0);
 arg2 = new SimpleNode(2.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.TRUE, expression.computeValue(context));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArgNull() {
 arg1 = null;
 arg2 = new SimpleNode(2.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArg2Null() {
 arg1 = new SimpleNode(2.0);
 arg2 = null;
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test
 public void testInvalidArgsType() {
 arg1 = new SimpleNode(1);
 arg2 = new SimpleNode("string");
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test
 public void testNaN() {
 arg1 = new SimpleNode(Double.NaN);
 arg2 = new SimpleNode(Double.NaN);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.FALSE, expression.computeValue(context));
 }

 @Test
 public void testPositiveAndNegative() {
 arg1 = new SimpleNode(Double.POSITIVE_INFINITY);
 arg2 = new SimpleNode(-1.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.TRUE, expression.computeValue(context));
 }

 @Test
 public void testNegativeAndNegative() {
 arg1 = new SimpleNode(-2.0);
 arg2 = new SimpleNode(-1.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.TRUE, expression.computeValue(context));
 }

 @Test
 public void testZeroAndNegative() {
 arg1 = new SimpleNode(0.0);
 arg2 = new SimpleNode(-1.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.FALSE, expression.computeValue(context));
 }

 @Test
 public void testZeroAndZero() {
 arg1 = new SimpleNode(0.0);
 arg2 = new SimpleNode(0.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.TRUE, expression.computeValue(context));
 }

 @Test
 public void testZeroAndPositive() {
 arg1 = new SimpleNode(0.0);
 arg2 = new SimpleNode(1.0);
 context = new EvalContext();

 expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertEquals(Boolean.FALSE, expression.computeValue(context));
 }
}