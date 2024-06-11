package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertNotNull(attributeContext);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.setCurrentNodePointer(nodePointer);
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPosition() {
        attributeContext.setPosition(0);
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.setCurrentNodePointer(nodePointer);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getAttributeIterator(Mockito.any(QName.class))).thenReturn(Mockito.mock(NodeIterator.class));
        attributeContext.setPosition(1);
        assertFalse(attributeContext.getCurrentNodePointer().equals(nodePointer));
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        Mockito.verify(nodePointer).getAttributeIterator(Mockito.any(QName.class));
        Mockito.verify(nodePointer, Mockito.times(2)).getAttributeIterator(Mockito.any(QName.class));
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getAttributeIterator(Mockito.any(QName.class))).thenReturn(Mockito.mock(NodeIterator.class));
        attributeContext.nextNode();
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
        Mockito.verify(nodePointer).getAttributeIterator(Mockito.any(QName.class));
        Mockito.verify(nodePointer, Mockito.times(2)).getAttributeIterator(Mockito.any(QName.class));
    }

    @Test
    public void testIsStarted() {
        assertFalse(attributeContext.isStarted());
        attributeContext.nextNode();
        assertTrue(attributeContext.isStarted());
    }

    @Test
    public void testGetIterator() {
        NodeIterator nodeIterator = Mockito.mock(NodeIterator.class);
        attributeContext.setIterator(nodeIterator);
        assertSame(nodeIterator, attributeContext.getIterator());
    }
}