import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualSuccess() {
 InfoSet infoSet = new InfoSet();
 NodeInfo node1 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "1.5", null);
 InfoSetUtil.addNode(infoSet, node1);
 NodeInfo node2 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "2.5", null);
 InfoSetUtil.addNode(infoSet, node2);

 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(new Double(1.5)), new ConstantExpression(new Double(2.5)));
 EvalContext context = new EvalContext(infoSet);

 Boolean result = (Boolean) expr.computeValue(context);

 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanOrEqualFailure() {
 InfoSet infoSet = new InfoSet();
 NodeInfo node1 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "1.5", null);
 InfoSetUtil.addNode(infoSet, node1);
 NodeInfo node2 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "3.5", null);
 InfoSetUtil.addNode(infoSet, node2);

 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(new Double(1.5)), new ConstantExpression(new Double(3.5)));
 EvalContext context = new EvalContext(infoSet);

 Boolean result = (Boolean) expr.computeValue(context);

 assertFalse(result.booleanValue());
 }

 @Test
 public void testLessThanOrEqualNaN() {
 InfoSet infoSet = new InfoSet();
 NodeInfo node1 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "NaN", null);
 InfoSetUtil.addNode(infoSet, node1);
 NodeInfo node2 = new NodeInfo(new Object(), Path.NODE_TYPE_NUMBER, "2.5", null);
 InfoSetUtil.addNode(infoSet, node2);

 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(new Double("NaN")), new ConstantExpression(new Double(2.5)));
 EvalContext context = new EvalContext(infoSet);

 Boolean result = (Boolean) expr.computeValue(context);

 assertFalse(result.booleanValue());
 }

 @Test
 public void testLessThanOrEqualNull() {
 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(null), new ConstantExpression(new Double(2.5)));
 EvalContext context = new EvalContext(null);

 try {
 Boolean result = (Boolean) expr.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLessThanOrEqualNullArg1() {
 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(null), new ConstantExpression(new Double(2.5)));
 EvalContext context = new EvalContext(null);

 try {
 expr.setArgs(new Expression[] { null, new ConstantExpression(new Double(2.5)) });
 Boolean result = (Boolean) expr.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLessThanOrEqualNullArg2() {
 CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(new ConstantExpression(new Double(1.5)), null);
 EvalContext context = new EvalContext(null);

 try {
 expr.setArgs(new Expression[] { new ConstantExpression(new Double(1.5)), null });
 Boolean result = (Boolean) expr.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}