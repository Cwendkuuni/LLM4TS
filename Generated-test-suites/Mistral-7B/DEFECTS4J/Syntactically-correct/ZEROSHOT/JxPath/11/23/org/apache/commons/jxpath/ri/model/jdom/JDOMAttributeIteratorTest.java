package org.apache.commons.jxpath.ri.model.jdom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;

/**
 * Test class for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

    /**
     * Test constructor.
     * <p>
     * Test case for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)}.
     */
    @org.junit.Test
    public void testConstructor() {
        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root"), null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertSame(parent, iterator.getParent());
        assertNull(iterator.attributes);
    }

    /**
     * Test constructor with no attributes.
     * <p>
     * Test case for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)}.
     */
    @org.junit.Test
    public void testConstructorNoAttributes() {
        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root"), null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertSame(parent, iterator.getParent());
        assertNull(iterator.attributes);
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor with attributes.
     * <p>
     * Test case for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)}.
     */
    @org.junit.Test
    public void testConstructorWithAttributes() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";

        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root", "rootElement", new Namespace("xml", xmlNS)), null);

        Element rootElement = (Element) parent.getNode();
        rootElement.addAttribute("xml:id", "rootID", xmlNS);
        rootElement.addAttribute("xml:version", "1.0", xmlNS);
        rootElement.addAttribute("xsi:type", "xs:Element", xsiNS);
        rootElement.addAttribute("foo:attr1", null, null);
        rootElement.addAttribute("foo:attr2", "value1", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertSame(parent, iterator.getParent());
        List attributes = iterator.attributes;
        assertEquals(4, attributes.size());
        assertTrue(iterator.hasNext());
        assertEquals("xml:id", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
    }

    /**
     * Test getPosition.
     * <p>
     * Test case for {@link JDOMAttributeIterator#getPosition()}.
     */
    @org.junit.Test
    public void testGetPosition() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";

        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root", "rootElement", new Namespace("xml", xmlNS)), null);

        Element rootElement = (Element) parent.getNode();
        rootElement.addAttribute("xml:id", "rootID", xmlNS);
        rootElement.addAttribute("xml:version", "1.0", xmlNS);
        rootElement.addAttribute("xsi:type", "xs:Element", xsiNS);
        rootElement.addAttribute("foo:attr1", null, null);
        rootElement.addAttribute("foo:attr2", "value1", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertEquals("xml:id", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
        assertEquals(1, iterator.getPosition());
    }

    /**
     * Test setPosition.
     * <p>
     * Test case for {@link JDOMAttributeIterator#setPosition(int)}.
     */
    @org.junit.Test
    public void testSetPosition() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";

        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root", "rootElement", new Namespace("xml", xmlNS)), null);

        Element rootElement = (Element) parent.getNode();
        rootElement.addAttribute("xml:id", "rootID", xmlNS);
        rootElement.addAttribute("xml:version", "1.0", xmlNS);
        rootElement.addAttribute("xsi:type", "xs:Element", xsiNS);
        rootElement.addAttribute("foo:attr1", null, null);
        rootElement.addAttribute("foo:attr2", "value1", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        assertEquals("xml:version", ((Attribute) iterator.next().getNode()).getName().getLocalPart());

        iterator.setPosition(6);
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getNodePointer.
     * <p>
     * Test case for {@link JDOMAttributeIterator#getNodePointer()}.
     */
    @org.junit.Test
    public void testgetNodePointer() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";

        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root", "rootElement", new Namespace("xml", xmlNS)), null);

        Element rootElement = (Element) parent.getNode();
        rootElement.addAttribute("xml:id", "rootID", xmlNS);
        rootElement.addAttribute("xml:version", "1.0", xmlNS);
        rootElement.addAttribute("xsi:type", "xs:Element", xsiNS);
        rootElement.addAttribute("foo:attr1", null, null);
        rootElement.addAttribute("foo:attr2", "value1", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Attribute attribute = (Attribute) iterator.next().getNode();
        JDOMAttributePointer attributePointer = iterator.getNodePointer();

        NodePointer nodePointer = attributePointer.getNodePointer();
        assertEquals("rootElement", nodePointer.getNode().getName());
        assertEquals("root", nodePointer.getPath().get(0).getNode().getName());
        assertSame(parent, nodePointer.getParent());
        assertEquals(attribute, nodePointer.getNode());
    }

    /**
     * Test hasNext.
     * <p>
     * Test case for {@link JDOMAttributeIterator#hasNext()}.
     */
    @org.junit.Test
    public void testHasNext() {
        String xmlNS = "http://www.w3.org/XML/1998/namespace";
        String xsiNS = "http://www.w3.org/2001/XMLSchema-instance";

        QName name = new QName("foo:bar", "http://example.com/ns");
        NodePointer parent = new NodePointer(new Element("root", "rootElement", new Namespace("xml", xmlNS)), null);

        Element rootElement = (Element) parent.getNode();
        rootElement.addAttribute("xml:id", "rootID", xmlNS);
        rootElement.addAttribute("xml:version", "1.0", xmlNS);
        rootElement.addAttribute("xsi:type", "xs:Element", xsiNS);
        rootElement.addAttribute("foo:attr1", null, null);
        rootElement.addAttribute("foo:attr2", "value1", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        assertEquals("xml:id", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
        assertTrue(iterator.hasNext());
        assertEquals("xml:version", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
        assertTrue(iterator.hasNext());
        assertEquals("xsi:type", ((Attribute) iterator.next().getNode()).getName().getLocalPart());
        assertFalse(iterator.hasNext());
    }
}