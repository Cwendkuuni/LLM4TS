import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithBothNumbersLessThan() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(2.0));
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 assertEquals(Boolean.TRUE, is.getValue("result"));
 }

 @Test
 public void testLessThanOrEqualWithFirstNumberGreaterThan() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(1.0));
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 assertEquals(Boolean.FALSE, is.getValue("result"));
 }

 @Test
 public void testLessThanOrEqualWithBothNumbersEqual() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 assertEquals(Boolean.TRUE, is.getValue("result"));
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanOrEqualWithInvalidTypes() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new StringExpression("str1"), new StringExpression("str2"));
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 }

 @Test
 public void testLessThanOrEqualWithNulls() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new NullExpression(), new NullExpression());
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 assertEquals(Boolean.TRUE, is.getValue("result"));
 }

 @Test
 public void testLessThanOrEqualWithMixedTypes() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new StringExpression("2.5"));
 EqualOperator equal = new EqualOperator(new VariableExpression("result"), op);
 InfoSet is = new MockInfoSet();
 EvalContext context = new MockEvalContext(is);
 equal.computeValue(context);
 assertEquals(Boolean.FALSE, is.getValue("result"));
 }
}