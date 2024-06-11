import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @Test
 public void testComputeValue() {
 Expression left = new LiteralExpression(new Integer(5));
 Expression right = new LiteralExpression(new Integer(10));
 expression = new GtExpression(new CoreOperationRelationalExpression[]{left, right});

 EvalContext context = new EvalContext();

 Object result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 left = new LiteralExpression(new Integer(10));
 right = new LiteralExpression(new Integer(5));
 expression = new GtExpression(new CoreOperationRelationalExpression[]{left, right});

 result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGetPrecedence() {
 expression = new GtExpression(new CoreOperationRelationalExpression[]{new LiteralExpression(1), new LiteralExpression(2)});
 assertEquals(3, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 expression = new GtExpression(new CoreOperationRelationalExpression[]{new LiteralExpression(1), new LiteralExpression(2)});
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 expression = new GtExpression(new CoreOperationRelationalExpression[]{new LiteralExpression(1), new LiteralExpression(2)});
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testComputeWithIterators() {
 InitialContext leftContext = new InitialContext();
 leftContext.addItem(new LiteralExpression(5.0));
 leftContext.addItem(new LiteralExpression(10.0));
 leftContext.addItem(new LiteralExpression(15.0));

 InitialContext rightContext = new InitialContext();
 rightContext.addItem(new LiteralExpression(10.0));
 rightContext.addItem(new LiteralExpression(5.0));
 rightContext.addItem(new LiteralExpression(3.0));

 expression = new GtExpression(new CoreOperationRelationalExpression[]{new IteratorExpression(new CoreExpression[]{new VariableExpression("ctx")}, leftContext), new IteratorExpression(new CoreExpression[]{new VariableExpression("ctx")}, rightContext)});

 EvalContext context = new EvalContext();
 context.setContext(leftContext);
 context.setContext(rightContext);

 Object result = expression.computeValue(context);

 assertFalse(result instanceof Boolean);

 context.setVariable("ctx", leftContext.getItem(0));
 result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 context.setVariable("ctx", leftContext.getItem(1));
 result = expression.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testContainsMatch() {
 Iterator leftIter = new java.util.ArrayList(3)
 .iterator();
 leftIter.add(new LiteralExpression(5.0));
 leftIter.add(new LiteralExpression(10.0));
 leftIter.add(new LiteralExpression(15.0));

 Object right = new LiteralExpression(10.0);

 boolean result = expression.containsMatch(leftIter, right);

 assertFalse(result);

 leftIter.next();
 result = expression.containsMatch(leftIter, right);

 assertTrue(result);
 }

 @Test
 public void testFindMatch() {
 Iterator leftIter = new java.util.ArrayList(3)
 .iterator();
 leftIter.add(new LiteralExpression(5.0));
 leftIter.add(new LiteralExpression(10.0));
 leftIter.add(new LiteralExpression(15.0));

 Iterator rightIter = new java.util.ArrayList(3)
 .iterator();
 rightIter.add(new LiteralExpression(5.0));
 rightIter.add(new LiteralExpression(10.0));
 rightIter.add(new LiteralExpression(3.0));

 HashSet leftSet = new HashSet();
 while (leftIter.hasNext()) {
 leftSet.add(leftIter.next());
 }

 Object result = expression.findMatch(leftIter, rightIter);

 assertFalse(result instanceof Boolean);

 leftIter.next();
 result = expression.findMatch(leftIter, rightIter);

 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

}