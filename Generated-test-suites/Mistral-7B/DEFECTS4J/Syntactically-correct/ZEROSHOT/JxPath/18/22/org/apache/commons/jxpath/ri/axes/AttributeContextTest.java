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
    public void testConstructor() {
        assertNotNull(attributeContext);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.isStarted());
        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        // Test setting to a position before the start
        assertFalse(attributeContext.setPosition(0));

        // Test setting to a position equal to the start
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(1));
        assertEquals(attributeContext.getCurrentNodePointer(), attributeContext.getCurrentNodePointer());

        // Test setting to a position after the start
        attributeContext.nextNode();
        assertTrue(attributeContext.setPosition(2));
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        // Test nextNode before starting
        assertFalse(attributeContext.nextNode());

        // Test nextNode after starting but before reaching the end
        attributeContext.nextNode();
        NodePointer expectedCurrentNodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(expectedCurrentNodePointer);
        Mockito.when(iterator.hasNext()).thenReturn(true);
        NodePointer actualCurrentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedCurrentNodePointer, actualCurrentNodePointer);

        // Test nextNode after reaching the end
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}