package org.junit.tests.AttributeContextTest;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        NodePointer expectedNodePointer = new NodePointer(1, 1, 1); // Arbitrary node pointer

        assertSame(expectedNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.nextNode();
        attributeContext.reset();

        assertFalse(attributeContext.nextNode());
        assertFalse(attributeContext.setPosition(0));
    }

    @Test(expected = AssertionError.class)
    public void testInvalidNodeTest() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, null);
        fail("Invalid NodeTest instance should cause an AssertionError.");
    }

    @Test
    public void testEmptyAttributeIterator() {
        EvalContext parentContext = new EvalContext(new NodeIterator(new NodePointer(1, 1, 1)) {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }
        });
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullIterator() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.iterator = null;
        attributeContext.nextNode();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.nextNode();
        attributeContext.setPosition(-1);
    }

    @Test
    public void testNullCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.currentNodePointer = null;
        assertEquals(null, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.nextNode();
        attributeContext.setPosition(0);
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionToCurrentPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.nextNode();
        attributeContext.setPosition(attributeContext.getCurrentPosition());
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionToOutOfBoundsPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.nextNode();
        attributeContext.setPosition(Integer.MAX_VALUE);
        assertFalse(attributeContext.nextNode());
    }
}