package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement);

        xmlElement = new XmlElement("root");
        xmlElement.setData("data");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("root", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertNull(xmlElement.getParent());
        assertFalse(xmlElement.hasSubElements());
    }

    @Test
    public void testAddAttributeMethod() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");

        assertEquals(2, xmlElement.getAttributes().size());
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals("value2", xmlElement.getAttribute("attr2"));
    }

    @Test
    public void testGetAttributeMethod() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");

        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals(null, xmlElement.getAttribute("attr2"));
    }

    @Test
    public void testGetAttributesMethod() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");

        Map<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }

    @Test
    public void testSetAttributesMethod() {
        XmlElement xmlElement = new XmlElement();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr1", "value1");
        xmlElement.setAttributes(attributes);

        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeNamesMethod() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");

        Iterator<String> iterator = xmlElement.getAttributeNames().asIterator();
        assertEquals("attr1", iterator.next());
        assertEquals("attr2", iterator.next());
    }

    @Test
    public void testAddElementMethod() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);

        assertTrue(parent.hasSubElements());
        assertEquals(child, parent.getSubElement(0));
        assertEquals(child, child.getParent());
    }

    @Test
    public void testRemoveElementMethod() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);

        parent.removeElement(child);

        assertFalse(parent.hasSubElements());
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveFromParentMethod() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);

        child.removeFromParent();

        assertFalse(parent.hasSubElements());
        assertNull(child.getParent());
    }

    @Test
    public void testGetElementMethod() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertNull(parent.getElement("nonExistentChild"));
    }

    @Test
    public void testGetElementMethodWithPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        parent.addElement(child1);
        parent.addElement(child2);
        child1.addElement(child3);

        assertEquals(child3, parent.getElement("parent/child1/child3"));
        assertNull(parent.getElement("nonExistentElement"));
    }

    @Test
    public void testAddSubElementMethod() {
        XmlElement parent = new XmlElement();

        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2/child2.1");
        XmlElement child3 = parent.addSubElement("child2/child2.2");

        assertEquals("child1", child1.getName());
        assertEquals("child2.1", child2.getName());
        assertEquals("child2.2", child3.getName());
        assertEquals(parent, child1.getParent());
        assertEquals(child2, child3.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithNullName() {
        XmlElement parent = new XmlElement();
        parent.addSubElement(null);
    }

    @Test
    public void testCloneMethod() {
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "value1");
        original.addAttribute("attr2", "value2");
        original.addElement("child1");

        XmlElement cloned = (XmlElement) original.clone();

        assertEquals(original, cloned);
        assertNotSame(original, cloned);
        assertNotNull(cloned.getAttributes());
        assertEquals(original.getAttributes().size(), cloned.getAttributes().size());
        assertNotNull(cloned.getSubElements());
        assertEquals(original.getSubElements().size(), cloned.getSubElements().size());
    }

    @Test
    public void testSetNameMethod() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("name1");
        xmlElement.setName("name2");

        assertEquals("name2", xmlElement.getName());
    }
}