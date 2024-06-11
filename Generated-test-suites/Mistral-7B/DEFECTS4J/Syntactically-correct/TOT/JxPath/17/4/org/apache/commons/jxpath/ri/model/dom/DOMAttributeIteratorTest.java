package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DOMAttributeIteratorTest {

    private Element root;
    private NodePointer rootPointer;

    @Before
    public void setUp() {
        root = createTestElement();
        rootPointer = new DOMPointer(root);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, null);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttributeIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("test"));
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());

        addAttribute(root, "test", "testValue");
        attrIteratorNext(iterator);

        NodePointer attrPointer = iterator.getNodePointer();
        assertEquals("test", attrPointer.getNodeName().getLocalPart());
        assertEquals("testValue", attrPointer.getNodeValue());

        assertEquals(1, iterator.getPosition());
        assertSame(attrPointer, iterator.getNodePointer());

        attrIteratorNext(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testMultipleAttributesIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*"));

        addAttribute(root, "test1", "testValue1");
        addAttribute(root, "test2", "testValue2");

        attrIteratorNext(iterator);
        NodePointer attrPointer1 = iterator.getNodePointer();
        assertEquals("test1", attrPointer1.getNodeName().getLocalPart());
        assertEquals("testValue1", attrPointer1.getNodeValue());

        attrIteratorNext(iterator);
        NodePointer attrPointer2 = iterator.getNodePointer();
        assertEquals("test2", attrPointer2.getNodeName().getLocalPart());
        assertEquals("testValue2", attrPointer2.getNodeValue());

        attrIteratorNext(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNullParent() {
        NodePointer parentPointer = new DOMPointer(null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, null);
    }

    @Test(expected = DOMException.class)
    public void testInvalidChildType() {
        NodePointer childPointer = new DOMPointer(createTestElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(childPointer, new QName("test"));
    }

    private void addAttribute(Element element, String name, String value) {
        if (value == null) {
            element.setAttributeNodeNS(createElement("attr", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"), null);
            Attr attr = element.setAttributeNode(createElement("attr", name, value));
            if (attr == null) {
                attr = createAttribute(element, name, value);
                element.appendChild(attr);
            }
        } else {
            Attr attr = createAttribute(element, name, value);
            element.appendChild(attr);
        }
    }

    private Node createAttribute(Element element, String name, String value) {
        try {
            return element.createAttribute(name);
        } catch (DOMException e) {
            // Ignore, if name is empty
            if (name.isEmpty()) {
                return null;
            }
            throw e;
        }
    }

    private Node createElement(String tagName, String namespaceUri, String prefix) {
        Element element = document.createElementNS(namespaceUri, tagName);
        if (prefix != null) {
            document.setDocumentElement().setPrefix(prefix, namespaceUri);
        }
        return element;
    }

    private Element createTestElement() {
        Document document = document.createDocument(null, "test", null);
        return document.getDocumentElement();
    }

    private void attrIteratorNext(DOMAttributeIterator iterator) {
        if (iterator.setPosition(1)) {
            iterator.next();
        }
    }
}