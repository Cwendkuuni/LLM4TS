package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object());
        attributeContext.setCurrentNodePointer(nodePointer);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(nodePointer, currentNodePointer);
    }

    @Test
    public void testNextNode() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer nodePointer = new NodePointer(new Object());
        NodeIterator nodeIterator = new NodeIterator(new Object());
        attributeContext.setCurrentNodePointer(nodePointer);
        attributeContext.iterator = nodeIterator;
        attributeContext.nextNode();
        NodePointer nextNode = attributeContext.getCurrentNodePointer();
        assertNotNull(nextNode);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testNextNodeEmptyIterator() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator nodeIterator = new NodeIterator(new Object());
        attributeContext.iterator = nodeIterator;
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentNodePointer(new NodePointer(new Object()));
        attributeContext.iterator = new NodeIterator(new Object());
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.iterator != null);
        assertFalse(attributeContext.setStarted);
        assertFalse(attributeContext.getCurrentNodePointer() != null);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer currentNodePointer = new NodePointer(new Object());
        attributeContext.setCurrentNodePointer(currentNodePointer);
        attributeContext.iterator = new NodeIterator(new Object());
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.setPosition(10));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testNextNodeNullIterator() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testNextNodeEmptyIteratorWithException() {
        EvalContext parentContext = new EvalContext(new EvalContext(null));
        NodeTest nodeTest = new NodeNameTest("test");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator nodeIterator = new NodeIterator(new Object());
        attributeContext.iterator = nodeIterator;
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertFalse(attributeContext.nextNode());
    }
}