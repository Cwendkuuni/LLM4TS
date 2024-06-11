import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Parser;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.util.ExpressionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqual() {
 // Test with valid numbers
 InfoSet is = Parser.parse("1.0", null).evaluate(new EvaluationContext());
 Expression arg1 = new ExpressionBuilder(is, null).value("2.0").build();
 Expression arg2 = new ExpressionBuilder(is, null).value("2.0").build();
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 assertEquals(Boolean.TRUE, lessThanOrEqual.computeValue(new EvaluationContext()));

 // Test with valid numbers, first argument greater than second
 arg1 = new ExpressionBuilder(is, null).value("3.0").build();
 assertEquals(Boolean.FALSE, lessThanOrEqual.computeValue(new EvaluationContext()));

 // Test with valid numbers, first argument equal to second
 arg1 = new ExpressionBuilder(is, null).value("2.0").build();
 assertEquals(Boolean.TRUE, lessThanOrEqual.computeValue(new EvaluationContext()));

 // Test with NaN and infinity
 arg1 = new ExpressionBuilder(is, null).value("NaN").build();
 arg2 = new ExpressionBuilder(is, null).value("Inf").build();
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 assertEquals(Boolean.FALSE, lessThanOrEqual.computeValue(new EvaluationContext()));

 // Test with negative numbers
 arg1 = new ExpressionBuilder(is, null).value("-1.0").build();
 arg2 = new ExpressionBuilder(is, null).value("-2.0").build();
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 assertEquals(Boolean.TRUE, lessThanOrEqual.computeValue(new EvaluationContext()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidExpression() {
 // Test with invalid expressions
 InfoSet is = Parser.parse("1.0", null).evaluate(new EvaluationContext());
 Expression arg1 = new ExpressionBuilder(is, null).value("InvalidExpression").build();
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, new ExpressionBuilder(is, null).value("2.0").build());
 }
}