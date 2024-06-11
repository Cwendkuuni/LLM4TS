package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext = new AttributeContext(null, new NodeNameTest("test"));
        NodePointer nodePointer = new NodePointer(null, null, null, 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer expectedNodePointer = nodePointer;

        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionLessThanCurrent() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionGreaterThanTotalNodes() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(100);
    }

    @Test
    public void testSetPositionEqualCurrent() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeNoNextNode() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeUninitializedIterator() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
    }
}