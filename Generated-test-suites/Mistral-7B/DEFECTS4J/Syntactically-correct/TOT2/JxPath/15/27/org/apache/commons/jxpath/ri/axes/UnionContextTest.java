package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[0]);
        Assert.assertEquals(0, unionContext.getDocumentOrder());

        UnionContext unionContext1 = new UnionContext(new EvalContext(new Object()), new EvalContext[] { new EvalContext(new Object()) });
        Assert.assertEquals(1, unionContext1.getDocumentOrder());

        UnionContext unionContext2 = new UnionContext(new EvalContext(new Object()), new EvalContext[] { new EvalContext(new Object()), new EvalContext(new Object()) });
        Assert.assertEquals(1, unionContext2.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[0]);
        unionContext.setPosition(0);
        Assert.assertFalse(unionContext.isDone());

        UnionContext unionContext1 = new UnionContext(new EvalContext(new Object()), new EvalContext[] { new EvalContext(new Object()) });
        unionContext1.setPosition(0);
        unionContext1.nextSet();
        unionContext1.nextNode();
        unionContext1.nextNode();
        Assert.assertFalse(unionContext1.isDone());

        unionContext1.setPosition(3);
        Assert.assertTrue(unionContext1.isDone());
    }

    @Test
    public void testUnionContext() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[0]);
        Assert.assertEquals(new BasicNodeSet(), unionContext.getNodeSet());

        UnionContext unionContext1 = new UnionContext(new EvalContext(new Object()), new EvalContext[] { new EvalContext(new Object()) });
        unionContext1.nextSet();
        unionContext1.nextNode();
        unionContext1.nextNode();
        Assert.assertEquals(3, unionContext1.getNodeSet().size());

        UnionContext unionContext2 = new UnionContext(new EvalContext(new Object()), new EvalContext[] { new EvalContext(new Object()), new EvalContext(new Object()) });
        unionContext2.nextSet();
        unionContext2.nextNode();
        unionContext2.nextNode();
        unionContext2.nextNode();
        unionContext2.nextNode();
        unionContext2.nextNode();
        Assert.assertEquals(6, unionContext2.getNodeSet().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextWithNullParentContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextWithEmptyContextsArray() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextWithNullContext() {
        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[] { null });
    }

    @Test(expected = RuntimeException.class)
    public void testUnionContextWithExceptionDuringIteration() {
        EvalContext context1 = new EvalContext(new Object()) {
            @Override
            public boolean nextSet() {
                throw new RuntimeException("Test exception");
            }
        };

        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[] { context1 });
    }

    @Test(expected = RuntimeException.class)
    public void testUnionContextWithExceptionDuringAddingNodes() {
        EvalContext context = new EvalContext(new Object()) {
            @Override
            public NodePointer nextNode() {
                throw new RuntimeException("Test exception");
            }
        };

        UnionContext unionContext = new UnionContext(new EvalContext(new Object()), new EvalContext[] { context });
    }
}