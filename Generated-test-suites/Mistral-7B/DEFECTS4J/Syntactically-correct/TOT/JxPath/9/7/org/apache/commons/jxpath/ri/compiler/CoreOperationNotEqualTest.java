import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.ArgType;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualTrue() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
 EvalContext context = new EvalContext();
 assertTrue(notEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqualFalse() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 assertTrue(notEqual.computeValue(context).equals(Boolean.FALSE));
 }

 @Test
 public void testNotEqualNullAndObject() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(null), new ConstantExpression("test"));
 EvalContext context = new EvalContext();
 assertTrue(notEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqualObjectAndNull() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression("test"), new ConstantExpression(null));
 EvalContext context = new EvalContext();
 assertTrue(notEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqualDifferentTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression("test"));
 EvalContext context = new EvalContext();
 assertTrue(notEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testNotEqualSameTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
 EvalContext context = new EvalContext();
 assertFalse(notEqual.computeValue(context).equals(Boolean.TRUE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqualWithInvalidTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(new CoreOperationAdd()));
 EvalContext context = new EvalContext();
 }

 @Test
 public void testNotEqualWithFunctions() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(Functions.ONE), new ConstantExpression(Functions.ONE));
 EvalContext context = new EvalContext();
 assertFalse(notEqual.computeValue(context).equals(Boolean.TRUE));

 CoreOperationNotEqual notEqual2 = new CoreOperationNotEqual(new ConstantExpression(Functions.ONE), new ConstantExpression(Functions.ZERO));
 EvalContext context2 = new EvalContext();
 assertTrue(notEqual2.computeValue(context2).equals(Boolean.TRUE));
 }
}