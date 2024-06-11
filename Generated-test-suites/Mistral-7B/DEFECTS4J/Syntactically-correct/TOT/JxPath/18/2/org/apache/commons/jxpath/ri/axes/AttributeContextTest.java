package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test class for AttributeContext.
 */
public class AttributeContextTest {

    /**
     * Test nextNode method with valid node test.
     */
    @Test
    public void testNextNodeWithValidNodeTest() {
        Document document = createDocument();
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("testAttr")));
        attributeContext.reset();
        attributeContext.setPosition(0);

        NodePointer expectedNodePointer = document.getDocumentElement().getAttributes().getNamedItem("testAttr");
        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertSame(expectedNodePointer, actualNodePointer);
    }

    /**
     * Test nextNode method with invalid node test.
     */
    @Test
    public void testNextNodeWithInvalidNodeTest() {
        Document document = createDocument();
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeTest() {
            @Override
            public boolean matches(NodePointer nodePointer, EvalContext evalContext) {
                throw new UnsupportedOperationException();
            }
        };
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        attributeContext.setPosition(0);

        assertNull(attributeContext.nextNode());
    }

    /**
     * Test setPosition method with valid position.
     */
    @Test
    public void testSetPositionWithValidPosition() {
        Document document = createDocument();
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("testAttr")));
        attributeContext.reset();
        attributeContext.setPosition(1);

        NodePointer expectedNodePointer = document.getDocumentElement().getAttributes().getNamedItem("testAttr2");
        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();

        assertSame(expectedNodePointer, actualNodePointer);
    }

    /**
     * Test setPosition method with invalid position.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithInvalidPosition() {
        Document document = createDocument();
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("testAttr")));
        attributeContext.reset();
        attributeContext.setPosition(1);

        attributeContext.nextNode();
        attributeContext.setPosition(-1);
    }

    /**
     * Test reset method.
     */
    @Test
     public void testReset() {
        Document document = createDocument();
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("testAttr")));
        attributeContext.reset();

        assertNull(attributeContext.getCurrentNodePointer());
        assertNull(attributeContext.iterator);
    }

    private Document createDocument() {
        Document document = DocumentBuilderFactory.newInstance().newDocument();
        Element root = document.createElement("root");
        root.setAttribute("testAttr", "testValue1");
        root.setAttribute("testAttr2", "testValue2");
        document.appendChild(root);
        return document;
    }
}