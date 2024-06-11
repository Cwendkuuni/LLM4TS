package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @Mock
    private EvalContext parentContext;

    @Mock
    private NodeTest nodeTest;

    @InjectMocks
    private AttributeContext attributeContext;

    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        when(nodeTest.accept(mock(NodePointer.class))).thenReturn(true);
    }

    @Test
    public void testAttributeContextConstructor() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getNodeTest());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
    }

    @Test
    public void testSetPositionWhenPositionIsLessThanCurrentPosition() {
        attributeContext.setPosition(0);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenStarted() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWhenIteratorIsEmpty() {
        attributeContext.setPosition(0);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName()))
                .thenReturn(mock(NodeIterator.class));
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWhenIteratorHasNoMoreNodes() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        when(iterator.setPosition(iterator.getPosition() + 1))
                .thenReturn(false);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        iterator = mock(NodeIterator.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest.getNodeName()))
                .thenReturn(iterator);
        when(iterator.setPosition(iterator.getPosition() + 1))
                .thenReturn(true);
        currentNodePointer = mock(NodePointer.class);
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        when(nodeTest.accept(currentNodePointer)).thenReturn(true);
        when(currentNodePointer.getNodeType()).thenReturn(NodePointer.ELEMENT_NODE);
        attributeContext.nextNode();
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testIsSetStartedIsFalseBeforeNextNodeIsCalled() {
        assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testIsSetStartedIsTrueAfterNextNodeIsCalled() {
        attributeContext.nextNode();
        assertTrue(attributeContext.isSetStarted());
    }

    @Test
    public void testGetIterator() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertNotNull(attributeContext.getIterator());
    }

    private boolean isSetStarted() {
        return attributeContext.isSetStarted;
    }
}