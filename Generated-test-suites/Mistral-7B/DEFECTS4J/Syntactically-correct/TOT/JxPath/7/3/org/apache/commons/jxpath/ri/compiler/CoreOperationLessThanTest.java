import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 InfoSet is = InfoSet.EMPTY; // empty InfoSet

 // typical use case: 5 < 10
 CoreOperationLessThan lessThan1 = new CoreOperationLessThan(
 new ConstantExpression(5.0), new ConstantExpression(10.0));
 Object result1 = lessThan1.computeValue(new EvalContext(is));
 assertTrue(result1 instanceof Boolean);
 assertTrue((Boolean) result1);

 // typical use case: 0.5 < 1.5
 CoreOperationLessThan lessThan2 = new CoreOperationLessThan(
 new ConstantExpression(0.5), new ConstantExpression(1.5));
 Object result2 = lessThan2.computeValue(new EvalContext(is));
 assertTrue(result2 instanceof Boolean);
 assertTrue((Boolean) result2);

 // edge case: 0 < 0
 CoreOperationLessThan lessThan3 = new CoreOperationLessThan(
 new ConstantExpression(0.0), new ConstantExpression(0.0));
 Object result3 = lessThan3.computeValue(new EvalContext(is));
 assertFalse(result3 instanceof Boolean);
 assertFalse(result3 == Boolean.FALSE); // ensure NaN is not considered false

 // error case: less than operation with non-numeric operands
 CoreOperationLessThan lessThan4 = new CoreOperationLessThan(
 new ConstantExpression("string1"), new ConstantExpression("string2"));
 Object result4 = lessThan4.computeValue(new EvalContext(is));
 assertFalse(result4 instanceof Boolean);
 }
}