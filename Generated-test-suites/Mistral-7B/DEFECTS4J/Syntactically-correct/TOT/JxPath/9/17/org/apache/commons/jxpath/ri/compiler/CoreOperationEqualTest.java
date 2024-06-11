import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 @Test
 public void testEqualTrue() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(1);
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression(1);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testEqualFalse() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(1);
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression(2);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testEqualNull() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(null);
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression(null);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 assertTrue(operation.computeValue(context).equals(Boolean.TRUE));
 }

 @Test
 public void testEqualDifferentTypes() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(1);
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression("1");
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 assertFalse(operation.computeValue(context).equals(Boolean.TRUE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualNullArg1() {
 Expression arg1 = null;
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression(1);
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualNullArg2() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(1);
 Expression arg2 = null;
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualDifferentTypesArg1() {
 Expression arg1 = CoreOperationEqual_Test.createConstantExpression(1);
 Expression arg2 = CoreOperationEqual_Test.createConstantExpression('a');
 CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
 }
}