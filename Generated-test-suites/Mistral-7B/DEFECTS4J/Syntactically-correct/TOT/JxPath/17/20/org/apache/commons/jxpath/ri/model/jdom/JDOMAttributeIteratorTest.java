package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JDOMAttributeIteratorTest {

    private NodePointer nodePointer;
    private JDOMAttributeIterator iterator;
    private Element testElement;

    @Before
    public void setUp() {
        testElement = new Element("root");
        testElement.addAttribute(new Attribute("attr1", "value1"));
        testElement.addAttribute(new Attribute("attr2", "value2", "ns1:Prefix1"));
        testElement.addAttribute(new Attribute("attr3", "value3", "ns2:Prefix2"));
        nodePointer = new NodePointer(testElement);
    }

    @Test
    public void testEmptyIterator() {
        iterator = new JDOMAttributeIterator(nodePointer, null);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        assertEquals(1, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value1", attribute.getValue());
        assertEquals("attr1", attribute.getName());
        assertEquals("", attribute.getNamespaceURI());
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithNamespace() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("ns1:Prefix1:attr2"));
        assertEquals(1, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value2", attribute.getValue());
        assertEquals("attr2", attribute.getName());
        assertEquals("ns1:Prefix1", attribute.getNamespace().getQualifiedName().getLocalPart());
        assertEquals("ns1:Prefix1", attribute.getNamespaceURI());
        assertEquals(2, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithWildcardName() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        assertEquals(3, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        assertEquals("value2", attribute.getValue());
        assertEquals("ns1:Prefix1:attr2", attribute.getQualifiedName().toString());
        assertEquals("ns1:Prefix1", attribute.getNamespaceURI());
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithWrongNamePrefix() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("wrong:prefix:attr1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorWithWrongPosition() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        iterator.setPosition(5);
    }
}