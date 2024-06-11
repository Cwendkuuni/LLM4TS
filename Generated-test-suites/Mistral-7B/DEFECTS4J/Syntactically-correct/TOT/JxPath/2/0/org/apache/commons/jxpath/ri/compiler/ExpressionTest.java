import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testIsContextDependent_ContextIndependent() {
 expression = new MyContextIndependentExpression();
 assertTrue(expression.isContextDependent());
 }

 @Test
 public void testIsContextDependent_ContextDependent() {
 expression = new MyContextDependentExpression();
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue_ContextIndependent() {
 expression = new MyContextIndependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 Object result = expression.computeValue(context);
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testComputeValue_ContextDependent() {
 expression = new MyContextDependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 context.setCurrentNodePointer(new NodePointer("root", null, null, null));
 Object result = expression.computeValue(context);
 assertEquals(context.getCurrentNodePointer(), result);
 }

 @Test
 public void testIterate_ContextIndependent() {
 expression = new MyContextIndependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 Iterator<Object> iterator = expression.iterate(context);
 List<Object> result = new ArrayList<>();
 while (iterator.hasNext()) {
 result.add(iterator.next());
 }
 assertTrue(result.contains(1.0));
 }

 @Test
 public void testIterate_ContextDependent() {
 expression = new MyContextDependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 context.setCurrentNodePointer(new NodePointer("root", null, null, null));
 Iterator<Object> iterator = expression.iterate(context);
 List<Object> result = new ArrayList<>();
 while (iterator.hasNext()) {
 result.add(iterator.next());
 }
 assertTrue(result.contains(context.getCurrentNodePointer()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterate_WithNullContext() {
 expression = new MyContextIndependentExpression();
 Iterator<Object> iterator = expression.iterate(null);
 }

 @Test
 public void testCompute_ContextIndependent() {
 expression = new MyContextIndependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 Object result = expression.compute(context);
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testCompute_ContextDependent() {
 expression = new MyContextDependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 context.setCurrentNodePointer(new NodePointer("root", null, null, null));
 Object result = expression.compute(context);
 assertEquals(context.getCurrentNodePointer(), result);
 }

 @Test
 public void testIteratePointers_ContextIndependent() {
 expression = new MyContextIndependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 Iterator<Pointer> iterator = expression.iteratePointers(context);
 assertNull(iterator.next());
 }

 @Test
 public void testIteratePointers_ContextDependent() {
 expression = new MyContextDependentExpression();
 EvaluationContext context = new EvaluationContext(null, null, null);
 context.setCurrentNodePointer(new NodePointer("root", null, null, null));
 Iterator<Pointer> iterator = expression.iteratePointers(context);
 Pointer result = iterator.next();
 assertNotNull(result);
 assertEquals("root", result.getNodeName());
 }

 @Test
 public void testIteratePointers_WithNullContext() {
 expression = new MyContextIndependentExpression();
 Iterator<Pointer> iterator = expression.iteratePointers(null);
 }

 private static class MyContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvaluationContext context) {
 return 1.0;
 }

 @Override
 public Object compute(EvaluationContext context) {
 return 1.0;
 }
 }

 private static class MyContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvaluationContext context) {
 return context.getCurrentNodePointer();
 }
}

}