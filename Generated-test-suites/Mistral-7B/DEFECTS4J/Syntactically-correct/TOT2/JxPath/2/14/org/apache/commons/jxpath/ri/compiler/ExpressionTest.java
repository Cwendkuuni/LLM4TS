package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression expr1 = new ContextDependentExpression();
    boolean actual1 = expr1.isContextDependent();
    boolean expected1 = true;
    assertTrue("isContextDependent() should return true for context-dependent expressions", actual1 == expected1);

    Expression expr2 = new ContextIndependentExpression();
    boolean actual2 = expr2.isContextDependent();
    boolean expected2 = false;
    assertTrue("isContextDependent() should return false for context-independent expressions", actual2 == expected2);
  }

  @Test
  public void testComputeValue() {
    Expression expr = new ConstantExpression(new NodePointer("root"));
    EvalContext context = new EvalContext(null, null);
    Object actual = expr.computeValue(context);
    Object expected = new NodePointer("root");
    assertEquals("computeValue() should return the expected node pointer", expected, actual);

    context = new EvalContext(null, null);
    context.setCurrentNodePointer(new NodePointer("root/child"));
    actual = expr.computeValue(context);
    expected = new NodePointer("root/child");
    assertEquals("computeValue() should return the expected node pointer", expected, actual);
  }

  @Test
  public void testCompute() {
    Expression expr1 = new ContextDependentExpression();
    EvalContext context = new EvalContext(null, null);
    Object actual1 = expr1.compute(context);
    assertNotNull("compute() should not return null for context-dependent expressions", actual1);

    context = new EvalContext(null, null);
    context.setCurrentNodePointer(new NodePointer("root"));
    Object actual2 = expr1.compute(context);
    assertNotNull("compute() should not return null for context-dependent expressions", actual2);

    Expression expr2 = new ConstantExpression(new NodePointer("root"));
    Object expected = new NodePointer("root");
    Object actual3 = expr2.compute(context);
    assertEquals("compute() should return the expected node pointer for context-independent expressions", expected, actual3);
  }

  @Test(expected = RuntimeException.class)
  public void testComputeWithNullContext() {
    Expression expr = new ContextDependentExpression();
    EvalContext context = null;
    expr.compute(context);
  }

  @Test
  public void testComputeWithEmptyContext() {
    Expression expr = new ContextDependentExpression();
    EvalContext context = new EvalContext(null, null);
    Object actual = expr.compute(context);
    assertNotNull("compute() should not return null for empty contexts", actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsContextDependentWithInvalidInput() {
    Object obj = new Object();
    Expression expr = new ContextDependentExpression();
    boolean actual = expr.isContextDependent();
  }

  @Test
  public void testComputeValueWithUnexpectedResultType() {
    Expression expr = new ContextDependentExpression();
    EvalContext context = new EvalContext(null, null);
    Object actual = expr.computeValue(context);
    assertFalse("computeValue() should not return a boolean", actual instanceof Boolean);
  }

  @Test
  public void testComputeWithUnexpectedResultType() {
    Expression expr = new ContextDependentExpression();
    EvalContext context = new EvalContext(null, null);
    Object actual = expr.compute(context);
    assertFalse("compute() should not return a boolean", actual instanceof Boolean);
  }
}