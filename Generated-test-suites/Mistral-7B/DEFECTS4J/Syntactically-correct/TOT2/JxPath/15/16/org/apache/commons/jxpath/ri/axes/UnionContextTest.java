import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        int documentOrder = unionContext.getDocumentOrder();
        assertTrue(documentOrder > 0);
    }

    // Test cases for setPosition method
    @Test
    public void testUnionContextSetPositionValid() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        for (int i = 0; i < context1.size(); i++) {
            context1.nextSet();
            context1.nextNode();
        }
        for (int i = 0; i < context2.size(); i++) {
            context2.nextSet();
            context2.nextNode();
        }

        unionContext.setPosition(0);
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        NodePointer nodePointer2 = context1.getCurrentNodePointer();
        assertEquals(nodePointer1, nodePointer2);

        unionContext.nextNode();
        NodePointer nodePointer3 = unionContext.getCurrentNodePointer();
        NodePointer nodePointer4 = context2.getCurrentNodePointer();
        assertEquals(nodePointer3, nodePointer4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnionContextSetPositionInvalid() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        for (int i = 0; i < context1.size(); i++) {
            context1.nextSet();
            context1.nextNode();
        }
        for (int i = 0; i < context2.size(); i++) {
            context2.nextSet();
            context2.nextNode();
        }

        unionContext.setPosition(unionContext.size());
    }

    // Expected error when contexts is null
    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNullContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, null);
    }

    // Expected error when contexts contains null
    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNullContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[2];
        contexts[0] = new EvalContext(new BasicNodeSet());
        contexts[1] = null;
        UnionContext unionContext = new UnionContext(parentContext, contexts);
    }

    // Test case: UnionContext with empty contexts
    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertTrue(unionContext.getNodeSet().isEmpty());
    }
}