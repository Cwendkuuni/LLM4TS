import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.ConstantExpression;
import org.apache.commons.jxpath.ri.compiler.VariableExpression;

public class CoreOperationEqualTest {

 @Test
 public void testCoreOperationEqualConstructor1() {
 Expression arg1 = new ConstantExpression(1);
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testCoreOperationEqualConstructor2() {
 Expression arg1 = new VariableExpression("x");
 Expression arg2 = new VariableExpression("y");
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueEqual() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = new ConstantExpression(1);
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test (expected = ClassCastException.class)
 public void testComputeValueDifferentTypes() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = new ConstantExpression("Hello");
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 coreOperationEqual.computeValue(context);
 }

 @Test (expected = NullPointerException.class)
 public void testComputeValueNullArgs() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = null;
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 coreOperationEqual.computeValue(context);
 }

 @Test
 public void testComputeValueDifferentValues() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = new ConstantExpression(1);
 Expression arg2 = new ConstantExpression(2);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueSameValueDifferentExpressionTypes() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = new VariableExpression("x");
 Expression arg2 = new ConstantExpression(1);
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueSameValueSameExpression() {
 EvaluationContext context = new EvaluationContext();
 Expression arg1 = new VariableExpression("x");
 Expression arg2 = new VariableExpression("x");
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 Object result = coreOperationEqual.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGetSymbol() {
 CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
 String symbol = coreOperationEqual.getSymbol();
 assertEquals("=", symbol);
 }
}