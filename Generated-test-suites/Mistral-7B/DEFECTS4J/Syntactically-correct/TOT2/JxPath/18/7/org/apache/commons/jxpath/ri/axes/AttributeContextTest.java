import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentPosition_noNodesVisited() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_positionLessThanCurrent() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
        attributeContext.setPosition(1);
    }

    @Test
    public void testSetPosition_positionGreaterThanOrEqualCurrent() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
        attributeContext.setPosition(2);
    }

    @Test
    public void testNextNode_nodesLeft() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(null, null, null, null, null);
        NodeIterator nodeIterator = new NodeIterator(nodePointer, null, null, null, null, null);
        attributeContext.iterator = nodeIterator;
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_noNodesLeft() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextNode_nodeTestNotNodeNameTest() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, null);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(null, null, null, null, null);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertFalse(attributeContext.iterator != null);
        assertFalse(attributeContext.setStarted);
        assertEquals(0, attributeContext.getCurrentPosition());
    }
}