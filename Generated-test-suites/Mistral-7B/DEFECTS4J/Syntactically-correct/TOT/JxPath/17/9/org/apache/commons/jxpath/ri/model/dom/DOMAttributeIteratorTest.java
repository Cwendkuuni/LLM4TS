package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_SimpleElement() {
        Document doc = createSimpleDocument();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, doc.getDocumentElement()), new QName("*"));

        for (int i = 0; i < 5; i++) {
            NodePointer currentNode = iterator.getNodePointer();
            Assert.assertNotNull("Failed to get NodePointer", currentNode);
            Assert.assertTrue("Unexpected NodeType: " + currentNode.getNodeType(), currentNode.getNodeType() == Node.ATTRIBUTE_NODE);
            Assert.assertNotNull("Unexpected null Attribute", currentNode.getNode());
            Assert.assertEquals("Unexpected attribute name", ((Attr) currentNode.getNode()).getNodeName(), currentNode.getName().getName());
            iterator.next();
        }
        Assert.assertFalse("Iterator should be exhausted after 5 steps", iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceElement() {
        Document doc = createNamespaceDocument();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, doc.getDocumentElement()), new QName("{http://test.com}myAttribute"));

        for (int i = 0; i < 3; i++) {
            NodePointer currentNode = iterator.getNodePointer();
            Assert.assertNotNull("Failed to get NodePointer", currentNode);
            Assert.assertTrue("Unexpected NodeType: " + currentNode.getNodeType(), currentNode.getNodeType() == Node.ATTRIBUTE_NODE);
            Assert.assertNotNull("Unexpected null Attribute", currentNode.getNode());
            Assert.assertEquals("Unexpected attribute name", ((Attr) currentNode.getNode()).getNodeName(), currentNode.getName().getName());
            iterator.next();
        }
        Assert.assertFalse("Iterator should be exhausted after 3 steps", iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyElement() {
        Document doc = createEmptyDocument();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc, doc.getDocumentElement()), new QName("*"));
        Assert.assertFalse("Iterator should be exhausted", iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullParent() {
        new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullName() {
        new DOMAttributeIterator(new DOMPointer(createSimpleDocument(), createDocumentElement("root", "root")), null);
    }

    private Document createSimpleDocument() {
        Document doc = createDocument();
        Element root = createDocumentElement("root", "root");
        doc.appendChild(root);
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        return doc;
    }

    private Document createNamespaceDocument() {
        Document doc = createDocument();
        doc.setXmlNamespaceAware(true);
        Element root = createDocumentElementNS("{http://test.com}", "root", "root");
        doc.appendChild(root);
        root.setAttributeNS("{http://test.com}", "myAttribute", "value3");
        root.setAttributeNS("{http://test.com}", "myAttribute2", "value4");
        return doc;
    }

    private Document createEmptyDocument() {
        Document doc = createDocument();
        Element root = createDocumentElement("root", "root");
        doc.appendChild(root);
        return doc;
    }

    private Document createDocument() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        return doc;
    }

    private Element createDocumentElement(String tagName, String tagValue) {
        Element element = doc.createElement(tagName);
        element.setTextContent(tagValue);
        return element;
    }
}