package org.apache.commons.jxpath.ri.axes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        attributeContext.reset();
        assertNotNull(attributeContext.getCurrentPosition());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        attributeContext.setPosition(0);
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(nodePointer);

        attributeContext.setPosition(-1);
        NodePointer nodePointerNegative = attributeContext.getCurrentNodePointer();
        assertNull(nodePointerNegative);
    }

    @Test
    public void testNextNode() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        NodePointer nodePointer = new NodePointer(new Object());
        attributeContext.iterator = new NodeIterator() {
            NodePointer nodePointer = nodePointer;
            boolean hasNext = true;

            @Override
            public NodePointer nextNode() {
                return nodePointer;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }
        };
        attributeContext.setPosition(0);
        NodePointer nodePointerNext = attributeContext.nextNode().getNodePointer();
        assertNotNull(nodePointerNext);
        assertSame(nodePointer, nodePointerNext);

        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer nextNode() {
                return null;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
        NodePointer nodePointerEmpty = attributeContext.nextNode().getNodePointer();
        assertNull(nodePointerEmpty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeContextConstructorWithNullParentContext() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeContextConstructorWithNullNodeTest() {
        AttributeContext attributeContext = new AttributeContext(null, null);
    }

    @Test
    public void testIteratorAttributeNull() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodeIterator nodeIterator = attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer nextNode() {
                return null;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
        NodePointer nodePointer = attributeContext.nextNode().getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testIteratorAttributeEmpty() {
        AttributeContext attributeContext = new AttributeContext(null, new NodeNameTest("attrName"));
        NodeIterator nodeIterator = attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer nextNode() {
                return null;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        };
        NodePointer nodePointer = attributeContext.nextNode().getNodePointer();
        assertNull(nodePointer);
    }
}