package org.apache.commons.jxpath.ri.axes;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testUnionContextWithSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        unionContext = new UnionContext(parentContext, new EvalContext[] { context });
        context.nextSet();
        context.nextNode();
        unionContext.setPosition(0);
        NodePointer expectedNodePointer = unionContext.getCurrentNodePointer();
        NodePointer nodePointer = context.getCurrentNodePointer();
        assertEquals(nodePointer, expectedNodePointer);
    }

    @Test
    public void testUnionContextWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        context1.nextSet();
        context1.nextNode();
        context2.nextSet();
        context2.nextNode();
        unionContext.setPosition(0);
        NodePointer expectedNodePointer1 = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer2 = unionContext.nextNode().getCurrentNodePointer();
        NodePointer nodePointer1 = context1.getCurrentNodePointer();
        NodePointer nodePointer2 = context2.getCurrentNodePointer();
        assertEquals(nodePointer1, expectedNodePointer1);
        assertEquals(nodePointer2, expectedNodePointer2);
    }

    @Test
    public void testUnionContextWithMultipleContextsWithSameNode() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        context1.addNode(new NodePointer("root"));
        context2.addNode(new NodePointer("root"));
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(0);
        NodePointer expectedNodePointer1 = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer2 = unionContext.nextNode().getCurrentNodePointer();
        NodePointer nodePointer1 = context1.getCurrentNodePointer();
        NodePointer nodePointer2 = context2.getCurrentNodePointer();
        assertEquals(nodePointer1, expectedNodePointer1);
        assertEquals(nodePointer2, expectedNodePointer2);
    }

    @Test
    public void testUnionContextWithContextsContainingDifferentNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        context1.addNode(new NodePointer("root1"));
        context2.addNode(new NodePointer("root2"));
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(0);
        NodePointer expectedNodePointer1 = unionContext.getCurrentNodePointer();
        NodePointer nodePointer1 = context1.getCurrentNodePointer();
        NodePointer expectedNodePointer2 = unionContext.nextNode().getCurrentNodePointer();
        NodePointer nodePointer2 = context2.getCurrentNodePointer();
        assertNotSame(nodePointer1, nodePointer2);
        assertEquals(expectedNodePointer1, nodePointer1);
        assertEquals(expectedNodePointer2, nodePointer2);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnionContextAfterEndOfContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        context1.nextSet();
        context1.nextNode();
        context2.nextSet();
        context2.nextNode();
        context2.nextNode(); // Go beyond the end of the context
        unionContext.setPosition(0);
    }
}