import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testCoreOperationLessThanOrEqual() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(10.0)));
 EqualEvaluator evaluator = new EqualEvaluator();

 InfoSet infoSet = TestSupport.getTestInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationLessThanOrEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse(((Boolean) result).booleanValue());
 }

 @Test
 public void testCoreOperationLessThanOrEqualTrue() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
 EqualEvaluator evaluator = new EqualEvaluator();

 InfoSet infoSet = TestSupport.getTestInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationLessThanOrEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue(((Boolean) result).booleanValue());
 }

 @Test
 public void testCoreOperationLessThanOrEqualWithNaN() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
 new ConstantExpression(new Double(Double.NaN)), new ConstantExpression(new Double(5.0)));
 EqualEvaluator evaluator = new EqualEvaluator();

 InfoSet infoSet = TestSupport.getTestInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationLessThanOrEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse(((Boolean) result).booleanValue());
 }

 @Test
 public void testCoreOperationLessThanOrEqualWithNegativeNumber() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
 new ConstantExpression(new Double(-5.0)), new ConstantExpression(new Double(-10.0)));
 EqualEvaluator evaluator = new EqualEvaluator();

 InfoSet infoSet = TestSupport.getTestInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationLessThanOrEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertTrue(((Boolean) result).booleanValue());
 }

 @Test
 public void testCoreOperationLessThanOrEqualWithPositiveNumber() {
 CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(1.0)));
 EqualEvaluator evaluator = new EqualEvaluator();

 InfoSet infoSet = TestSupport.getTestInfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationLessThanOrEqual.computeValue(context);

 assertTrue(result instanceof Boolean);
 assertFalse(((Boolean) result).booleanValue());
 }
}