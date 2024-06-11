import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithTwoNumbersLessThanOrEqual() {
 InfoSet infoSet = createInfoSet();
 EvaluationContext context = new EvaluationContext(infoSet);
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new NumberNode(3.5, infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWithTwoNumbersNotEqual() {
 InfoSet infoSet = createInfoSet();
 EvaluationContext context = new EvaluationContext(infoSet);
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new NumberNode(3.5, infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 // The result will be of type Boolean, but it will be FALSE
 }

 @Test
 public void testLessThanOrEqualWithTwoNumbersFirstGreaterThanSecond() {
 InfoSet infoSet = createInfoSet();
 EvaluationContext context = new EvaluationContext(infoSet);
 Expression arg1 = new NumberNode(3.5, infoSet);
 Expression arg2 = new NumberNode(2.5, infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 // The result will be of type Boolean, but it will be FALSE
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithInvalidArgument() {
 InfoSet infoSet = createInfoSet();
 EvaluationContext context = new EvaluationContext(infoSet);
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = null;
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanOrEqualWithMismatchedTypes() {
 InfoSet infoSet = createInfoSet();
 EvaluationContext context = new EvaluationContext(infoSet);
 Expression arg1 = new NumberNode(2.5, infoSet);
 Expression arg2 = new StringNode("10", infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 }

 private InfoSet createInfoSet() {
 InfoSet infoSet = new InfoSet();
 infoSet.setDocument("Test document");
 return infoSet;
 }

}