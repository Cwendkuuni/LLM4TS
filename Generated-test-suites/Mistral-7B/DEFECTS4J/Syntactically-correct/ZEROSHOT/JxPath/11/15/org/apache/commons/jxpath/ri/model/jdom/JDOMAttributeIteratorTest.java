package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testJDOMAttributeIteratorEmpty() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorSingleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "lang";
        String value = "en";

        Element root = new Element("root");
        root.setNamespace(xmlns, prefix);
        root.addAttribute(new Attribute(localName, value, xmlns));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertTrue(iterator.hasNext());
        Attribute attribute = iterator.next();
        assertSame(attribute, root.getAttribute(localName, xmlns));
        assertEquals(value, attribute.getValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorMultipleAttributes() {
        String namespaceUri = "http://www.example.com/";
        String prefix = "ex";
        String localName1 = "attr1";
        String value1 = "value1";
        String localName2 = "attr2";
        String value2 = "value2";

        Element root = new Element("root");
        root.setNamespace(new Namespace(namespaceUri, prefix));
        root.addAttribute(new Attribute(localName1, value1, new Namespace(namespaceUri, prefix)));
        root.addAttribute(new Attribute(localName2, value2, new Namespace(namespaceUri, prefix)));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));

        List<Attribute> expectedAttributes = new ArrayList<>();
        expectedAttributes.add(root.getAttribute(localName1, namespaceUri));
        expectedAttributes.add(root.getAttribute(localName2, namespaceUri));

        for (int i = 0; i < expectedAttributes.size(); i++) {
            Attribute attribute = iterator.next();
            assertSame(attribute, expectedAttributes.get(i));
        }

        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorMixedNamespaces() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String exPrefix = "ex";
        String localName1 = "lang";
        String value1 = "en";
        String exNamespaceUri = "http://www.example.com/";
        String exPrefix2 = "ex2";
        String localName2 = "color";
        String value2 = "red";

        Element root = new Element("root");
        root.setNamespace(xmlns, exPrefix);
        root.addAttribute(new Attribute(localName1, value1, xmlns));
        root.addNamespaceDeclaration(new Namespace(exNamespaceUri, exPrefix2));
        root.addAttribute(new Attribute(localName2, value2, new Namespace(exNamespaceUri, exPrefix2)));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", localName1));

        assertTrue(iterator.hasNext());
        Attribute attribute1 = iterator.next();
        assertSame(attribute1, root.getAttribute(localName1, xmlns));
        assertEquals(value1, attribute1.getValue());
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName(exPrefix2, localName2));
        assertTrue(iterator.hasNext());
        Attribute attribute2 = iterator.next();
        assertSame(attribute2, root.getAttribute(localName2, new Namespace(exNamespaceUri, exPrefix2)));
        assertEquals(value2, attribute2.getValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorNullParent() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorNullName() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testJDOMAttributeIteratorEmptyElement() {
        Element emptyElement = new Element("root");
        NodePointer parent = new JDOMPointer(emptyElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}