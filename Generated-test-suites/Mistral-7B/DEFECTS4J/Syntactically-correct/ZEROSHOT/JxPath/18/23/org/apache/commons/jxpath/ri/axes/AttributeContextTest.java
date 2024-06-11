package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @Mock
    private EvalContext parentContext;

    @Mock
    private NodeTest nodeTest;

    @InjectMocks
    private AttributeContext attributeContext;

    @Captor
    private ArgumentCaptor<NodeNameTest> nodeNameTestCaptor;

    private NodePointer nodePointer;
    private NodeIterator nodeIterator;

    @Before
    public void setUp() {
        nodePointer = mock(NodePointer.class);
        nodeIterator = mock(NodeIterator.class);
    }

    @Test
    public void testAttributeContextConstructor() {
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertEquals(parentContext, attributeContext.getParentContext());
        verify(parentContext).addChildContext(attributeContext);
        verify(parentContext).addChildContext(eq(attributeContext), times(1));
        verify(parentContext).setCurrentNodePointer(nodePointer);
        verify(parentContext, times(1)).setAttributeContext(attributeContext);
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testReset() {
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        attributeContext.reset();
        verify(iterator).close();
        verify(iterator, never()).setPosition(anyInt());
        verify(parentContext).reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when(iterator.getSize()).thenReturn(3);
        when(iterator.setPosition(anyInt())).thenReturn(true);

        attributeContext.setPosition(1);
        verify(iterator, times(1)).setPosition(1);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(4);
        verify(iterator, times(2)).setPosition(anyInt());
        assertFalse(attributeContext.getCurrentNodePointer() instanceof NodePointer);
    }

    @Test
    public void testNextNode() {
        when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        when((NodeNameTest) nodeTest.getNodeName()).thenReturn(new QName("test"));
        when(nodePointer.getAttributeIterator("test")).thenReturn(nodeIterator);
        when(nodeIterator.getSize()).thenReturn(3);
        when(nodeIterator.getNodePointer(anyInt())).thenReturn(nodePointer);

        attributeContext.nextNode();
        verify(iterator, times(1)).setPosition(anyInt() + 1);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        verify(iterator, times(2)).setPosition(anyInt() + 1);
        assertEquals(nodePointer, attributeContext.getCurrentNodePointer());

        attributeContext.nextNode();
        verify(iterator, times(3)).setPosition(anyInt() + 1);
        assertFalse(attributeContext.getCurrentNodePointer() instanceof NodePointer);
    }
}