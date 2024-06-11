import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri. Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqual_DifferentValues() {
 Expression arg1 = new CoreOperationNumber(1);
 Expression arg2 = new CoreOperationNumber(2);
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotEqual_SameValues() {
 Expression arg1 = new CoreOperationNumber(1);
 Expression arg2 = new CoreOperationNumber(1);
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_InvalidArgumentType() {
 Expression arg1 = new CoreOperationString("1");
 Expression arg2 = new CoreOperationNumber(2);
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_MismatchedArgumentTypes() {
 Expression arg1 = new CoreOperationNumber(1);
 Expression arg2 = new CoreOperationString("2");
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 EvalContext context = new EvalContext();
 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}