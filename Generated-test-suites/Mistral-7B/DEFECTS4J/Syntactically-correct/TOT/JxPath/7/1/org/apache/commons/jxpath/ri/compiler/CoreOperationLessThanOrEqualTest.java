import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithBothDoubleNumbers() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression(1.5), new CoreConstantExpression(2.5));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithBothNegativeDoubleNumbers() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression(-1.5), new CoreConstantExpression(-2.5));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithOneDoubleNumberAndOneInteger() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression(1.5), new CoreConstantExpression(2));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithOneDoubleNumberAndOneString() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression("1.5"), new CoreConstantExpression("2.5"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 try {
 lessThanOrEqual.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLessThanOrEqualWithBothStringValues() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression("1.5"), new CoreConstantExpression("2.5"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 try {
 lessThanOrEqual.computeValue(context);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLessThanOrEqualWithBothNullValues() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression(null), new CoreConstantExpression(null));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testLessThanOrEqualWithOneNullValue() {
 CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(
 new CoreConstantExpression(null), new CoreConstantExpression(1.5));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertNull(result);
 }
}