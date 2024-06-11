package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        // Given
        Element element = createTestElement();
        QName name = new QName("test:attr");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);

        // When
        NodePointer nodePointer = iterator.getNodePointer();
        Attr attr = (Attr) nodePointer.getNode();

        // Then
        Assert.assertNotNull(attr);
        Assert.assertEquals("test:attr", attr.getNodeName().getNodeValue());
        Assert.assertEquals(element, attr.getOwnerElement());
        Assert.assertEquals("test", attr.getNamespaceURI());

        // Given
        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();

        // Then
        attr = (Attr) nodePointer.getNode();
        Assert.assertNotNull(attr);
        Assert.assertEquals("test2:attr", attr.getNodeName().getNodeValue());
        Assert.assertEquals(element, attr.getOwnerElement());
        Assert.assertEquals("test2", attr.getNamespaceURI());

        // Given
        iterator.setPosition(-1);
        nodePointer = iterator.getNodePointer();

        // Then
        Assert.assertNull(nodePointer.getNode());

        // Given
        iterator.setPosition(attributesSize(element, name));
        nodePointer = iterator.getNodePointer();

        // Then
        attr = (Attr) nodePointer.getNode();
        Assert.assertNull(attr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullParent() {
        // Given
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test:attr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullName() {
        // Given
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
    }

    private int attributesSize(Element element, QName name) {
        int size = 0;
        if (name.getName().equals("*")) {
            NamedNodeMap namedNodeMap = element.getAttributes();
            size = namedNodeMap.getLength();
        } else {
            Attr attr = element.getAttributeNode(name.getName());
            if (attr != null) {
                size = 1;
            }
        }
        return size;
    }

    private Element createTestElement() {
        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("root");
        element.setAttributeNS("http://test.com", "test:attr", "value1");
        element.setAttributeNS("http://test.com", "test2:attr", "value2");
        return element;
    }
}