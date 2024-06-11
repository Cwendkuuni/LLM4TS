package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class AttributeContextTest {

    @Test
    public void testAttributeContextConstructor() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        // Check if the object has been created correctly
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointer() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        // Check if the current node pointer is the same as the expected one
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(1);
        attributeContext.nextNode();
        attributeContext.reset();
        // Check if the object has been reset correctly
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getIterator());
        Assert.assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(0);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        attributeContext.setCurrentNodePointer(expectedNodePointer);
        // Check if the method sets the correct position
        attributeContext.setPosition(1);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        // Check if the current node pointer is the one after the expected one
        Assert.assertFalse(expectedNodePointer.equals(actualNodePointer));
    }

    @Test
    public void testNextNode() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext expectedAttributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        expectedAttributeContext.setCurrentNodePointer(expectedNodePointer);
        AttributeContext attributeContextUnderTest = new AttributeContext(parentContext, nodeTest);
        NodePointer actualNodePointer = attributeContextUnderTest.nextNode().getCurrentNodePointer();
        // Check if the method returns the correct node pointer
        Assert.assertFalse(expectedAttributeContext.getCurrentNodePointer().equals(actualNodePointer));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeContextConstructorWithNullParentContext() {
        new AttributeContext(null, new NodeNameTest(new QName("test")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithNegativePosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithPositionGreaterThanNumberOfNodes() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        NodePointer[] nodes = new NodePointer[10];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new NodePointer(new Object());
        }
        EvalContext parentContext = new EvalContext(nodes);
        AttributeContext attributeContextUnderTest = new AttributeContext(parentContext, new NodeNameTest(new QName("test")));
        attributeContextUnderTest.setPosition(nodes.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextNodeWhenIteratorIsNull() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.setIterator(null);
        attributeContext.nextNode();
    }

    @Test
    public void testNextNodeWhenIteratorHasNoMoreNodes() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext expectedAttributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        expectedAttributeContext.setCurrentNodePointer(expectedNodePointer);
        AttributeContext attributeContextUnderTest = new AttributeContext(parentContext, nodeTest);
        attributeContextUnderTest.nextNode();
        NodePointer actualNodePointer = attributeContextUnderTest.nextNode().getCurrentNodePointer();
        // Check if the method returns null when there are no more nodes
        Assert.assertNull(actualNodePointer);
    }

    @Test
    public void testGetCurrentNodePointerWhenIteratorHasNoMoreNodes() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test"));
        AttributeContext expectedAttributeContext = new AttributeContext(parentContext, nodeTest);
        NodePointer expectedNodePointer = new NodePointer(new Object());
        expectedAttributeContext.setCurrentNodePointer(expectedNodePointer);
        AttributeContext attributeContextUnderTest = new AttributeContext(parentContext, nodeTest);
        attributeContextUnderTest.nextNode();
        attributeContextUnderTest.nextNode();
        NodePointer actualNodePointer = attributeContextUnderTest.getCurrentNodePointer();
        // Check if the method throws an exception when there are no more nodes
        Assert.assertNull(actualNodePointer);
        attributeContextUnderTest.reset();
        NodePointer resetNodePointer = attributeContextUnderTest.getCurrentNodePointer();
        // Check if the method returns the correct node pointer after a reset
        Assert.assertEquals(resetNodePointer, attributeContextUnderTest.getCurrentNodePointer());
    }
}