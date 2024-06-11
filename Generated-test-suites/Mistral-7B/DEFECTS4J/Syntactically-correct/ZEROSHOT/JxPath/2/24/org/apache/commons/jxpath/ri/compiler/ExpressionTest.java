import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 // Replace this with the actual instance of Expression to be tested.
 expression = new ConstantExpression(ONE);
 }

 @Test
 public void testIsContextDependent() {
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Object result = expression.computeValue(new EvalContext());
 assertTrue(result.equals(ONE));
 }

 @Test
 public void testCompute() {
 Object result = expression.compute(new EvalContext());
 assertTrue(result.equals(ONE));
 }

 @Test
 public void testIterate() {
 Iterator<Object> iterator = expression.iterate(new EvalContext());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.next().equals(ONE));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 Iterator<Pointer> iterator = expression.iteratePointers(new EvalContext());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testComputeValueWithContext() {
 EvalContext context = new EvalContext();
 context.setCurrentNodePointer(new NodePointer(new QName("root"), null, null));
 context.setVariable("myVar", new NodePointer(new QName("myNode"), null, null));

 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromIterator() {
 Iterator<Object> iterator = expression.iterate(new EvalContext());
 iterator.next();
 iterator.remove();
 }
}