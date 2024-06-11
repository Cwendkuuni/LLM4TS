import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithDifferentTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression("two"));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithSameValues() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
 assertEquals(Boolean.FALSE, notEqual.computeValue(new EvalContext()));
 }

 @Test
 public void testNotEqualWithNulls() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(null), new ConstantExpression("not null string"));
 assertEquals(Boolean.TRUE, notEqual.computeValue(new EvalContext()));

 CoreOperationNotEqual notEqualNull = new CoreOperationNotEqual(new ConstantExpression("not null string"), new ConstantExpression(null));
 assertEquals(Boolean.TRUE, notEqualNull.computeValue(new EvalContext()));
 }

 @Test(expected = ClassCastException.class)
 public void testNotEqualWithIncompatibleTypes() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new CoreOperationCompare(new ConstantExpression("two"), new ConstantExpression(2), Operator.DIVIDE));
 }

 @Test
 public void testNotEqualWithErrorInArguments() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(new Throwable()), new ConstantExpression(new Throwable()));
 try {
 notEqual.computeValue(new EvalContext());
 fail("Should have thrown an exception.");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testNotEqualWithErrorInContext() {
 CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
 EvalContext context = new EvalContext() {
 @Override
 public Object getVariableValue(String name) {
 throw new RuntimeException("Test error.");
 }
 };
 try {
 notEqual.computeValue(context);
 fail("Should have thrown an exception.");
 } catch (Exception e) {
 // Expected
 }
 }
}