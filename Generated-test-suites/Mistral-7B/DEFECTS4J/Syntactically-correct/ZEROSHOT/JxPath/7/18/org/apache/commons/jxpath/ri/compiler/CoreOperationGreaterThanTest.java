import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

 @Test
 public void testCoreOperationGreaterThan() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(3.0)));
 assertEquals("Expected operation symbol to be greater than sign", ">", coreOperationGreaterThan.getSymbol());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoDoubleValuesLessThan() {
 EvalContext context = new EvalContext(new InfoSet());
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(2.0)), new ConstantExpression(new Double(5.0)));
 assertFalse("Expected false for 2 > 5", coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoDoubleValuesEqual() {
 EvalContext context = new EvalContext(new InfoSet());
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
 assertFalse("Expected false for 5 > 5", coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoDoubleValuesGreaterThan() {
 EvalContext context = new EvalContext(new InfoSet());
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(3.0)));
 assertTrue("Expected true for 5 > 3", coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoStringValues() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new String("five")), new ConstantExpression(new String("three")));
 assertFalse("Expected false for string comparison using '>' operator", coreOperationGreaterThan.computeValue(new EvalContext(new InfoSet())).booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testCoreOperationGreaterThanWithMismatchedTypes() {
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Integer(5)), new ConstantExpression(new String("three")));
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoIntegerValues() {
 EvalContext context = new EvalContext(new InfoSet());
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(3)));
 assertTrue("Expected true for 5 > 3", coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoVariableExpressions() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("var1", new Double(5.0));
 context.setVariable("var2", new Double(3.0));
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new VariableExpression("var1"), new VariableExpression("var2"));
 assertTrue("Expected true for var1 > var2", coreOperationGreaterThan.computeValue(context).booleanValue());
 }

 @Test
 public void testCoreOperationGreaterThanWithTwoListExpressions() {
 EvalContext context = new EvalContext(new InfoSet());
 context.setVariable("list1", TestUtil.createDoubleList(new double[] { 1.0, 2.0, 3.0 }));
 context.setVariable("list2", TestUtil.createDoubleList(new double[] { 4.0, 5.0, 6.0 }));
 CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
 new IndexedVariableExpression(new String("list1"), new IntExpression(1)),
 new IndexedVariableExpression(new String("list2"), new IntExpression(0)));
 assertTrue("Expected true for list1[1] > list2[0]", coreOperationGreaterThan.computeValue(context).booleanValue());
 }
}