import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanWithNumbers() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 SimpleNode arg1 = new SimpleNode(1.5);
 SimpleNode arg2 = new SimpleNode(2.5);
 CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 arg1 = new SimpleNode(1.0);
 arg2 = new SimpleNode(2.0);
 operation = new CoreOperationLessThan(arg1, arg2);

 result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 arg1 = new SimpleNode(1.0);
 arg2 = new SimpleNode(1.5);
 operation = new CoreOperationLessThan(arg1, arg2);

 result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse(result == null);
 assertEquals(Boolean.FALSE, (Boolean) result);

 arg1 = new SimpleNode(1.5);
 arg2 = new SimpleNode(1.0);
 operation = new CoreOperationLessThan(arg1, arg2);

 result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanWithString() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 SimpleNode arg1 = new SimpleNode("a");
 SimpleNode arg2 = new SimpleNode("b");
 CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

 operation.computeValue(context);
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanWithMixedTypes() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);

 SimpleNode arg1 = new SimpleNode(1.5);
 SimpleNode arg2 = new SimpleNode("2");
 CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

 operation.computeValue(context);
 }
}