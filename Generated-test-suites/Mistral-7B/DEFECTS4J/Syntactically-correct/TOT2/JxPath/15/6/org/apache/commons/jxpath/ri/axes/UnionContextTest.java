
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {
    @Test
    public void testUnionContextConstruction() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.setPosition(1));
        // Add more assertions as needed
    }

    @Test
    public void testSetPositionNegativePosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});
        assertFalse(unionContext.setPosition(-1));
    }

    @Test
    public void testSetPositionBeyondTotalNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});
        NodeSetContext nodeSetContext = (NodeSetContext) unionContext;
        int totalNodes = nodeSetContext.getNodeSet().size();
        assertFalse(unionContext.setPosition(totalNodes + 1));
    }

    @Test(expected = NullPointerException.class)
    public void testUnionContextWithNullParentContext() {
        EvalContext[] contexts = new EvalContext[1];
        contexts[0] = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(null, contexts);
    }

    @Test(expected = NullPointerException.class)
    public void testUnionContextWithNullContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), null);
    }
}