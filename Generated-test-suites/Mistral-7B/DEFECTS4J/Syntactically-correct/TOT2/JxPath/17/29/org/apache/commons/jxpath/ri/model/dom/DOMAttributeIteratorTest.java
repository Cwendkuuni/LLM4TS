package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.dom.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIterator() {
        NodePointer parent = new DOMPointer("root", null);
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals(parent, iterator.getNodePointer().getNodePointer().getParent());
        assertEquals(name.getName(), iterator.getNodePointer().getNodeName().getLocalPart());
    }

    @Test
    public void testTestAttr() {
        Attr attr1 = new Attr("xmlns:test", "test");
        Attr attr2 = new Attr("test", "test");
        Attr attr3 = new Attr("test:test", "test");
        Attr attr4 = new Attr("test", "Test");

        assertTrue(DOMAttributeIterator.testAttr(attr1));
        assertTrue(DOMAttributeIterator.testAttr(attr2));
        assertTrue(DOMAttributeIterator.testAttr(attr3));
        assertFalse(DOMAttributeIterator.testAttr(attr4));
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("root", "root");
        Attr attr = new Attr("test", "Test");
        element.setAttributeNode(attr);
        NodePointer parent = new DOMPointer("root", null);
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("Test", nodePointer.getNodeName().getNodeValue());
        assertEquals("Test", attr.getNodeValue());
    }
}
//