
package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        NodePointer nodePointer1 = new NodePointer(1, "root", 0); // root node
        NodePointer nodePointer2 = new NodePointer(1, "root", 1); // first child node
        NodePointer nodePointer3 = new NodePointer(1, "root", 2); // second child node

        context1.setCurrentNodePointer(nodePointer1);
        context2.setCurrentNodePointer(nodePointer2);

        UnionContext unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);

        // Test basic functionality
        assertEquals(nodePointer1, unionContext.getNodeAt(0));
        assertEquals(nodePointer2, unionContext.getNodeAt(1));
        assertEquals(nodePointer3, unionContext.getNodeAt(2));

        // Test edge cases
        assertEquals(nodePointer3, unionContext.getNodeAt(unionContext.size() - 1));
        assertEquals(nodePointer2, unionContext.getNodeAt(unionContext.size() - 2));
        assertEquals(nodePointer1, unionContext.getNodeAt(unionContext.size() - 3));

        // Test exceptions
        try {
            unionContext.getNodeAt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            unionContext.getNodeAt(unionContext.size());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            unionContext = new UnionContext(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}