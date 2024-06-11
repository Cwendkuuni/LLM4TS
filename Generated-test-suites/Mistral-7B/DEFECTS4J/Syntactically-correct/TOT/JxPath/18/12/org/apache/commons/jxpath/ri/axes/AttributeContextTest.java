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
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link AttributeContext}.
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private AttributeContext attributeContext;
    private NodeTest nodeTest;
    private Document document;

    @Before
    public void setUp() throws Exception {
        parentContext = new EvalContext(null, null);
        nodeTest = new NodeNameTest(new QName("test:attr"));
        document = DocumentBuilderFactory.newInstance().newDocument();
        document.appendChild(document.createElement("root"));
        document.getDocumentElement().setAttribute("test:attr", "value");
        parentContext.setCurrentNodePointer(document.getDocumentElement());
    }

    @Test
    public void testNextNodeAttributeContext() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value", nodePointer.getNodeValue());
        assertNull(nodePointer.getNodeNextSibling());
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionAttributeContext() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(0);
        NodePointer nodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("value", nodePointer.getNodeValue());
        assertNull(nodePointer.getNodeNextSibling());
        attributeContext.nextNode();
        nodePointer = attributeContext.getCurrentNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testNextNodeAttributeContextWithEmptyItertor() {
        parentContext.getDocument().getDocumentElement().removeAttribute("test:attr");
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeAttributeContextWithDifferentAttributeName() {
        nodeTest = new NodeNameTest(new QName("test:differentAttr"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testResetAttributeContext() {
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }
}