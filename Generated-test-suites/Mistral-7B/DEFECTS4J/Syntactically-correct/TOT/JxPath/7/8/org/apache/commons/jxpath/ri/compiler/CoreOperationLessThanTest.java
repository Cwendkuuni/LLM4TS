import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Queries;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 Expression arg1 = Queries.newExpression("2.0");
 Expression arg2 = Queries.newExpression("3.0");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 Object result = op.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanEqual() {
 Expression arg1 = Queries.newExpression("2.0");
 Expression arg2 = Queries.newExpression("2.0");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 Object result = op.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGreaterThan() {
 Expression arg1 = Queries.newExpression("3.0");
 Expression arg2 = Queries.newExpression("2.0");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 Object result = op.computeValue(evalContext);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = ClassCastException.class)
 public void testNonNumeric() {
 Expression arg1 = Queries.newExpression("two");
 Expression arg2 = Queries.newExpression("three");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 op.computeValue(evalContext);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanNegativeAndNegative() {
 Expression arg1 = Queries.newExpression("-5.0");
 Expression arg2 = Queries.newExpression("-3.0");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 try {
 op.computeValue(evalContext);
 } catch (Exception e) {
 fail("Expected IllegalArgumentException but got " + e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanZeroAndPositive() {
 Expression arg1 = Queries.newExpression("0.0");
 Expression arg2 = Queries.newExpression("5.0");
 CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
 Context context = new Context();
 InfoSet infoSet = new InfoSet();
 context.setInfoSet(infoSet);
 EvaluationContext evalContext = new EvaluationContext(context, null);
 try {
 op.computeValue(evalContext);
 } catch (Exception e) {
 fail("Expected IllegalArgumentException but got " + e);
 }
 }
}