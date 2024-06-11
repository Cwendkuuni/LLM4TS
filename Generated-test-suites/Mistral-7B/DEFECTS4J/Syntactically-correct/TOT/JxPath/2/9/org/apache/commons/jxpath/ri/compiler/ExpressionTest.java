import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testContextDependentExpression() {
 expression = new ContextDependentExpression();
 assertTrue(expression.isContextDependent());
 }

 @Test
 public void testContextIndependentExpression() {
 expression = new ContextIndependentExpression();
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testContextDependentExpressionEvaluation() {
 expression = new ContextDependentExpression();
 EvalContext ctx = new EvalContext(new NodePointer("root"), null);
 Object result = expression.computeValue(ctx);
 assertNull(result);
 ctx.setCurrentNodePointer(new NodePointer("child"));
 result = expression.computeValue(ctx);
 assertTrue(result instanceof Integer);
 }

 @Test
 public void testContextIndependentExpressionEvaluation() {
 expression = new ContextIndependentExpression();
 EvalContext ctx = new EvalContext(new NodePointer("root"), null);
 Object result = expression.computeValue(ctx);
 assertTrue(result instanceof Integer);
 ctx.setCurrentNodePointer(new NodePointer("child"));
 Object result2 = expression.computeValue(ctx);
 assertTrue(result2.equals(result));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateContextDependentExpression() {
 expression = new ContextDependentExpression();
 Iterator iter = expression.iterate(new EvalContext(new NodePointer("root"), null));
 iter.next();
 }

 @Test
 public void testIterateContextIndependentExpression() {
 expression = new ContextIndependentExpression();
 Iterator iter = expression.iterate(new EvalContext(new NodePointer("root"), null));
 assertTrue(iter.hasNext());
 Object next = iter.next();
 assertTrue(next instanceof Integer);
 assertTrue(iter.hasNext());
 next = iter.next();
 assertTrue(next.equals(next));
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Integer computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getDepth();
 }
}

class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Integer computeValue(EvalContext context) {
 return new Integer(42);
 }
}