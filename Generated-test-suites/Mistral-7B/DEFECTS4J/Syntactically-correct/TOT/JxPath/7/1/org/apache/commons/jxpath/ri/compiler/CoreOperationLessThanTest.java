import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanWithNumbers() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreNumberExpression(1.0), new CoreNumberExpression(2.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Boolean result = (Boolean) expression.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanWithEqualNumbers() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreNumberExpression(1.0), new CoreNumberExpression(1.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Boolean result = (Boolean) expression.computeValue(context);
 assertFalse(result.booleanValue());
 }

 @Test
 public void testLessThanWithNegativeNumbers() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreNumberExpression(-1.0), new CoreNumberExpression(-2.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Boolean result = (Boolean) expression.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanWithZero() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreNumberExpression(0.0), new CoreNumberExpression(1.0));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Boolean result = (Boolean) expression.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test
 public void testLessThanWithInfinities() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreNumberExpression(Double.NEGATIVE_INFINITY), new CoreNumberExpression(Double.POSITIVE_INFINITY));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Boolean result = (Boolean) expression.computeValue(context);
 assertTrue(result.booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithNonNumericValues() {
 CoreOperationLessThan expression = new CoreOperationLessThan(
 new CoreStringExpression("str1"), new CoreStringExpression("str2"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 expression.computeValue(context);
 fail("Expected ClassCastException");
 }
}