package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link AttributeContext} class.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest("data", null);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    /**
     * Test nextNode method with valid attributes.
     */
    @Test
    public void testNextNodeWithValidAttributes() {
        Element root = newElement("root", "data", "value1");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.nextNode();
        NodePointer attributeNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(attributeNodePointer);
        assertEquals("value1", attributeNodePointer.getNodeName());
        assertEquals(Node.ATTRIBUTE_NODE, attributeNodePointer.getNodeType());

        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test nextNode method with no attributes.
     */
    @Test
    public void testNextNodeWithNoAttributes() {
        Element root = newElement("root");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.nextNode();
        assertFalse(attributeContext.hasNext());
    }

    /**
     * Test nextNode method with different attribute names.
     */
    @Test
    public void testNextNodeWithDifferentAttributeNames() {
        Element root = newElement("root", "data1", "value1");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.nextNode();
        NodePointer attributeNodePointer1 = attributeContext.getCurrentNodePointer();
        assertEquals("value1", attributeNodePointer1.getNodeName());

        attributeContext.nextNode();
        NodeNameTest nodeTest2 = new NodeNameTest("data2", null);
        AttributeContext attributeContext2 = new AttributeContext(attributeContext, nodeTest2);
        attributeContext2.nextNode();
        NodePointer attributeNodePointer2 = attributeContext2.getCurrentNodePointer();
        assertNull(attributeNodePointer2);
    }

    /**
     * Test setPosition method with valid positions.
     */
    @Test
    public void testSetPositionWithValidPositions() {
        Element root = newElement("root", "data1", "value1");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.setPosition(0);
        NodePointer attributeNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value1", attributeNodePointer.getNodeName());

        attributeContext.nextNode();
        attributeContext.setPosition(1);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    /**
     * Test setPosition method with invalid positions.
     */
    @Test
    public void testSetPositionWithInvalidPositions() {
        Element root = newElement("root", "data1", "value1");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.setPosition(-1);
        assertFalse(attributeContext.hasNext());

        attributeContext.nextNode();
        attributeContext.setPosition(Integer.MAX_VALUE);
        assertFalse(attributeContext.hasNext());
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
        Element root = newElement("root", "data1", "value1");
        NodePointer rootNodePointer = new NodePointer(root);
        parentContext.setCurrentNodePointer(rootNodePointer);

        attributeContext.nextNode();
        NodePointer attributeNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value1", attributeNodePointer.getNodeName());

        attributeContext.reset();
        assertFalse(attributeContext.hasNext());
    }

    private Element newElement(String tagName, String... attributeNamesAndValues) {
        Element element = document.createElement(tagName);
        for (String attributeNameAndValue : attributeNamesAndValues) {
            String[] parts = attributeNameAndValue.split("=");
            String attributeName = parts[0];
            String attributeValue = parts.length > 1 ? parts[1] : "";
            element.setAttribute(attributeName, attributeValue);
        }
        return element;
    }
}