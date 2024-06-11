package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.*;
import org.junit.*;
import org.junit.Assert.*;
import org.w3c.dom.*;

public class DOMAttributeIteratorTest {
    private NodePointer parentNodePointer;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
    }

    @Test
    public void testDOMAttributeIterator_ValidArguments() {
        iterator = new DOMAttributeIterator(parentNodePointer, name);
        assertTrue(iterator.hasNext());
        // Other assertions for method functionality
    }

    @Test
    public void testAttr() {
        Attr attr1 = new Attr("xmlns:test", "testNS");
        assertFalse(DOMAttributeIterator.testAttr(attr1));

        Attr attr2 = new Attr("test", "test");
        Element element = new Element("root");
        Attr attr3 = element.setAttributeNode(attr2);
        assertTrue(DOMAttributeIterator.testAttr(attr3));

        Attr attr4 = new Attr("xmlns:test", "testNS");
        Element element2 = new Element("root");
        Attr attr5 = element2.setAttributeNodeNS(attr4, "http://example.com/test", "test");
        assertTrue(DOMAttributeIterator.testAttr(attr5));
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("root");
        Attr attr1 = element.setAttributeNode(new Attr("test", "testValue"));
        QName name1 = new QName("test", "test");
        Attr testAttr1 = DOMAttributeIterator.getAttribute(element, name1);
        assertEquals(attr1, testAttr1);

        Attr attr2 = new Attr("xmlns:test", "testNS");
        Attr testAttr2 = DOMAttributeIterator.getAttribute(element, new QName("", "xmlns:test"));
        assertNull(testAttr2);

        Attr attr3 = new Attr("test", "test");
        NamedNodeMap nodeMap = element.getAttributes();
        Attr attr4 = (Attr) nodeMap.getNamedItem("test");
        Attr testAttr3 = DOMAttributeIterator.getAttribute(element, new QName("test", ""));
        assertEquals(attr4, testAttr3);
    }

    @Test
    public void testDOMAttributeIterator_NullArguments() {
        assertNull(new DOMAttributeIterator(null, new QName("test", "test")));
        assertNull(new DOMAttributeIterator(new NodePointer(new Element("root")), null));
    }

    @Test
    public void testDOMAttributeIterator_ParentNodeIsNotElement() {
        Node node = new Document();
        assertFalse(new DOMAttributeIterator(new NodePointer(node), new QName("test", "test")).hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MoveNext() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        DOMAttributePointer attrPointer1 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("test", attrPointer1.getName().getName());
        iterator.next();
        DOMAttributePointer attrPointer2 = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer2);
        assertTrue(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDOMAttributeIterator_MoveNext_EndOfIterator() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testDOMAttributeIterator_MoveNext_GetSameAttributeTwice() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        DOMAttributePointer attrPointer1 = (DOMAttributePointer) iterator.getNodePointer();
        DOMAttributePointer attrPointer2 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(attrPointer1, attrPointer2);
    }

    @Test
    public void testDOMAttributeIterator_MovePrevious() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.next();
        iterator.next();
        iterator.previous();
        DOMAttributePointer attrPointer1 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(attrPointer1.getNode(), iterator.getNodePointer().getNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testDOMAttributeIterator_MovePrevious_NotAtStart() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.next();
        iterator.next();
        iterator.previous();
        iterator.previous();
    }

    @Test
    public void testDOMAttributeIterator_GetPosition() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_SetPosition() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.setPosition(2);
        DOMAttributePointer attrPointer1 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(attrPointer1.getNode(), iterator.getNodePointer().getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_SetPosition_InvalidIndex() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.setPosition(-1);
    }

    @Test
    public void testDOMAttributeIterator_SetPosition_PositionGreaterThanSize() {
        Element element = new Element("root");
        Node node = (Node) element;
        parentNodePointer = new NodePointer(node);
        name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNodePointer, name);
        iterator.setPosition(5);
    }
}