import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {
    // Test method for UnionContext constructor
    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        Assert.assertEquals(3, unionContext.contexts.length);
    }

    // Test method for getDocumentOrder()
    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        Assert.assertEquals(1, unionContext.getDocumentOrder());
    }

    // Test method for setPosition() with valid position
    @Test
    public void testSetPositionWithValidPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        NodePointer nodePointer1 = new NodePointer(0, 0, null);
        NodePointer nodePointer2 = new NodePointer(1, 0, null);
        context1.addNode(nodePointer1);
        context1.addNode(nodePointer2);
        context2.addNode(nodePointer2);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        Assert.assertTrue(unionContext.setPosition(1));
        Assert.assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
    }

    // Test method for setPosition() with invalid position
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        NodePointer nodePointer1 = new NodePointer(0, 0, null);
        NodePointer nodePointer2 = new NodePointer(1, 0, null);
        context1.addNode(nodePointer1);
        context1.addNode(nodePointer2);
        context2.addNode(nodePointer2);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(2);
    }

    // Test method for setPosition() with empty contexts
    @Test
    public void testSetPositionWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        Assert.assertFalse(unionContext.setPosition(0));
    }

    // Test method for setPosition() with null contexts
    @Test(expected = NullPointerException.class)
    public void testSetPositionWithNullContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, null);
    }

    // Test method for setPosition() with uninitialized contexts
    @Test
    public void testSetPositionWithUninitializedContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        Assert.assertFalse(unionContext.setPosition(0));
    }
}