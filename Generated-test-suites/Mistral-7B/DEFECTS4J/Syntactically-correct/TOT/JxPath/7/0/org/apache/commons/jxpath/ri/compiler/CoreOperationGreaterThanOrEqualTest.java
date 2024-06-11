import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri. expressions.Expression;
import org.apache.commons.jxpath.ri. expressions.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqualWithBothNumbersGreaterThanOrEqual() {
 Expression arg1 = new Constant(1.5);
 Expression arg2 = new Constant(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualWithFirstNumberGreaterThanSecondNumber() {
 Expression arg1 = new Constant(3.5);
 Expression arg2 = new Constant(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualWithBothNumbersEqual() {
 Expression arg1 = new Constant(5.5);
 Expression arg2 = new Constant(5.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqualWithFirstNumberLessThanSecondNumber() {
 Expression arg1 = new Constant(1.5);
 Expression arg2 = new Constant(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = ClassCastException.class)
 public void testGreaterThanOrEqualWithOneArgumentNotANumber() {
 Expression arg1 = new Constant("abc");
 Expression arg2 = new Constant(2.5);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 fail("An exception should have been thrown");
 }

 @Test(expected = ClassCastException.class)
 public void testGreaterThanOrEqualWithSecondArgumentNotANumber() {
 Expression arg1 = new Constant(1.5);
 Expression arg2 = new Constant('a');
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 InfoSet infoSet = new InfoSet();
 JXPathContext context = new JXPathContext(new EvaluationContext(), infoSet);
 Object result = operation.computeValue(context);
 fail("An exception should have been thrown");
 }
}