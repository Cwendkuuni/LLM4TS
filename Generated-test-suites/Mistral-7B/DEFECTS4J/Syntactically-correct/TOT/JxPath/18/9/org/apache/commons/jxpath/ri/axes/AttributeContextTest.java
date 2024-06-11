package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AttributeContextTest {

    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private Document document;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null, null);
        document = parentContext.getDocument();
        Element root = document.createElement("root");
        root.setAttribute("test", "testValue");
        document.appendChild(root);
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
    }

    @Test
    public void testNextNodeSuccess() {
        attributeContext.nextNode();
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(nodePointer);
        assertEquals("testValue", nodePointer.getNodeValue());
        assertEquals(root, nodePointer.getNode());
    }

    @Test
    public void testNextNodeMultipleAttributes() {
        Element child = document.createElement("child");
        child.setAttribute("attr1", "value1");
        child.setAttribute("attr2", "value2");
        root.appendChild(child);
        attributeContext.reset();

        attributeContext.nextNode(); // test
        assertFalse(attributeContext.nextNode()); // attr2
        attributeContext.nextNode(); // attr1
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value1", nodePointer.getNodeValue());
        assertEquals(child, nodePointer.getNode());
        
        attributeContext.nextNode(); // end of attributes
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeEmptyElement() {
        Element emptyChild = document.createElement("child");
        root.appendChild(emptyChild);
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeNoAttributes() {
        Element textChild = document.createTextNode("test");
        root.appendChild(textChild);
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeFailed() {
        attributeContext.nextNode();
        attributeContext.nextNode(); // should throw exception
    }

    @Test
    public void testSetPositionSuccess() {
        attributeContext.nextNode();
        attributeContext.setPosition(1);
        assertFalse(attributeContext.nextNode()); // test
        attributeContext.nextNode(); // attr1
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value1", nodePointer.getNodeValue());
        assertEquals(child, nodePointer.getNode());
    }

    @Test
    public void testSetPositionFailed() {
        attributeContext.nextNode();
        attributeContext.setPosition(2); // should throw exception
    }

    @Test
    public void testSetPositionEndOfNodes() {
        attributeContext.nextNode();
        attributeContext.setPosition(Integer.MAX_VALUE);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionInvalidPosition() {
        attributeContext.nextNode();
        attributeContext.setPosition(-1); // should throw exception
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }
}