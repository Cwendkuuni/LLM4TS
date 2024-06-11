package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        Expression expression2 = new ConcreteExpression();
        assertTrue(expression2.isContextDependent());
    }

    @Test
    public void testIsContextDependent_NullContext() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        EvalContext context = null;
        assertThrows(NullPointerException.class, () -> expression.isContextDependent(context));
    }

    @Test
    public void testIsContextDependent_EmptyContext() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        EvalContext context = new EvalContext();
        assertFalse(expression.isContextDependent(context));
    }

    @Test
    public void testIsContextDependent_SingleNodeContext() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        EvalContext context = new EvalContext();
        NodePointer nodePointer = new NodePointer("root", 1);
        context.setCurrentNodePointer(nodePointer);
        assertFalse(expression.isContextDependent(context));
    }

    @Test
    public void testIsContextDependent_MultipleNodeContext() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        EvalContext context = new EvalContext();
        NodePointer nodePointer1 = new NodePointer("root", 1);
        NodePointer nodePointer2 = new NodePointer("root", 2);
        context.setCurrentNodePointer(nodePointer1);
        context.addNodePointer(nodePointer2);

        assertFalse(expression.isContextDependent(context));
    }

    @Test
    public void testIsContextDependent_ComputeContextDependentBeforeIsContextDependent() {
        Expression expression = new ConcreteExpression();
        assertFalse(expression.isContextDependent());

        boolean contextDependent = expression.computeContextDependent();
        assertTrue(expression.isContextDependent());
    }
}