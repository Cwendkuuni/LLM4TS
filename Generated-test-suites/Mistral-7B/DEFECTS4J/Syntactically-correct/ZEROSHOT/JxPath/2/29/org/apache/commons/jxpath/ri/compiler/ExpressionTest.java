import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;
 private EvalContext context;

 @Before
 public void setUp() {
 context = Mockito.mock(EvalContext.class);
 expression = Mockito.spy(new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1;
 }

 @Override
 public Object compute(EvalContext context) {
 return 1;
 }
 });
 }

 @Test
 public void testIsContextDependent() {
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 Object result = expression.computeValue(context);
 assertEquals(1, result.doubleValue());
 }

 @Test
 public void testCompute() {
 Object result = expression.compute(context);
 assertEquals(1, result.doubleValue());
 }

 @Test
 public void testIterate() {
 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 assertNotNull(iterator.next());
 }

 @Test
 public void testIteratePointers() {
 Iterator iterator = expression.iteratePointers(context);
 assertTrue(iterator.hasNext());
 assertNotNull(iterator.next());
 }

 @Test
 public void testPointerIterator() {
 Iterator iterator = new Expression.PointerIterator(ValueUtils.iterate(new NodePointer("test", null, null)), new QName("test"), Locale.getDefault());
 assertTrue(iterator.hasNext());
 assertNotNull(iterator.next());
 }

 @Test
 public void testValueIterator() {
 Iterator iterator = new Expression.ValueIterator(ValueUtils.iterate(1));
 assertTrue(iterator.hasNext());
 assertNotNull(iterator.next());
 }
}