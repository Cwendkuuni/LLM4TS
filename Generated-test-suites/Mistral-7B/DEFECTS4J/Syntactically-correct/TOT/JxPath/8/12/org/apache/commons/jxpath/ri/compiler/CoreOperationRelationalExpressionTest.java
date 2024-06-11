import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiled.CompiledExpression;
import org.apache.commons.jxpath.ri.compiled.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeList;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 int a = 5, b = 3;
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(a)),
 new CoreNumberExpression(new ConstantExpression(b)),
 Axis.CHILD, ">", CoreOperationRelationalExpression.OP_GT);
 JXPathContext jxpathContext = new JXPathContext(new NodeList(), new Node());
 Object result = expr.compile(jxpathContext).evaluate(jxpathContext, null);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 int a = 5, b = 3;
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(a)),
 new CoreNumberExpression(new ConstantExpression(b)),
 Axis.CHILD, ">=", CoreOperationRelationalExpression.OP_GTE);
 JXPathContext jxpathContext = new JXPathContext(new NodeList(), new Node());
 Object result = expr.compile(jxpathContext).evaluate(jxpathContext, null);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan() {
 int a = 3, b = 5;
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(a)),
 new CoreNumberExpression(new ConstantExpression(b)),
 Axis.CHILD, "<", CoreOperationRelationalExpression.OP_LT);
 JXPathContext jxpathContext = new JXPathContext(new NodeList(), new Node());
 Object result = expr.compile(jxpathContext).evaluate(jxpathContext, null);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 int a = 3, b = 5;
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(a)),
 new CoreNumberExpression(new ConstantExpression(b)),
 Axis.CHILD, "<=", CoreOperationRelationalExpression.OP_LTE);
 JXPathContext jxpathContext = new JXPathContext(new NodeList(), new Node());
 Object result = expr.compile(jxpathContext).evaluate(jxpathContext, null);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLeftOperand() {
 CompiledExpression expr = new CoreOperationRelationalExpression(
 null, new CoreNumberExpression(new ConstantExpression(5)),
 Axis.CHILD, ">", CoreOperationRelationalExpression.OP_GT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRightOperand() {
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(5)),
 null,
 Axis.CHILD, ">", CoreOperationRelationalExpression.OP_GT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOperator() {
 CompiledExpression expr = new CoreOperationRelationalExpression(
 new CoreNumberExpression(new ConstantExpression(5)),
 new CoreNumberExpression(new ConstantExpression(5)),
 Axis.CHILD, "!=", CoreOperationRelationalExpression.OP_NE);
 }

}