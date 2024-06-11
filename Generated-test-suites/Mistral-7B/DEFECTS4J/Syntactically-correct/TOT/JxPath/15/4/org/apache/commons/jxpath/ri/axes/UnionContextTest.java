import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for UnionContext.
 */
public class UnionContextTest {

    /**
     * Test method for {@link UnionContext#UnionContext(EvalContext, EvalContext[])}.
     */
    @Test
    public void testUnionContextCreation() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new JXPathContext(new Object()), new EvalContext[] {contextA, contextB});

        Assert.assertNotNull(unionContext);
        Assert.assertTrue(unionContext.getContexts.length == 2);
        Assert.assertFalse(unionContext.isPrepared());
    }

    /**
     * Test method for {@link UnionContext#getDocumentOrder()}.
     */
    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new JXPathContext(new Object()), new EvalContext[] {contextA, contextB});

        Assert.assertTrue(unionContext.getDocumentOrder() == 1);
    }

    /**
     * Test method for {@link UnionContext#setPosition(int)}.
     */
    @Test
    public void testSetPosition() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(new JXPathContext(new Object()), new EvalContext[] {contextA, contextB});

        List<NodePointer> nodeA = new ArrayList<>();
        List<NodePointer> nodeB = new ArrayList<>();

        nodeA.add(new NodePointer("nodeA1"));
        nodeA.add(new NodePointer("nodeA2"));

        nodeB.add(new NodePointer("nodeB1"));
        nodeB.add(new NodePointer("nodeA2"));

        for (int i = 0; i < contextA.size(); i++) {
            contextA.setPosition(i);
            Assert.assertEquals(nodeA.get(i), contextA.getCurrentNodePointer());
        }

        for (int i = 0; i < contextB.size(); i++) {
            contextB.setPosition(i);
            Assert.assertEquals(nodeB.get(i), contextB.getCurrentNodePointer());
        }

        unionContext.setPosition(4); // this position is out of bounds for both contexts

        Assert.assertNull(unionContext.getCurrentNodePointer());

        unionContext.setPosition(3); // this position is valid for contextA
        Assert.assertEquals(nodeA.get(2), unionContext.getCurrentNodePointer());

        unionContext.setPosition(2); // this position is valid for contextB
        Assert.assertEquals(nodeB.get(1), unionContext.getCurrentNodePointer());
    }

    /**
     * Test method for {@link UnionContext#setPosition(int)} when contexts have different sizes.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithDifferentSizes() {
        EvalContext contextA = new EvalContext(new BasicNodeSet());
        EvalContext contextB = new EvalContext(new BasicNodeSet());

        contextA.add(new NodePointer("nodeA1"));

        UnionContext unionContext = new UnionContext(new JXPathContext(new Object()), new EvalContext[] {contextA, contextB});

        unionContext.setPosition(1);

        contextB.add(new NodePointer("nodeB1"));
    }
}