import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<child1><value1>1</value1></child1>\n" +
         "<child2><value2>2</value2></child2>\n" +
         "<child3></child3>\n" +
         "<child4/>\n" +
         "<child5><value5></value5></child5>\n" +
         "</root>";

 @Test
 public void testContextDependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextDependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);

 assertFalse(expression.isContextDependent());
 Object value = expression.computeValue(evalContext);
 assertNotNull(value);
 EvalContext newContext = new EvalContext(context, null, null);
 assertFalse(expression.isContextDependent());
 Object newValue = expression.computeValue(newContext);
 assertEquals(value, newValue);
 }

 @Test
 public void testContextIndependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextIndependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 Object value = expression.computeValue(evalContext);
 assertNotNull(value);
 EvalContext newContext = new EvalContext(context, null, null);
 Expression newExpression = new ContextIndependentExpression();
 Object newValue = newExpression.computeValue(newContext);
 assertEquals(value, newValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratePointerOnContextIndependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextIndependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 Iterator<Pointer> iterator = expression.iteratePointers(evalContext);
 iterator.next();
 }

 @Test
 public void testIteratePointerOnContextDependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextDependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 Iterator<Pointer> iterator = expression.iteratePointers(evalContext);
 Pointer pointer = iterator.next();
 assertNotNull(pointer);
 assertNotNull(pointer.getValue());
 EvalContext newContext = new EvalContext(context, null, null);
 iterator = expression.iteratePointers(newContext);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterateValueOnContextDependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextDependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 Iterator<Object> iterator = expression.iterate(evalContext);
 Object value = iterator.next();
 assertNotNull(value);
 EvalContext newContext = new EvalContext(context, null, null);
 expression = new ContextDependentExpression();
 iterator = expression.iterate(newContext);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterateValueOnContextIndependentExpression() {
 Object context = JXPathContextFactory.newContext(Document.parse(XML));
 Expression expression = new ContextIndependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 Iterator<Object> iterator = expression.iterate(evalContext);
 Object value = iterator.next();
 assertNotNull(value);
 EvalContext newContext = new EvalContext(context, null, null);
 Expression newExpression = new ContextIndependentExpression();
 iterator = newExpression.iterate(newContext);
 Object newValue = iterator.next();
 assertEquals(value, newValue);
 }

 @Test
 public void testEmptyContext() {
 Object context = JXPathContextFactory.newContext(null);
 Expression expression = new ContextIndependentExpression();
 EvalContext evalContext = new EvalContext(context, null, null);
 assertNull(expression.computeValue(evalContext));
 }

 @Test
 public void testNullContext() {
 Expression expression = new ContextIndependentExpression();
 assertNull(expression.computeValue(null));
 }

 @Test
 public void testNullEvalContext() {
 Expression expression = new ContextIndependentExpression();
 EvalContext evalContext = null;
 assertNull(expression.computeValue(evalContext));
 }

 private static class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getRootContext().getCurrentNodePointer().getValue();
 }
 }

 private static class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return ONE;
 }
 }
}