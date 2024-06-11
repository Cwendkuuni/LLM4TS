import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualTrue() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(1), new LiteralExpression(2));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualFalse() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(1), new LiteralExpression(1));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = EvaluationException.class)
 public void testNotEqualTypeMismatch() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(1), new LiteralExpression("str"));
 EvalContext context = new EvalContext();
 notEqual.computeValue(context);
 fail("EvaluationException expected");
 }

 @Test
 public void testNotEqualNull() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(null), new LiteralExpression(1));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualNullNull() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(null), new LiteralExpression(null));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualNullFalse() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(null), new LiteralExpression(Boolean.FALSE));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqualFalseNull() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new LiteralExpression(Boolean.FALSE), new LiteralExpression(null));
 EvalContext context = new EvalContext();
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }
}