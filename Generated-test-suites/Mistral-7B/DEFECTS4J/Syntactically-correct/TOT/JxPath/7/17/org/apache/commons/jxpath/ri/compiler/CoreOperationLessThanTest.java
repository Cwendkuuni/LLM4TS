import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri. NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanWithNumbers() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 InfoSet infoSet = new InfoSet();
 EvaluationContext context = new EvaluationContext(new NodeInfo(1.0, "root", "", null, infoSet));
 Object result = lessThan.computeValue(context);
 assertTrue("Expected less than condition to evaluate to true", result instanceof Boolean && ((Boolean) result).booleanValue());
 }

 @Test
 public void testLessThanWithEqualValues() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
 InfoSet infoSet = new InfoSet();
 EvaluationContext context = new EvaluationContext(new NodeInfo(1.0, "root", "", null, infoSet));
 Object result = lessThan.computeValue(context);
 assertFalse("Expected less than condition to evaluate to false", result instanceof Boolean && ((Boolean) result).booleanValue());
 }

 @Test
 public void testLessThanWithNegativeNumbers() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(new ConstantExpression(-1.0), new ConstantExpression(-2.0));
 InfoSet infoSet = new InfoSet();
 EvaluationContext context = new EvaluationContext(new NodeInfo(1.0, "root", "", null, infoSet));
 Object result = lessThan.computeValue(context);
 assertTrue("Expected less than condition to evaluate to true", result instanceof Boolean && ((Boolean) result).booleanValue());
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithNonNumericTypes() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(new StringExpression("1"), new StringExpression("2"));
 InfoSet infoSet = new InfoSet();
 EvaluationContext context = new EvaluationContext(new NodeInfo(1.0, "root", "", null, infoSet));
 lessThan.computeValue(context); // Test for runtime exception
 }
}