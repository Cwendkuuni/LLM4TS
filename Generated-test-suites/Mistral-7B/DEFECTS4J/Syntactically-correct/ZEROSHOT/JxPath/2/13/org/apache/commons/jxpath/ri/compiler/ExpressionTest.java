import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Expression.PointerIterator;
import org.apache.commons.jxpath.ri.compiler.Expression.ValueIterator;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

public class ExpressionTest {

 @Test
 public void testContextDependentComputation() {
 Expression expression = new ContextDependentExpression();
 assertEquals(true, expression.isContextDependent());
 }

 @Test
 public void testContextIndependentComputation() {
 Expression expression = new ContextIndependentExpression();
 assertEquals(false, expression.isContextDependent());
 }

 @Test
 public void testContextDependentComputeValue() throws Exception {
 Expression expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 assertEquals("result of context-dependent expression", "context-dependent value", expression.computeValue(context));
 }

 @Test
 public void testContextIndependentComputeValue() throws Exception {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 assertEquals("result of context-independent expression", 42, expression.computeValue(context).doubleValue());
 }

 @Test
 public void testContextDependentIterate() {
 Expression expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 Iterator expectedIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "context-dependent value";
 }
 };
 Iterator actualIterator = expression.iterate(context);
 assertEquals("iterator returned by context-dependent expression", expectedIterator, actualIterator);
 }

 @Test
 public void testContextIndependentIterate() {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 Object[] expectedValues = new Object[]{1, 2, 3};
 Iterator expectedIterator = ValueUtils.iterate(expectedValues);
 Iterator actualIterator = expression.iterate(context);
 assertSame("iterators should return the same elements", expectedIterator, actualIterator);
 assertArrayEquals("elements returned by context-independent expression", expectedValues, ValueUtils.toObjectArray(actualIterator));
 }

 @Test
 public void testContextDependentIteratePointers() {
 Expression expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 Pointer expectedPointer = new NodePointer(null, "context-dependent node", Locale.US);
 Iterator expectedIterator = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return expectedPointer;
 }
 };
 Iterator actualIterator = expression.iteratePointers(context);
 assertEquals("iterator returned by context-dependent expression", expectedIterator, actualIterator);
 }

 @Test
 public void testContextIndependentIteratePointers() {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(null, null, null);
 Pointer[] expectedPointers = new Pointer[]{new NodePointer(null, "context-independent node 1", Locale.US), new NodePointer(null, "context-independent node 2", Locale.US), new NodePointer(null, "context-independent node 3", Locale.US)};
 Iterator expectedIterator = ValueUtils.iterate(expectedPointers);
 Iterator actualIterator = expression.iteratePointers(context);
 assertSame("iterators should return the same pointers", expectedIterator, actualIterator);
 assertArrayEquals("pointers returned by context-independent expression", expectedPointers, ValueUtils.toObjectArray(actualIterator));
 }

 @Test
 public void testSameInstances() {
 Expression expression1 = new ContextIndependentExpression();
 Expression expression2 = new ContextIndependentExpression();
 assertNotSame("different instances of context-independent expressions should not be the same", expression1, expression2);
 }

 @Test
 public void testSameInstance() {
 Expression expression = new ContextIndependentExpression();
 assertEquals("same instance of context-independent expression should be the same", expression, expression);
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return "context-dependent value";
 }
}

class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 42;
 }
}