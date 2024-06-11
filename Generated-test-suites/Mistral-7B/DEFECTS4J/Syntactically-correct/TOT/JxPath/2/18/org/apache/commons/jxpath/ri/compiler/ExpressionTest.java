import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

 private EvalContext context;

 @Before
 public void setup() {
 context = Mockito.spy(new EvalContext());
 }

 @Test
 public void testIsContextDependent_ContextDependentExpression() {
 Expression expression = Mockito.spy(new ContextDependentExpression());
 assertTrue(expression.isContextDependent());
 }

 @Test
 public void testIsContextDependent_ContextIndependentExpression() {
 Expression expression = Mockito.spy(new ContextIndependentExpression());
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue_ContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 assertEquals(1.0, expression.computeValue(context), 0.0);
 context = Mockito.spy(new EvalContext());
 Mockito.doReturn(new NodePointer(null, "someNode", Locale.ENGLISH)).when(context).getCurrentNodePointer();
 assertEquals(2.0, expression.computeValue(context), 0.0);
 }

 @Test
 public void testComputeValue_ContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 assertEquals(1.0, expression.computeValue(context), 0.0);
 assertEquals(1.0, expression.computeValue(context), 0.0);
 }

 @Test
 public void testIterate_ContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 Iterator<Object> iterator = expression.iterate(context);
 assertEquals(1.0, iterator.next());
 context = Mockito.spy(new EvalContext());
 Mockito.doReturn(new NodePointer(null, "someNode", Locale.ENGLISH)).when(context).getCurrentNodePointer();
 assertEquals(2.0, iterator.next());
 }

 @Test
 public void testIterate_ContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 Iterator<Object> iterator = expression.iterate(context);
 assertEquals(1.0, iterator.next());
 assertEquals(1.0, iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove_ValueIterator() {
 Expression expression = new ContextIndependentExpression();
 Iterator<Object> iterator = expression.iterate(context);
 iterator.next();
 iterator.remove();
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1.0;
 }
}

class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1.0;
 }
}