import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {
    @Test
    public void testUnionContext() {
        // Given
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        NodeSetContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        // Tests from all experts

        // Given: Empty contexts
        unionContext = new UnionContext(parentContext, new EvalContext[] {});
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Single context
        context1 = new EvalContext(new BasicNodeSet());
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1 });
        unionContext.setPosition(0);
        assertTrue(unionContext.getCurrentNodePointer() != null);

        // Given: Adding nodes to contexts
        NodePointer nodePointer1 = new NodePointer("node1");
        context1.addNode(nodePointer1);
        context1.nextSet();
        context1.nextNode();
        unionContext.setPosition(0);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer1));

        // Given: Adding nodes with the same NodePointer to contexts
        NodePointer nodePointerSame = new NodePointer("node1");
        context1.addNode(nodePointerSame);
        context1.nextSet();
        context1.nextNode();
        context2.addNode(nodePointerSame);
        context2.nextSet();
        context2.nextNode();
        unionContext.setPosition(1);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointerSame));

        // Given: Adding null nodes to contexts
        context1.addNode(null);
        context1.nextSet();
        context1.nextNode();
        unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        unionContext.setPosition(0);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Adding nodes with null NodePointer to contexts
        NodePointer nodePointerNull = null;
        context2.addNode(nodePointerNull);
        context2.nextSet();
        context2.nextNode();
        unionContext.setPosition(1);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Contexts with different document orders
        NodePointer nodePointer3 = new NodePointer("node3");
        context1.addNode(nodePointer3);
        context1.nextSet();
        context1.nextNode();
        context2.addNode(nodePointer3);
        context2.nextSet();
        context2.nextNode();
        unionContext.setPosition(1);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer3));

        // Given: Adding nodes to contexts with duplicates
        NodePointer nodePointer2 = new NodePointer("node2");
        context2.addNode(nodePointer2);
        context2.nextSet();
        context2.nextNode();
        context1.addNode(nodePointer2);
        unionContext.setPosition(1);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer2));

        // Given: Setting position beyond the last node in the union context
        unionContext.setPosition(2);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Setting position with a negative number
        unionContext.setPosition(-1);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Setting position with a number greater than the total number of nodes
        unionContext.setPosition(3);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Setting position with empty contexts
        unionContext = new UnionContext(parentContext, new EvalContext[] {});
        unionContext.setPosition(0);
        assertNull(unionContext.getCurrentNodePointer());

        // Given: Setting position in the union context with null contexts
        unionContext = new UnionContext(parentContext, null);
        assertNull(unionContext.getCurrentNodePointer());
    }
}