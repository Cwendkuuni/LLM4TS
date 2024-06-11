import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.model.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 @Test
 public void testNotEqualWithBooleanValues() {
 SimpleNode arg1 = new SimpleNode("true");
 SimpleNode arg2 = new SimpleNode("false");
 JXPathContext context = new JXPathContext();
 context.setVariables(new SimpleNode("x"), arg1);

 try {
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 } catch (EvaluationException e) {
 // Not expected for this test case
 }
 }

 @Test
 public void testNotEqualWithEqualValues() {
 SimpleNode arg1 = new SimpleNode("1");
 SimpleNode arg2 = new SimpleNode("1");
 JXPathContext context = new JXPathContext();
 context.setVariables(new SimpleNode("x"), arg1);

 try {
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 } catch (EvaluationException e) {
 // Not expected for this test case
 }
 }

 @Test
 public void testNotEqualWithDifferentDataTypes() {
 SimpleNode arg1 = new SimpleNode("1");
 SimpleNode arg2 = new SimpleNode("true");
 JXPathContext context = new JXPathContext();
 context.setVariables(new SimpleNode("x"), arg1);

 try {
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 } catch (EvaluationException e) {
 // This is expected, since different data types can't be compared using "!"=
 assertTrue(e.getMessage().contains("Cannot compare Node [1] of type NUMBER with Node [true] of type BOOLEAN"));
 }
 }

 @Test
 public void testNotEqualWithNullValues() {
 SimpleNode arg1 = new SimpleNode(null);
 SimpleNode arg2 = new SimpleNode("0");
 JXPathContext context = new JXPathContext();
 context.setVariables(new SimpleNode("x"), arg1);

 try {
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 } catch (EvaluationException e) {
 // Not expected for this test case
 }
 }

 @Test
 public void testNotEqualWithNullAndNonNullValues() {
 SimpleNode arg1 = new SimpleNode(null);
 SimpleNode arg2 = new SimpleNode("foo");
 JXPathContext context = new JXPathContext();
 context.setVariables(new SimpleNode("x"), arg1);

 try {
 CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 } catch (EvaluationException e) {
 // Not expected for this test case
 }
 }
}