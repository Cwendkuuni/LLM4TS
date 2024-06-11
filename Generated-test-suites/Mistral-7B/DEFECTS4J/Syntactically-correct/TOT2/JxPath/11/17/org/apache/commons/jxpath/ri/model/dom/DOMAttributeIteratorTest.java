import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.QName;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructor() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());

        // Test constructor with null arguments
        parent = null;
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator);

        // Test constructor with empty QName
        name = new QName("", "", null);
        iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null), new QName("test", null, null));
        QName result = iterator.getName();
        assertEquals(new QName("test", null, null), result);
    }

    @Test
    public void testHasNext() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null), new QName("test", null, null));
        assertFalse(iterator.hasNext());

        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        iterator = new DOMAttributeIterator(parent, name);
        Node node = new Element("test");
        parent.setNode(node);
        Attr attr = node.createAttribute("test");
        node.setAttributeNode(attr);
        iterator.next();
        assertTrue(iterator.hasNext());

        // Test hasNext() when there are no attributes
        parent.setNode(null);
        iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());

        Node node = new Element("test");
        parent.setNode(node);
        Attr attr = node.createAttribute("test");
        node.setAttributeNode(attr);
        DOMAttributePointer result = (DOMAttributePointer) iterator.next();
        assertEquals(parent, result.getParent());
        assertEquals(attr, result.getNode());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPosition() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(5);
    }

    @Test
    public void testTestAttr() {
        QName name = new QName("", "xmlns:test", null);
        String prefix = "xmlns:test";
        String localName = "test";
        NodePointer parent = new NodePointer(null, null);
        Attr attr = new Attr("test", "test");
        Element element = new Element("test");
        parent.setNode(element);
        element.setAttributeNode(attr);
        assertTrue(DOMAttributeIterator.testAttr(attr, name));

        name = new QName("", "test", null);
        parent = new NodePointer(null, null);
        Attr attr2 = new Attr("test", "test");
        Element element2 = new Element("test");
        parent.setNode(element2);
        element2.setAttributeNode(attr2);
        assertFalse(DOMAttributeIterator.testAttr(attr2, name));

        name = new QName("test", null, null);
        parent = new NodePointer(null, null);
        Attr attr3 = new Attr("test", "test");
        Element element3 = new Element("test");
        parent.setNode(element3);
        element3.setAttributeNode(attr3);
        assertTrue(DOMAttributeIterator.testAttr(attr3, name));

        name = new QName("xmlns:test", null, null);
        String namespaceURI = "http://example.com/test";
        parent = new NodePointer(null, null);
        Attr attr4 = new Attr("test", "test");
        Element element4 = new Element("test");
        parent.setNamespaceURI("http://example.com/test");
        parent.setNode(element4);
        element4.setAttributeNode(attr4);
        assertTrue(DOMAttributeIterator.testAttr(attr4, name));

        parent.setNamespaceURI(null);
        assertFalse(DOMAttributeIterator.testAttr(attr4, name));
    }

    @Test
    public void testgetAttribute() {
        NodePointer parent = new NodePointer(null, null);
        QName name = new QName("", "test", null);
        Element element = new Element("test");
        parent.setNode(element);
        Attr attr = new Attr("test", "test");
        parent.setNamespaceURI("http://example.com/test");
        element.setAttributeNode(attr);
        assertEquals(attr, DOMAttributeIterator.getAttribute(element, name));

        parent.setNamespaceURI(null);
        Attr attr2 = new Attr("test", "test");
        element.setAttributeNode(attr2);
        assertEquals(attr2, DOMAttributeIterator.getAttribute(element, name));

        parent.setNode(null);
        assertNull(DOMAttributeIterator.getAttribute(null, name));

        NameSpace ns = new NameSpace("xmlns", "http://example.com/test");
        parent.setNamespaceContext(ns);
        Attr attr3 = new Attr("test", "test");
        Element element3 = new Element("test");
        parent.setNode(element3);
        element3.setAttributeNode(attr3);
        assertEquals(attr3, DOMAttributeIterator.getAttribute(element3, name));

        parent.setNamespaceContext(null);
        assertNull(DOMAttributeIterator.getAttribute(element3, name));
    }
}