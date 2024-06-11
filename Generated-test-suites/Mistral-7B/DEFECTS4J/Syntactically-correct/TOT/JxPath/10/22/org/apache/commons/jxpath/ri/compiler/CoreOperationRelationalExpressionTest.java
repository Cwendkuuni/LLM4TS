import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.AxisName;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation.CoreOperationType;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestBase;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeFactory;
import org.apache.commons.jxpath.ri.model.NodeList;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest extends CoreOperationTestBase {

 private static final NodeList EMPTY_NODE_LIST = NodeFactory.newInstance().createNodeList();
 private static final Node TRUE_NODE = NodeFactory.newInstance().createBooleanNode(true);
 private static final Node FALSE_NODE = NodeFactory.newInstance().createBooleanNode(false);

 @Test
 public void testGreaterThan() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(3.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(10.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(2.0));
 result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGreaterThanOrEqual() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(5.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(4.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(3.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(2.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThan() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(3.0)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(2.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 context.setNode(new NodeFactory().createNumberNode(4.0));
 result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanOrEqual() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(3.0)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(3.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(4.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setNode(new NodeFactory().createNumberNode(5.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testIteratorCompare() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createVariableExpression("num1")),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createVariableExpression("num2"))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new IteratorNode(new IteratorNode[] {
 new IteratorNode(EMPTY_NODE_LIST),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(3.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(5.0))
 }));
 context.setVariable("num1", NodeFactory.newInstance().createNumberNode(3.0));
 context.setVariable("num2", NodeFactory.newInstance().createNumberNode(5.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 context.setVariable("num1", NodeFactory.newInstance().createNumberNode(3.0));
 context.setVariable("num2", NodeFactory.newInstance().createNumberNode(3.0));
 result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testIteratorContainsMatch() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createVariableExpression("num")),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new IteratorNode(new IteratorNode[] {
 new IteratorNode(NodeFactory.newInstance().createNumberNode(3.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(5.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(4.0))
 }));
 context.setVariable("num", NodeFactory.newInstance().createNumberNode(5.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setVariable("num", NodeFactory.newInstance().createNumberNode(4.0));
 result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testIteratorFindMatch() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createVariableExpression("num1")),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createVariableExpression("num2"))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new IteratorNode(new IteratorNode[] {
 new IteratorNode(NodeFactory.newInstance().createNumberNode(3.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(5.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(4.0)),
 new IteratorNode(NodeFactory.newInstance().createNumberNode(5.0))
 }));
 context.setVariable("num1", NodeFactory.newInstance().createNumberNode(3.0));
 context.setVariable("num2", NodeFactory.newInstance().createNumberNode(5.0));
 Object result = relationalExpression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 context.setVariable("num1", NodeFactory.newInstance().createNumberNode(3.0));
 context.setVariable("num2", NodeFactory.newInstance().createNumberNode(4.0));
 result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testNullComparison() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(null))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(5.0));
 try {
 Object result = relationalExpression.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 context.setNode(new NodeFactory().createNumberNode(null));
 try {
 Object result = relationalExpression.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNaNComparison() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(Double.NaN)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(Double.NaN))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(Double.NaN));
 Object result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testNaNvsNonNaNComparison() throws Exception {
 CoreOperation relationalExpression = new CoreOperationRelationalExpression(
 new CoreOperation[] {
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(Double.NaN)),
 new CoreOperation(CoreOperationType.NUMERIC, new ExpressionFactory().createLiteralExpression(5.0))
 });
 JXPathContext context = createContext(AxisName.SELF);
 context.setNode(new NodeFactory().createNumberNode(5.0));
 Object result = relationalExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

}