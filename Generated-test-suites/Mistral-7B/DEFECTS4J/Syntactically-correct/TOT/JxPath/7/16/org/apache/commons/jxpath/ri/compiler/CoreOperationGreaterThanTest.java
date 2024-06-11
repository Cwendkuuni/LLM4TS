import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoConstant;
import org.apache.commons.jxpath.ri.NodeInfoNumber;
import org.apache.commons.jxpath.ri.nodes.NodeFactory;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanFloatPositive() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoConstant(1.5f, NodeFactory.createFloatNode("1.5")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoConstant(2.0f, NodeFactory.createFloatNode("2.0")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanFloatNegative() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoConstant(-1.5f, NodeFactory.createFloatNode("-1.5")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoConstant(-2.0f, NodeFactory.createFloatNode("-2.0")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testGreaterThanFloatZero() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoConstant(0.0f, NodeFactory.createFloatNode("0.0")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoConstant(1.0f, NodeFactory.createFloatNode("1.0")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanIntPositive() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoNumber(3, NodeFactory.createNumberNode("3")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoNumber(2, NodeFactory.createNumberNode("2")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanIntNegative() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoNumber(-3, NodeFactory.createNumberNode("-3")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoNumber(-2, NodeFactory.createNumberNode("-2")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testGreaterThanIntZero() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoNumber(0, NodeFactory.createNumberNode("0")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoNumber(1, NodeFactory.createNumberNode("1")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SimpleNode("root", NodeFactory.createNode("root")), infoSet);
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanMismatchedTypes() {
 Expression arg1 = new CoreConstantExpression(new NodeInfoConstant(1.5f, NodeFactory.createFloatNode("1.5")));
 Expression arg2 = new CoreConstantExpression(new NodeInfoNumber(2, NodeFactory.createNumberNode("2")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGreaterThanNullArg() {
 Expression arg1 = null;
 Expression arg2 = new CoreConstantExpression(new NodeInfoNumber(2, NodeFactory.createNumberNode("2")));
 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(arg1, arg2);
 }
}