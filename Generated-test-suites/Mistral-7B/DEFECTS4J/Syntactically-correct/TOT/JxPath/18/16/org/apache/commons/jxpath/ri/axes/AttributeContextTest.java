package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.Axis;
import org.apache.commons.jxpath.ri.model.ContextPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AttributeContextTest {

    private NodeTest nodeTest;
    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        nodeTest = mock(NodeTest.class);
        parentContext = mock(EvalContext.class);
        when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);
        attributeContext = new AttributeContext(parentContext, nodeTest);
        currentNodePointer = mock(NodePointer.class);
    }

    @Test
    public void testNextNodeWithValidAttributeName() {
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        when(iterator.setPosition(anyInt())).thenReturn(true);
        when(iterator.hasNext()).thenReturn(true);

        attributeContext.nextNode();

        verify(iterator, times(1)).next();
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithEmptyAttributeIterator() {
        when(iterator.getNodePointer()).thenReturn(null);
        when(iterator.setPosition(anyInt())).thenReturn(false);

        attributeContext.nextNode();

        verify(iterator, times(0)).next();
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        when(getCurrentPosition()).thenReturn(0);

        attributeContext.setPosition(1);

        verify(parentContext, times(1)).setPosition(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithNegativePosition() {
        when(getCurrentPosition()).thenReturn(0);

        attributeContext.setPosition(-1);
    }

    @Test
    public void testReset() {
        attributeContext.reset();

        verify(parentContext, times(1)).reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testHasNextWithValidIterator() {
        when(iterator.hasNext()).thenReturn(true);

        attributeContext.nextNode();

        Assert.assertTrue(attributeContext.hasNext());
    }

    @Test
    public void testHasNextWithEmptyIterator() {
        when(iterator.hasNext()).thenReturn(false);

        attributeContext.nextNode();

        Assert.assertFalse(attributeContext.hasNext());
    }

    @Test
    public void testIsSetStartedWithInitiallyFalse() {
        Assert.assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testIsSetStartedWithInitiallyTrue() {
        attributeContext.setStarted = true;

        Assert.assertTrue(attributeContext.isSetStarted());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        attributeContext.setPosition(Integer.MAX_VALUE);

        verify(parentContext, never()).setPosition(Integer.MAX_VALUE);
    }

    @Test
    public void testNextNodeWithSameAttributeNameButDifferentAxis() {
        AttributeContext axisContext = new AttributeContext(parentContext, new NodeNameTest(Axis.ATTRIBUTE, "test"));

        Assert.assertNotSame(attributeContext, axisContext);
        Assert.assertFalse(axisContext.nextNode());
    }
}