import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 Expression left = new LiteralExpression(1.5);
 Expression right = new LiteralExpression(2.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(null, null, null));
 EvalContext context = new EvalContext(infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLeftLessThanRight() {
 Expression left = new LiteralExpression(1.5);
 Expression right = new LiteralExpression(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(null, null, null));
 EvalContext context = new EvalContext(infoSet);
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) operation.getSymbol().toString().equals(result.toString()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLeftOperand() {
 Expression left = null;
 Expression right = new LiteralExpression(2.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRightOperand() {
 Expression left = new LiteralExpression(1.5);
 Expression right = null;
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 }

 @Test(expected = ClassCastException.class)
 public void testLeftOperandNotNumber() {
 Expression left = new LiteralExpression("abc");
 Expression right = new LiteralExpression(2.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(null, null, null));
 EvalContext context = new EvalContext(infoSet);
 Object result = operation.computeValue(context);
 fail("Expected ClassCastException, but got " + result);
 }

 @Test(expected = ClassCastException.class)
 public void testRightOperandNotNumber() {
 Expression left = new LiteralExpression(1.5);
 Expression right = new LiteralExpression("abc");
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(left, right);
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeInfo(null, null, null));
 EvalContext context = new EvalContext(infoSet);
 Object result = operation.computeValue(context);
 fail("Expected ClassCastException, but got " + result);
 }
}