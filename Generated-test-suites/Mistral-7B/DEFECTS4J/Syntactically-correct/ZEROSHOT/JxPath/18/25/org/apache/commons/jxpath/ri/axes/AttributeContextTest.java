package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIteratorStub;

public class AttributeContextTest {

    @org.junit.Before
    public void setUp() {
        // Initialize mocks
        EvalContext parentContext = mock(EvalContext.class);
        NodeTest nodeTest = mock(NodeNameTest.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("testAttribute"));

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @org.junit.Test
    public void testConstructor() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertNull(attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getParentContext());
    }

    @org.junit.Test
    public void testConstructorWithParentContextAndNodeTest() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        AttributeContext attributeContextWithParentContextAndNodeTest = new AttributeContext(mock(EvalContext.class), mock(NodeTest.class));
        assertNotNull(attributeContextWithParentContextAndNodeTest.getNodeTest());
        assertNull(attributeContextWithParentContextAndNodeTest.getCurrentNodePointer());
        assertNull(attributeContextWithParentContextAndNodeTest.iterator);
        assertSame(attributeContext.getClass(), attributeContextWithParentContextAndNodeTest.getClass());
    }

    @org.junit.Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.reset();
        assertNull(attributeContext.iterator);
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.getNodeTest());
        assertNull(attributeContext.getParentContext());
    }

    @org.junit.Test
    public void testSetPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(1);
        assertFalse(attributeContext.nextNode());
    }

    @org.junit.Test
    public void testNextNodeTrue() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodeIteratorStub nodeIteratorStub = new NodeIteratorStub();
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(parentContext.getCurrentNodePointer().attributeIterator(new QName("testAttribute"))).thenReturn(nodeIteratorStub);
        attributeContext.parentContext = parentContext;
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodeIteratorStub.getNodePointer()).thenReturn(nodePointer);
        when(nodePointer.getNodeName()).thenReturn(new QName("testAttribute"));
        attributeContext.nextNode();
        assertNotNull(attributeContext.iterator);
        assertNotNull(attributeContext.getCurrentNodePointer());
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testNextNodeFalse() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        attributeContext.parentContext = parentContext;
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }
}