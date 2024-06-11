package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DOMParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private static Document createDocument(String xml) throws Exception {
        DOMParser parser = new DOMParser();
        parser.parseFromString(xml, null);
        return parser.getDocument();
    }

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new DOMPointer("", "", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttribute() {
        String xml = "<root attr='value'/>";
        Document document = createDocument(xml);
        NodePointer parent = new DOMPointer("/", "root", document);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "attr"));
        assertTrue(iterator.hasNext());
        NodePointer attributePointer = iterator.next();
        assertEquals("value", attributePointer.getStringValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributes() {
        String xml = "<root attr1='value1' attr2='value2'/>";
        Document document = createDocument(xml);
        NodePointer parent = new DOMPointer("/", "root", document);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<NodePointer> attributes = new ArrayList<>();
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.next();
            attributes.add(attributePointer);
        }
        assertEquals(2, attributes.size());
        NodePointer firstAttribute = attributes.get(0);
        assertEquals("attr1", firstAttribute.getNodeName().getLocalName());
        assertEquals("value1", firstAttribute.getStringValue());
        NodePointer secondAttribute = attributes.get(1);
        assertEquals("attr2", secondAttribute.getNodeName().getLocalName());
        assertEquals("value2", secondAttribute.getStringValue());
    }

    @Test
    public void testPrefixNamespaces() {
        String xml = "<root xmlns:x='http://example.com/x' xmlns:y='http://example.com/y' x:foo='value1' y:bar='value2'/>";
        Document document = createDocument(xml);
        NodePointer parent = new DOMPointer("/", "root", document);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x", "foo"));
        assertTrue(iterator.hasNext());
        NodePointer attributePointer = iterator.next();
        assertEquals("value1", attributePointer.getStringValue());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(parent, new QName("y", "bar"));
        assertTrue(iterator.hasNext());
        attributePointer = iterator.next();
        assertEquals("value2", attributePointer.getStringValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyElement() {
        String xml = "<root/>";
        Document document = createDocument(xml);
        NodePointer parent = new DOMPointer("/", "root", document);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testElementWithNoAttributes() {
        String xml = "<root/>";
        Document document = createDocument(xml);
        NodePointer parent = new DOMPointer("/", "root", document);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "nonExistentAttribute"));
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer("", "", null), null);
    }
}