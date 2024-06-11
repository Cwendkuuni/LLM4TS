package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AttributeContext} class
 *
 * @author John Doe
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
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());

        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer result = attributeContext.getCurrentNodePointer();
        assertEquals(nodePointer, result);
    }

    @Test
    public void testReset() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        attributeContext.setCurrentNodePointer(nodePointer);

        assertTrue(attributeContext.setPosition(0));
        assertSame(nodePointer, attributeContext.getCurrentNodePointer());

        assertTrue(attributeContext.setPosition(1));
        assertNull(attributeContext.getCurrentNodePointer());

        assertTrue(attributeContext.setPosition(100));
        assertNull(attributeContext.getCurrentNodePointer());

        assertFalse(attributeContext.setPosition(-1));
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        NodePointer nodePointer3 = new NodePointer(new Object(), 2);

        attributeContext.setCurrentNodePointer(nodePointer1);
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer(int index) {
                switch (index) {
                    case 0:
                        return nodePointer1;
                    case 1:
                        return nodePointer2;
                    case 2:
                        return nodePointer3;
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }

            @Override
            public int getSize() {
                return 3;
            }
        };

        assertFalse(attributeContext.nextNode());
        assertSame(nodePointer1, attributeContext.getCurrentNodePointer());

        assertTrue(attributeContext.nextNode());
        assertSame(nodePointer2, attributeContext.getCurrentNodePointer());

        assertTrue(attributeContext.nextNode());
        assertSame(nodePointer3, attributeContext.getCurrentNodePointer());

        assertFalse(attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }
}