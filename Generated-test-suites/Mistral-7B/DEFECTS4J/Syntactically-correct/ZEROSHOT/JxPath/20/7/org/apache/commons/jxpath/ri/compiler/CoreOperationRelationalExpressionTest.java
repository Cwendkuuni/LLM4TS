import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 EvalContext context = new EvalContext(new Object[] { new Object[] { 1.1, 2.1, 3.1, 4.1 } });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 EvalContext context = new EvalContext(new Object[] { new Object[] { 1.1, 2.0, 3.1, 4.1 } });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 EvalContext context = new EvalContext(new Object[] { new Object[] { 1.1, 2.1, 3.1, 4.1 } });
 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
 EvalContext context = new EvalContext(new Object[] { new Object[] { 1.1, 2.0, 3.1, 4.1 } });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testGreaterThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 EvalContext context = new EvalContext(new Object[] { new Object[] { new Iterator[] { new CoreListIterator(new Object[] { 1.0, 2.0 }), null }, new Iterator[] { new CoreListIterator(new Object[] { 2.1, 3.1 }), null } } });
 Iterator left = context.get(0, 0);
 Iterator right = context.get(1, 0);
 assertTrue(expression.computeValue(context));
 assertFalse(left.hasNext());
 assertFalse(right.hasNext());
 }

 @Test
 public void testGreaterThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 EvalContext context = new EvalContext(new Object[] { new Object[] { new Iterator[] { new CoreListIterator(new Object[] { 1.0, 2.0 }), null }, new Iterator[] { new CoreListIterator(new Object[] { 2.0, 3.0 }), null } } });
 Iterator left = context.get(0, 0);
 Iterator right = context.get(1, 0);
 assertTrue(expression.computeValue(context));
 assertFalse(left.hasNext());
 assertFalse(right.hasNext());
 }

 @Test
 public void testLessThanExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 EvalContext context = new EvalContext(new Object[] { new Object[] { new Iterator[] { new CoreListIterator(new Object[] { 1.0, 2.0 }), null }, new Iterator[] { new CoreListIterator(new Object[] { 2.1, 3.0 }), null } } });
 Iterator left = context.get(0, 0);
 Iterator right = context.get(1, 0);
 assertFalse(expression.computeValue(context));
 assertFalse(left.hasNext());
 assertFalse(right.hasNext());
 }

 @Test
 public void testLessThanOrEqualExpressionWithIterators() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreVariableExpression("x"), new CoreVariableExpression("y"));
 EvalContext context = new EvalContext(new Object[] { new Object[] { new Iterator[] { new CoreListIterator(new Object[] { 1.0, 2.0 }), null }, new Iterator[] { new CoreListIterator(new Object[] { 2.1, 3.0 }), null } } });
 Iterator left = context.get(0, 0);
 Iterator right = context.get(1, 0);
 assertTrue(expression.computeValue(context));
 assertFalse(left.hasNext());
 assertFalse(right.hasNext());
 }

 @Test
 public void testGreaterThanExpressionWithInitialContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreInitialContextExpression(), new CoreInitialContextExpression());
 EvalContext context = new EvalContext(new Object[] { new Object[] { new InitialContext(), new InitialContext() } });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testGreaterThanOrEqualExpressionWithInitialContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreInitialContextExpression(), new CoreInitialContextExpression());
 EvalContext context = new EvalContext(new Object[] { new Object[] { new InitialContext(), new InitialContext() } });
 assertTrue(expression.computeValue(context));
 }

 @Test
 public void testLessThanExpressionWithInitialContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreInitialContextExpression(), new CoreInitialContextExpression());
 EvalContext context = new EvalContext(new Object[] { new Object[] { new InitialContext(), new InitialContext() } });
 assertFalse(expression.computeValue(context));
 }

 @Test
 public void testLessThanOrEqualExpressionWithInitialContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreInitialContextExpression(), new CoreInitialContextExpression());
 EvalContext context = new EvalContext(new Object[] { new Object[] { new InitialContext(), new InitialContext() } });
 assertTrue(expression.computeValue(context));
 }

}