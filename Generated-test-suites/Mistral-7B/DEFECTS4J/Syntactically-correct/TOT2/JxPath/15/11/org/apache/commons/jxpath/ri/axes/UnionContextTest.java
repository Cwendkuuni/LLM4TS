package org.apache.commons.jxpath.ri.axes.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testEmptyContexts() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        // Check if the NodeSet is empty
        assertEquals(0, unionContext.getNodeSet().size());
    }

    @Test
    public void testDifferentDocumentOrders() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        context1.setDocumentOrder(1);
        context2.setDocumentOrder(2);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        assertEquals(2, unionContext.getDocumentOrder());
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentContext() {
        EvalContext contexts[] = new EvalContext[0];
        UnionContext unionContext = new UnionContext(null, contexts);
    }

    @Test(expected = NullPointerException.class)
    public void testNullContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContextsArraySizeLessThanOne() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[0]);
    }

    @Test
    public void testSetPosition() {
        // Test the setPosition method with valid input
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(0);
        assertEquals(true, unionContext.nextSet()); // Check if nextSet is called
        unionContext.setPosition(1);
        assertEquals(true, unionContext.nextSet()); // Check if nextSet is called
        unionContext.setPosition(2);
        assertEquals(false, unionContext.nextSet()); // Check if nextSet returns false when position is out of bounds
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionBeforePrepare() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(0); // Position should not be set before prepare method is called
    }
}