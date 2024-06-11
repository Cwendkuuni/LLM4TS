
package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.*;
import org.apache.commons.jxpath.ri.axes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertNotNull(attributeContext);
    }

    @Test
    public void testGetCurrentNodePointerNull() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        Assert.assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.getIterator());
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionLessThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        Assert.assertTrue(attributeContext.setPosition(0));
    }

    @Test
    public void testSetPositionEqualCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        Assert.assertTrue(attributeContext.setPosition(1));
        Assert.assertTrue(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPositionGreaterThanCurrentPosition() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.setPosition(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionBeyondEndOfIteration() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.setPosition(4));
    }

    @Test
    public void testNextNodeNullIterator() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeEndOfIteration() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttribute");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeAttributeNotFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttributeNotExists");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        AttributeContext parentAttributeContext = new AttributeContext(null, null);
        parentContext.setParent(parentAttributeContext);
        parentContext.setCurrentNodePointer(new NodePointer(new NodeTest("testParentNode"), null, null, 0));
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeAttributeFound() {
        EvalContext parentContext = new EvalContext(null, null);
        NodeTest nodeTest = new NodeNameTest("testAttributeName");
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        AttributeContext parentAttributeContext = new AttributeContext(null, null);
        parentContext.setParent(parentAttributeContext);
        parentContext.setCurrentNodePointer(new NodePointer(new NodeTest("testParentNode"), null, null, 0));
        NodeIterator nodeIterator = parentContext.getCurrentNodePointer().attributeIterator(new QName("testAttributeName"));
        nodeIterator.setPosition(0);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertEquals("testAttributeValue", attributeContext.getCurrentNodePointer().getStringValue());
    }

    @Test
    public void testGetCurrentPositionLessThanZero() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        Assert.assertEquals(-1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testGetCurrentPositionZero() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        Assert.assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testGetCurrentPositionOne() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        Assert.assertEquals(1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testGetCurrentPositionBeyondEndOfIteration() {
        AttributeContext attributeContext = new AttributeContext(null, null);
        attributeContext.nextNode();
        attributeContext.nextNode();
        Assert.assertEquals(2, attributeContext.getCurrentPosition());
    }
}
