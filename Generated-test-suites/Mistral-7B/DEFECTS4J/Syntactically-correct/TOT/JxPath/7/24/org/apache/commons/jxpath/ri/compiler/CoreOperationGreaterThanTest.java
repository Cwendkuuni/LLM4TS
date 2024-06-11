import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.runtime.Context;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanPositive() {
 InfoSet infoSet = new InfoSet();
 Context context = new EvalContext();

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan gt = new CoreOperationGreaterThan(arg1, arg2);

 try {
 Object result = gt.computeValue(context);
 JXPathContext jxpathContext = new JXPathContext(infoSet);
 jxpathContext.setContextItem(context);
 assertEquals(Boolean.TRUE, jxpathContext.getValue(gt));
 } catch (EvaluationException e) {
 // Should not happen
 }
 }

 @Test
 public void testGreaterThanNegative() {
 InfoSet infoSet = new InfoSet();
 Context context = new EvalContext();

 Expression arg1 = new LiteralExpression(1.0);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThan gt = new CoreOperationGreaterThan(arg1, arg2);

 try {
 Object result = gt.computeValue(context);
 JXPathContext jxpathContext = new JXPathContext(infoSet);
 jxpathContext.setContextItem(context);
 assertFalse(jxpathContext.getValue(gt).booleanValue());
 } catch (EvaluationException e) {
 // Should not happen
 }
 }

 @Test
 public void testGreaterThanZero() {
 InfoSet infoSet = new InfoSet();
 Context context = new EvalContext();

 Expression arg1 = new LiteralExpression(0);
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan gt = new CoreOperationGreaterThan(arg1, arg2);

 try {
 Object result = gt.computeValue(context);
 JXPathContext jxpathContext = new JXPathContext(infoSet);
 jxpathContext.setContextItem(context);
 assertTrue(jxpathContext.getValue(gt).booleanValue());
 } catch (EvaluationException e) {
 // Should not happen
 }
 }

 @Test(expected = EvaluationException.class)
 public void testGreaterThanNaN() {
 InfoSet infoSet = new InfoSet();
 Context context = new EvalContext();

 Expression arg1 = new LiteralExpression(Double.NaN);
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan gt = new CoreOperationGreaterThan(arg1, arg2);

 try {
 Object result = gt.computeValue(context);
 } catch (EvaluationException e) {
 // Expected
 assertEquals("Cannot compare NaN with a number", e.getMessage());
 }
 }

 @Test(expected = EvaluationException.class)
 public void testGreaterThanNull() {
 InfoSet infoSet = new InfoSet();
 Context context = new EvalContext();

 Expression arg1 = new LiteralExpression(null);
 Expression arg2 = new LiteralExpression(1.0);
 CoreOperationGreaterThan gt = new CoreOperationGreaterThan(arg1, arg2);

 try {
 Object result = gt.computeValue(context);
 } catch (EvaluationException e) {
 // Expected
 assertEquals("Cannot compare null with a number", e.getMessage());
 }
 }
}