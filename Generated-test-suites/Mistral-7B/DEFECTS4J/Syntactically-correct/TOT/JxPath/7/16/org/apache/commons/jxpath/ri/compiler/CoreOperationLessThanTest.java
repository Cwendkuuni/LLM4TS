import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.Constants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanWithNumbers() {
 Expression arg1 = new Const(5.0);
 Expression arg2 = new Const(10.0);
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 arg1 = new Const(3.5);
 arg2 = new Const(5.0);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 arg1 = new Const(2.0);
 arg2 = new Const(3.5);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);

 arg1 = new Const(1.0);
 arg2 = new Const(2.0);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanWithNegativeNumbers() {
 Expression arg1 = new Const(-5.0);
 Expression arg2 = new Const(-10.0);
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 arg1 = new Const(-3.5);
 arg2 = new Const(-5.0);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 arg1 = new Const(-2.0);
 arg2 = new Const(-3.5);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 arg1 = new Const(-1.0);
 arg2 = new Const(-2.0);
 lessThan = new CoreOperationLessThan(arg1, arg2);
 result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithDifferentTypes() {
 Expression arg1 = new Const(5.0);
 Expression arg2 = new Const("5");
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 // This test should fail since the types of arg1 and arg2 are different
 // However, JXPath evaluates this expression as false due to type coercion
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanWithNull() {
 Expression arg1 = new Const(5.0);
 Expression arg2 = new Null();
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 // This test should throw an IllegalArgumentException since comparing a number to null is not supported
 }

 @Test
 public void testLessThanWithStrings() {
 Expression arg1 = new Const("3");
 Expression arg2 = new Const("5");
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithZero() {
 Expression arg1 = new Const(0.0);
 Expression arg2 = new Const(0.0);
 CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
 EvaluationContext context = new EvaluationContext(new InfoSet());
 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }
}