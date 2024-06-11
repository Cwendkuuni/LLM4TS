package org.apache.commons.jxpath.ri.model.jdom.test;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest extends TestCase {

    /**
     * Test constructor with attribute name and namespace.
     */
    public void testConstructorWithNameAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));
        assertEquals("attr1", iterator.getNodePointer().getName().getName());

        element.addAttribute("attr2", new QName("prefix", "attr2"));
        iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr2"));
        assertEquals("attr2", iterator.getNodePointer().getName().getName());

        // Test empty case
        parent = new JDOMNodePointer(new Element("root"));
        iterator = new JDOMAttributeIterator(parent, new QName("prefix", " attr3"));
        assertNull(iterator.getNodePointer());
    }

    /**
     * Test constructor with attribute name and no namespace.
     */
    public void testConstructorWithNameAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals("attr1", iterator.getNodePointer().getName().getName());

        element.addAttribute("attr2", "value2");
        iterator = new JDOMAttributeIterator(parent, new QName("attr2"));
        assertEquals("attr2", iterator.getNodePointer().getName().getName());

        // Test empty case
        parent = new JDOMNodePointer(new Element("root"));
        iterator = new JDOMAttributeIterator(parent, new QName(" attr3"));
        assertNull(iterator.getNodePointer());
    }

    /**
     * Test constructor with wildcard and namespace.
     */
    public void testConstructorWithWildcardAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "prefix:attr"));
        Attribute attribute = null;
        for (int i = 0; i < 2; i++) {
            NodePointer nodePointer = iterator.next();
            if (nodePointer != null) {
                attribute = (Attribute) nodePointer.getNode();
                break;
            }
        }
        assertEquals("attr1", attribute.getName());
    }

    /**
     * Test constructor with wildcard and no namespace.
     */
    public void testConstructorWithWildcardAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "*"));
        Attribute attribute = null;
        for (int i = 0; i < 2; i++) {
            NodePointer nodePointer = iterator.next();
            if (nodePointer != null) {
                attribute = (Attribute) nodePointer.getNode();
                break;
            }
        }
        assertEquals("attr1", attribute.getName());
    }

    /**
     * Test next() method with attribute name and namespace.
     */
    public void testNextWithNameAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));
        Attribute firstAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr1", firstAttribute.getName());

        Attribute secondAttribute = (Attribute) iterator.next().getNode();
        assertNull(secondAttribute);
    }

    /**
     * Test next() method with attribute name and no namespace.
     */
    public void testNextWithNameAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        Attribute firstAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr1", firstAttribute.getName());

        Attribute secondAttribute = (Attribute) iterator.next().getNode();
        assertNull(secondAttribute);
    }

    /**
     * Test next() method with wildcard and namespace.
     */
    public void testNextWithWildcardAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        element.addAttribute("attr2", new QName("prefix", "attr2"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "prefix:attr"));
        Attribute firstAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr1", firstAttribute.getName());

        Attribute secondAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr2", secondAttribute.getName());

        Attribute thirdAttribute = (Attribute) iterator.next().getNode();
        assertNull(thirdAttribute);
    }

    /**
     * Test next() method with wildcard and no namespace.
     */
    public void testNextWithWildcardAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "*"));
        Attribute firstAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr1", firstAttribute.getName());

        Attribute secondAttribute = (Attribute) iterator.next().getNode();
        assertEquals("attr2", secondAttribute.getName());

        Attribute thirdAttribute = (Attribute) iterator.next().getNode();
        assertNull(thirdAttribute);
    }

    /**
     * Test hasNext() method with attribute name and namespace.
     */
    public void testHasNextWithNameAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr2"));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test hasNext() method with attribute name and no namespace.
     */
    public void testHasNextWithNameAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName("attr2"));
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test hasNext() method with wildcard and namespace.
     */
    public void testHasNextWithWildcardAndNamespace() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        element.addAttribute("attr2", new QName("prefix", "attr2"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "prefix:attr"));
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test hasNext() method with wildcard and no namespace.
     */
    public void testHasNextWithWildcardAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "*"));
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test setPosition() method.
     */
    public void testSetPosition() {
        Element element = new Element("root", "prefix");
        element.addAttribute("attr1", new QName("prefix", "attr1"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr1"));

        iterator.setPosition(1);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.setPosition(0);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName("prefix", "attr2"));

        iterator.setPosition(1);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.setPosition(0);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }
}