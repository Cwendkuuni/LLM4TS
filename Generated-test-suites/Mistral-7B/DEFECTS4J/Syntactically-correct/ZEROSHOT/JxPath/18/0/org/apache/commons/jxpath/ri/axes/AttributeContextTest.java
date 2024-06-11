package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributeContext);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);

        attributeContext.nextNode();
        currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(2);
        assertTrue(attributeContext.nextNode());
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
    }

    @Test
    public void testNextNodeFalse() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeTrue() {
        attributeContext.nextNode();
        assertTrue(attributeContext.nextNode());
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
    }
}