import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testDOMAttributeIterator_empty() {
        NodePointer parent = new DOMPointNodePointer(null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_singleAttribute() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test:attribute", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute = attributePointer.getAttribute();
        assertEquals("attribute", attribute.getNodeName());
        assertEquals("http://test.com", attribute.getNamespaceURI());
        assertEquals("test:attribute", attribute.getLocalName());
        assertEquals("testValue", attribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_multipleAttributes() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute1 = attributePointer1.getAttribute();
        assertEquals("attribute1", attribute1.getNodeName());
        assertEquals("http://test.com", attribute1.getNamespaceURI());
        assertEquals("test:attribute1", attribute1.getLocalName());
        assertEquals("testValue1", attribute1.getValue());

        iterator.next();
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute2 = attributePointer2.getAttribute();
        assertEquals("attribute2", attribute2.getNodeName());
        assertEquals("http://test.com", attribute2.getNamespaceURI());
        assertEquals("test:attribute2", attribute2.getLocalName());
        assertEquals("testValue2", attribute2.getValue());

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_namespaceScope() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test:attribute", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute = attributePointer.getAttribute();
        assertEquals("attribute", attribute.getNodeName());
        assertEquals("http://test.com", attribute.getNamespaceURI());
        assertEquals("test:attribute", attribute.getLocalName());
        assertEquals("testValue", attribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_namespaceScope_multipleAttributes() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute1 = attributePointer1.getAttribute();
        assertEquals("attribute1", attribute1.getNodeName());
        assertEquals("http://test.com", attribute1.getNamespaceURI());
        assertEquals("test:attribute1", attribute1.getLocalName());
        assertEquals("testValue1", attribute1.getValue());

        iterator.next();
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute2 = attributePointer2.getAttribute();
        assertEquals("attribute2", attribute2.getNodeName());
        assertEquals("http://test.com", attribute2.getNamespaceURI());
        assertEquals("test:attribute2", attribute2.getLocalName());
        assertEquals("testValue2", attribute2.getValue());

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_emptyElement() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_mixedNamespace_noMatch() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test:noMatch", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_mixedNamespace_match() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test:match", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute = attributePointer.getAttribute();
        assertEquals("attribute", attribute.getNodeName());
        assertEquals("http://test.com", attribute.getNamespaceURI());
        assertEquals("test:attribute", attribute.getLocalName());
        assertEquals("testValue", attribute.getValue());

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_mixedNamespace_match_multipleAttributes() {
        Element element = new MockElement();
        NodePointer parent = new DOMPointNodePointer(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "http://test.com"));

        assertFalse(iterator.hasNext());

        iterator.next();
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute1 = attributePointer1.getAttribute();
        assertEquals("attribute1", attribute1.getNodeName());
        assertEquals("http://test.com", attribute1.getNamespaceURI());
        assertEquals("test:attribute1", attribute1.getLocalName());
        assertEquals("testValue1", attribute1.getValue());

        iterator.next();
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
        Attr attribute2 = attributePointer2.getAttribute();
        assertEquals("attribute2", attribute2.getNodeName());
        assertEquals("http://test.com", attribute2.getNamespaceURI());
        assertEquals("test:attribute2", attribute2.getLocalName());
        assertEquals("testValue2", attribute2.getValue());

        assertFalse(iterator.hasNext());
    }
}

class MockElement extends Element {
    @Override
    public String getTagName() {
        return "mock";
    }

    @Override
    public NamedNodeMap getAttributes() {
        Attr attr1 = new Attr();
        attr1.setNodeName(new QName("attribute1", "http://test.com"));
        attr1.setValue("testValue1");
        attr1.setNamespaceURI("http://test.com");
        Attr attr2 = new Attr();
        attr2.setNodeName(new QName("attribute2", "http://test.com"));
        attr2.setValue("testValue2");
        attr2.setNamespaceURI("http://test.com");

        NamedNodeMap attrs = super.getAttributes();
        attrs.setNamedItem(attr1);
        attrs.setNamedItem(attr2);
        return attrs;
    }
}