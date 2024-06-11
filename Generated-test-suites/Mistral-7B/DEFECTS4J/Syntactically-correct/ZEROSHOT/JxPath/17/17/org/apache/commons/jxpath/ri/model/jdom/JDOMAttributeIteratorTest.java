package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JUnit test file for JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyIterator() {
        // Test empty iterator
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testSingleAttributeIterator() {
        // Test single attribute iterator
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "lang";
        String value = "en";

        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration(xmlns, prefix);
        rootElement.addAttribute(new QName(xmlns + ":lang"), value);

        NodePointer parent = new NodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), rootElement.getAttribute(localName));
        assertEquals(next.getPosition(), 1);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testMultipleAttributesIterator() {
        // Test multiple attributes iterator
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";

        String attr1LocalName = "lang";
        String attr1Value = "en";

        String attr2LocalName = "xml:id";
        String attr2Value = "test";

        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration(xmlns, prefix);
        rootElement.addAttribute(new QName(xmlns + ":lang"), attr1Value);
        rootElement.addAttribute(new QName(xmlns + ":*", attr2LocalName), attr2Value);

        NodePointer parent = new NodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), rootElement.getAttribute("xml:lang"));
        assertEquals(next.getPosition(), 1);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(next.getNode(), rootElement.getAttribute("xml:id"));
        assertEquals(next.getPosition(), 2);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next);
        assertEquals(iterator.getPosition(), 3);

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNamespaceIterator() {
        // Test namespace iterator
        String prefix = "xml";
        String localName = "lang";
        String uri = "http://www.w3.org/XML/1998/namespace";

        Element rootElement = new Element("root");
        Element childElement = new Element("child");
        rootElement.addContent(childElement);
        rootElement.addNamespaceDeclaration(new Namespace(uri, prefix));
        childElement.addAttribute(new QName(new QName(uri, localName)), "en");

        NodePointer parent = new NodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName(null, localName));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), childElement.getAttribute("lang"));
        assertEquals(next.getPosition(), 1);

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testIteratorWithNoMatchingAttributes() {
        // Test iterator with no matching attributes
        String prefix = "xml";
        String localName = "lang";

        Element rootElement = new Element("root");
        NodePointer parent = new NodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testIteratorWithMixedNamespaces() {
        // Test iterator with mixed namespaces
        String xmlns1 = "http://www.w3.org/2000/xmlns/";
        String prefix1 = "xml1";
        String localName1 = "lang1";
        String value1 = "en";

        String xmlns2 = "http://www.w3.org/2001/XMLSchema";
        String prefix2 = "xml2";
        String localName2 = "id";
        String value2 = "test";

        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration(xmlns1, prefix1);
        rootElement.addNamespaceDeclaration(xmlns2, prefix2);
        rootElement.addAttribute(new QName(xmlns1 + ":lang1"), value1);
        rootElement.addAttribute(new QName(xmlns2 + ":id"), value2);

        NodePointer parent = new NodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), rootElement.getAttribute("xml:lang1"));
        assertEquals(next.getPosition(), 1);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(next.getNode(), rootElement.getAttribute("xml2:id"));
        assertEquals(next.getPosition(), 2);

        assertFalse(iterator.hasNext());
    }
}