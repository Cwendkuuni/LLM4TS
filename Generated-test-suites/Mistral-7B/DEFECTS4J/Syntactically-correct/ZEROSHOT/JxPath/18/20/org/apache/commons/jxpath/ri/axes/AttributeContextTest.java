import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testConstructor() {
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.currentNodePointer = nodePointer;

        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertSame(nodePointer, actual);
    }

    @Test
    public void testReset() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        attributeContext.currentNodePointer = Mockito.mock(NodePointer.class);

        attributeContext.reset();

        Mockito.verify(attributeContext).iterator = null;
        Mockito.verify(attributeContext).currentNodePointer = null;
        Mockito.verifyNoMoreInteractions(attributeContext);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);

        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testSetPositionWithValidPosition() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer);

        QName name = Mockito.mock(QName.class);
        Mockito.when(nodeTest.getNodeName()).thenReturn(name);

        attributeContext.nextNode();

        assertTrue(attributeContext.setPosition(1));

        NodePointer expected = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(expected);
        Mockito.when(expected.getNode()).thenReturn(Mockito.mock(Object.class));

        assertSame(expected, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithNoStarted() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithItertorNull() {
        attributeContext.iterator = null;

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithNoMoreNodes() {
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(iterator.setPosition(iterator.getPosition() + 1)).thenReturn(false);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithSuccess() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        attributeContext.iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(nodePointer);

        NodePointer expected = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.setPosition(iterator.getPosition() + 1)).thenReturn(true);
        Mockito.when(iterator.getNodePointer()).thenReturn(expected);

        currentNodePointer = Mockito.mock(NodePointer.class);
        attributeContext.currentNodePointer = currentNodePointer;

        assertSame(expected, attributeContext.getCurrentNodePointer());

        Mockito.verify(iterator).setPosition(iterator.getPosition() + 1);
        Mockito.verify(iterator).getNodePointer();
    }
}