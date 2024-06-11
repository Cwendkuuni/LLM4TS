import org.apache.commons.jxpath.ri.CompilerEnvironment;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.apache.commons.jxpath.ri.utils.ExpressionUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 @Test
 public void testCoreOperationEqual_EqualValues() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeInt());
 Expression arg2 = new Variable("arg2", CompilerEnvironment.getInstance().getTypeInt());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", 5);
 context.setVariable("arg2", 5);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testCoreOperationEqual_NotEqualValues() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeInt());
 Expression arg2 = new Variable("arg2", CompilerEnvironment.getInstance().getTypeInt());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", 5);
 context.setVariable("arg2", 6);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));
 }

 @Test
 public void testCoreOperationEqual_NullAndNull() {
 Expression arg1 = new Variable("arg1", CompilerExpressionType.TYPE_NULL.getType());
 Expression arg2 = new Variable("arg2", CompilerExpressionType.TYPE_NULL.getType());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testCoreOperationEqual_NullAndNotNull() {
 Expression arg1 = new Variable("arg1", CompilerExpressionType.TYPE_NULL.getType());
 Expression arg2 = new Variable("arg2", CompilerEnvironment.getInstance().getTypeInt());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", null);
 context.setVariable("arg2", 5);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));
 }

 @Test
 public void testCoreOperationEqual_NotNullAndNull() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeInt());
 Expression arg2 = new Variable("arg2", CompilerExpressionType.TYPE_NULL.getType());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", 5);
 context.setVariable("arg2", null);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.FALSE));
 }

 @Test(expected = RuntimeException.class)
 public void testCoreOperationEqual_InvalidTypes() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeString());
 Expression arg2 = new Variable("arg2", CompilerEnvironment.getInstance().getTypeInt());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 Object result = equal.computeValue(context);
 fail("Expected a RuntimeException, but got " + result);
 }

 @Test
 public void testCoreOperationEqual_VariableAndConstant() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeInt());
 Expression arg2 = ExpressionUtils.constant(5);
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", 5);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));
 }

 @Test
 public void testCoreOperationEqual_VariableAndVariable() {
 Expression arg1 = new Variable("arg1", CompilerEnvironment.getInstance().getTypeInt());
 Expression arg2 = new Variable("arg2", CompilerEnvironment.getInstance().getTypeInt());
 CoreOperationEqual equal = new CoreOperationEqual(arg1, arg2);

 EvalContext context = new EvalContext();
 context.setVariable("arg1", 5);
 context.setVariable("arg2", 5);
 Object result = equal.computeValue(context);
 assertTrue(result.equals(Boolean.TRUE));

 context.setVariable("arg1", 5);
 context.setVariable("arg2", 6);
 Object result2 = equal.computeValue(context);
 assertTrue(result2.equals(Boolean.FALSE));
 }
}