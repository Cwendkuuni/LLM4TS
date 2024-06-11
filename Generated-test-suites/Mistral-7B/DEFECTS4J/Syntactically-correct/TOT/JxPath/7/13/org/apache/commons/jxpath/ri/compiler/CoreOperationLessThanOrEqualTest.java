import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.RuntimeContext;
import org.apache.commons.jxpath.ri.compiled.CompiledExpression;
import org.apache.commons.jxpath.ri.compiled.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithNumbers() throws Exception {
 CompiledExpression expr = new CoreOperationLessThanOrEqual(
 new ExpressionLiteral(5.0), new ExpressionLiteral(10.0));
 assertEquals(Boolean.TRUE, expr.computeValue(new EvaluationContext(new RuntimeContext(), new InfoSet())));
 }

 @Test
 public void testLessThanOrEqualWithNumbersNotEqual() throws Exception {
 CompiledExpression expr = new CoreOperationLessThanOrEqual(
 new ExpressionLiteral(10.0), new ExpressionLiteral(5.0));
 assertFalse(expr.computeValue(new EvaluationContext(new RuntimeContext(), new InfoSet())));
 }

 @Test
 public void testLessThanOrEqualWithNumbersEqual() throws Exception {
 CompiledExpression expr = new CoreOperationLessThanOrEqual(
 new ExpressionLiteral(5.0), new ExpressionLiteral(5.0));
 assertTrue(expr.computeValue(new EvaluationContext(new RuntimeContext(), new InfoSet())));
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanOrEqualWithInvalidExpression() throws Exception {
 CompiledExpression expr = new CoreOperationLessThanOrEqual(
 new ExpressionLiteral("invalid"), new ExpressionLiteral(5.0));
 // The test should fail with a RuntimeException
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanOrEqualWithNullExpression() throws Exception {
 CompiledExpression expr = new CoreOperationLessThanOrEqual(
 null, new ExpressionLiteral(5.0));
 // The test should fail with a RuntimeException
 }
}