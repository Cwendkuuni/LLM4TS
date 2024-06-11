package org.junit.jxpath.test.axes;

import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;

@RunWith(JUnit4.class)
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Test
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest("testAttribute");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testSetPositionEdgeCases() {
        int position = -5;
        assertFalse(attributeContext.setPosition(position));

        int totalNodes = 10;
        for (int i = 0; i < totalNodes; i++) {
            nextNode(attributeContext);
        }
        int positionTooLarge = totalNodes + 5;
        assertFalse(attributeContext.setPosition(positionTooLarge));
    }

    @Test
    public void testNextNode() {
        // Test default initialization
        assertNull(attributeContext.getCurrentNodePointer());

        // Test nextNode() when there are no nodes
        assertFalse(attributeContext.nextNode());

        // Test nextNode() when there are nodes
        int totalNodes = 5;
        for (int i = 0; i < totalNodes; i++) {
            nextNode(attributeContext);
            NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
            assertNotNull(currentNodePointer);
            assertNotNull(currentNodePointer.getNode());
        }

        // Test nextNode() after reaching the end of the nodes
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeException() {
        // Test nextNode() when the iterator is not initialized
        attributeContext.reset();
        nextNode(attributeContext);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testReset() {
        int totalNodes = 5;
        for (int i = 0; i < totalNodes; i++) {
            nextNode(attributeContext);
        }
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    private void nextNode(AttributeContext attributeContext) {
        attributeContext.nextNode();
    }

    @Test
    public void tearDown() {
        attributeContext = null;
        parentContext = null;
        nodeTest = null;
    }
}