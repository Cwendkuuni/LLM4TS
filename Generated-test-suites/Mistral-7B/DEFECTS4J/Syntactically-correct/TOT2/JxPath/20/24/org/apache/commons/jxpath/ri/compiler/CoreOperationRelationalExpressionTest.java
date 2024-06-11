import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.GreaterThanExpression;
import org.apache.commons.jxpath.ri.compiler.ContainsExpression;
import org.apache.commons.jxpath.ri.compiler.IntersectionExpression;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.iter.IteratorExpression;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.apache.commons.jxpath.ri.Iter;
import org.apache.commons.jxpath.ri.IterConfig;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

   @Test
   public void testComputevalueWithTrueInput() {
       Expression left = new LiteralExpression(true);
       Expression right = new LiteralExpression(5);
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new ArrayExpression(new Expression[] { left, right }));
       EvalContext context = new MockEvalContext();
       assertEquals(Boolean.TRUE, expression.computeValue(context));
   }

   @Test
   public void testComputevalueWithFalseInput() {
       Expression left = new LiteralExpression(false);
       Expression right = new LiteralExpression(5);
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new ArrayExpression(new Expression[] { left, right }));
       EvalContext context = new MockEvalContext();
       assertEquals(Boolean.FALSE, expression.computeValue(context));
   }

   @Test
   public void testGetPrecedenceMethod() {
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new Expression[0]);
       assertEquals(RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
   }

   @Test
   public void testIsSymmetricMethod() {
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new Expression[0]);
       assertFalse(expression.isSymmetric());
   }

   @Test
   public void testComputeMethodWithDoubleValues() {
       Expression left = new LiteralExpression(3.14);
       Expression right = new LiteralExpression(3.14);
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new ArrayExpression(new Expression[] { left, right }));
       EvalContext context = new MockEvalContext();
       assertTrue(expression.compute(context.getInfoSet(), context));
   }

   @Test
   public void testComputeMethodWithDifferentDoubleValues() {
       Expression left = new LiteralExpression(3.14);
       Expression right = new LiteralExpression(2.718);
       CoreOperationRelationalExpression expression = new GreaterThanExpression(new ArrayExpression(new Expression[] { left, right }));
       EvalContext context = new MockEvalContext();
       assertFalse(expression.compute(context.getInfoSet(), context));
   }

   @Test
   public void testReduceMethodWithLiteralExpression() {
       Expression expression = new LiteralExpression("Hello");
       assertEquals("Hello", expression.reduce(null));
   }

   @Test
   public void testContainsMatchMethodWithEmptyIterator() {
       Iterator iterator = Arrays.asList(new String[]{}).iterator();
       CoreOperationRelationalExpression expression = new ContainsExpression(new ArrayExpression(new Expression[] { new LiteralExpression("world"), iterator }));
       assertFalse(expression.containsMatch(iterator, "world"));
   }

   @Test
   public void testContainsMatchMethodWithSingleMatch() {
       Iterator iterator = Arrays.asList(new String[]{"world"}).iterator();
       CoreOperationRelationalExpression expression = new ContainsExpression(new ArrayExpression(new Expression[] { new LiteralExpression("world"), iterator }));
       assertTrue(expression.containsMatch(iterator, "world"));
   }

   @Test
   public void testFindMatchMethodWithEmptyIterators() {
       Iterator leftIterator = Arrays.asList(new String[]{}).iterator();
       Iterator rightIterator = Arrays.asList(new String[]{}).iterator();
       CoreOperationRelationalExpression expression = new IntersectionExpression(new ArrayExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) }));
       assertFalse(expression.findMatch(leftIterator, rightIterator));
   }

   @Test
   public void testFindMatchMethodWithIntersectingValues() {
       Iterator leftIterator = Arrays.asList(new String[]{"apple", "banana", "cherry"}.iterator());
       Iterator rightIterator = Arrays.asList(new String[]{"cherry", "banana", "apple"}.iterator());
       CoreOperationRelationalExpression expression = new IntersectionExpression(new ArrayExpression(new Expression[] { new IteratorExpression(leftIterator), new IteratorExpression(rightIterator) }));
       assertTrue(expression.findMatch(leftIterator, rightIterator));
   }

}