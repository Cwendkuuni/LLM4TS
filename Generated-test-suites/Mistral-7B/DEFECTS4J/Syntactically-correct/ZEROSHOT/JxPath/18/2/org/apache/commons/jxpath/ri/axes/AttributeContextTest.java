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
 * Tests for AttributeContext class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest attributeTest;

    @Before
    public void setup() {
        parentContext = new EvalContext(null, null);
        attributeTest = new NodeNameTest(new QName("test:attribute"));
    }

    @Test
    public void testAttributeContextConstruction() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testNextNodeWithItertorNotNull() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        NodeIterator iteratorSpy = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return expectedNodePointer;
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }
        };
        attributeContext.iterator = iteratorSpy;

        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testNextNodeWithItertorNull() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithParentContextNull() {
        AttributeContext attributeContext = new AttributeContext(null, attributeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionWithPositionLessThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.setPosition(1));
        assertSame(attributeContext.getCurrentNodePointer(), null);
    }

    @Test
    public void testSetPositionWithPositionGreaterThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        attributeContext.setPosition(10);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        NodeIterator iteratorSpy = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return expectedNodePointer;
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }
        };
        attributeContext.iterator = iteratorSpy;

        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(parentContext, attributeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.nextNode();

        attributeContext.reset();
        NodePointer expectedNodePointer = null;
        NodeIterator iteratorSpy = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return expectedNodePointer;
            }

            @Override
            public boolean setPosition(int position) {
                return true;
            }
        };
        attributeContext.iterator = iteratorSpy;

        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(actualNodePointer);
    }
}