import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.expressions.Constant;
import org.apache.commons.jxpath.ri.functions.FunctionCallExpression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationNotEqual notEqual;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = new Constant(1);
 arg2 = new Constant(2);
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 context = new EvalContext();
 }

 @Test
 public void testNotEqual_DifferentValues() {
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNotEqual_SameValues() {
 arg1 = arg2;
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual_NullValues() {
 arg1 = new Constant(null);
 arg2 = new Constant(null);
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 arg1 = new Constant(null);
 arg2 = new Constant(new Object());
 Object result2 = notEqual.computeValue(context);
 assertTrue(result2 instanceof Boolean);
 assertEquals(Boolean.TRUE, result2);

 arg1 = new Constant(new Object());
 arg2 = new Constant(null);
 Object result3 = notEqual.computeValue(context);
 assertTrue(result3 instanceof Boolean);
 assertEquals(Boolean.TRUE, result3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_InvalidArguments() {
 arg1 = new CoreOperationPlus(new Constant(1), new CoreOperationTimes(new Constant(2), new Constant(0)));
 arg2 = arg1;
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 notEqual.computeValue(context);
 fail("Should throw an IllegalArgumentException for self references");
 }

 @Test
 public void testNotEqual_FunctionCall() {
 FunctionCallExpression funcCall = new FunctionCallExpression("someFunction", arg1, arg2);
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 notEqual = new CoreOperationNotEqual(notEqual, funcCall);
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_InvalidExpressions() {
 arg1 = new CoreOperationPlus(new Constant(1), new CoreOperationTimes(new Constant(2), new Constant(0)));
 arg2 = new CoreOperationDivide(new Constant(1), new Constant(0));
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 fail("Should throw an IllegalArgumentException for invalid expressions");
 }

 @Test
 public void testNotEqual_Context() {
 arg1 = new Constant(1);
 arg2 = new Variable("x");
 context.set("x", 2);
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 Object result = notEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }
}