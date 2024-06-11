import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 private static final double DELTA = 0.001;

 @Test
 public void testGreaterThanOrEqual() {
 InfoSet infoSet = TestUtils.createInfoSet();
 EvalContext context = new EvalContext();

 // Test with double values
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", 5.0);
 InfoSetUtil.setDoubleValue(infoSet, "$b", 5.0);
 Expression arg1 = new ConstantExpression(5.0);
 Expression arg2 = new ConstantExpression(5.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 boolean result = (boolean) operation.computeValue(context);
 assertTrue(result);

 // Test with double values where arg1 >= arg2 is true
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", 10.0);
 InfoSetUtil.setDoubleValue(infoSet, "$b", 5.0);
 arg1 = new ConstantExpression(10.0);
 arg2 = new ConstantExpression(5.0);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = (boolean) operation.computeValue(context);
 assertTrue(result);

 // Test with double values where arg1 >= arg2 is false
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", 5.0);
 InfoSetUtil.setDoubleValue(infoSet, "$b", 10.0);
 arg1 = new ConstantExpression(5.0);
 arg2 = new ConstantExpression(10.0);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = (boolean) operation.computeValue(context);
 assertFalse(result);

 // Test with NaN
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", Double.NaN);
 InfoSetUtil.setDoubleValue(infoSet, "$b", Double.NaN);
 arg1 = new ConstantExpression(Double.NaN);
 arg2 = new ConstantExpression(Double.NaN);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 assertFalse(operation.computeValue(context) instanceof Boolean);

 // Test with Infinity
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", Double.POSITIVE_INFINITY);
 InfoSetUtil.setDoubleValue(infoSet, "$b", Double.POSITIVE_INFINITY);
 arg1 = new ConstantExpression(Double.POSITIVE_INFINITY);
 arg2 = new ConstantExpression(Double.POSITIVE_INFINITY);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 assertTrue(operation.computeValue(context) instanceof Boolean);

 // Test with Negative Infinity
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", Double.NEGATIVE_INFINITY);
 InfoSetUtil.setDoubleValue(infoSet, "$b", Double.NEGATIVE_INFINITY);
 arg1 = new ConstantExpression(Double.NEGATIVE_INFINITY);
 arg2 = new ConstantExpression(Double.NEGATIVE_INFINITY);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 assertTrue(operation.computeValue(context) instanceof Boolean);

 // Test with negative values
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", -5.0);
 InfoSetUtil.setDoubleValue(infoSet, "$b", -6.0);
 arg1 = new ConstantExpression(-5.0);
 arg2 = new ConstantExpression(-6.0);
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 assertTrue(result);
 }

 @Test
 public void testNegativeValuesWithSmallerFirstArgument() {
 // Test with negative values where arg1 < arg2
 InfoSet infoSet = TestUtils.createInfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentInfoSet(infoSet);
 InfoSetUtil.setDoubleValue(infoSet, "$a", -5.0);
 InfoSetUtil.setDoubleValue(infoSet, "$b", -6.0);
 Expression arg1 = new ConstantExpression(-5.0);
 Expression arg2 = new ConstantExpression(-6.0);
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 boolean result = (boolean) operation.computeValue(context);
 assertFalse(result);
 }
}