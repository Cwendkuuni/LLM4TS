package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Arguments;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        nodeTest.getNodeName().setValue("testAttribute");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer expectedNodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(attributeContext.iterator.getNodePointer()).thenReturn(expectedNodePointer);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition_validPosition() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getAttributeIterator(Mockito.any(QName.class)))
            .thenReturn(Mockito.mock(NodeIterator.class));
        attributeContext.setPosition(1);
        assertTrue(attributeContext.iterator.isPositionValid());
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(actualNodePointer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_invalidPosition() {
        attributeContext.setPosition(-1);
    }

    @Test
    public void testNextNode_noAttributeFound() {
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        attributeContext.nextNode();
        assertFalse(attributeContext.iterator.isPositionValid());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_attributeFound() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getAttributeIterator(Mockito.any(QName.class)))
            .thenReturn(Mockito.mock(NodeIterator.class));
        NodePointer expectedNodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(attributeContext.iterator.getNodePointer()).thenReturn(expectedNodePointer);
        attributeContext.nextNode();
        assertEquals(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testIsSetStarted_notStarted() {
        assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testIsSetStarted_started() {
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
    }
}