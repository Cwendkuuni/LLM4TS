package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertEquals(0, xmlElement.count());

        xmlElement = new XmlElement("root");
        assertEquals("root", xmlElement.getName());
        assertEquals(0, xmlElement.count());

        xmlElement = new XmlElement("root", null);
        assertEquals("root", xmlElement.getName());
        assertEquals(0, xmlElement.count());

        xmlElement = new XmlElement("root", "root data");
        assertEquals("root", xmlElement.getName());
        assertEquals("root data", xmlElement.getData());
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "test1");
        assertEquals("test1", xmlElement.getAttribute("test"));

        xmlElement.addAttribute("test2", "test2");
        assertEquals("test1", xmlElement.getAttribute("test"));
        assertEquals("test2", xmlElement.getAttribute("test2"));

        xmlElement.addAttribute("test", "test3");
        assertEquals("test3", xmlElement.getAttribute("test"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test", "test1");
        assertEquals("test1", xmlElement.getAttribute("test"));
        assertEquals(null, xmlElement.getAttribute("test2"));
        assertEquals("", xmlElement.getAttribute("test3"));

        xmlElement.addAttribute("test2", "test2");
        assertEquals("test1", xmlElement.getAttribute("test"));
        assertEquals("test2", xmlElement.getAttribute("test2"));
        assertEquals(null, xmlElement.getAttribute("test3"));

        xmlElement.addAttribute("test3", "test3");
        assertEquals("test1", xmlElement.getAttribute("test"));
        assertEquals("test2", xmlElement.getAttribute("test2"));
        assertEquals("test3", xmlElement.getAttribute("test3"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test1", "test1");
        xmlElement.addAttribute("test2", "test2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("test1", attributes.get("test1"));
        assertEquals("test2", attributes.get("test2"));
        assertEquals(0, attributes.size() - 2);
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("test1", "test1");
        attributes.put("test2", "test2");
        xmlElement.setAttributes(attributes);

        assertEquals("test1", xmlElement.getAttribute("test1"));
        assertEquals("test2", xmlElement.getAttribute("test2"));
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("test1", "test1");
        xmlElement.addAttribute("test2", "test2");
        Enumeration enumeration = xmlElement.getAttributeNames();
        assertEquals("test1", enumeration.nextElement());
        assertEquals("test2", enumeration.nextElement());
        assertEquals(0, enumeration.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(2, parent.count());
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);

        parent.removeElement(child1);
        assertEquals(1, parent.count());
        assertNull(parent.getElement(0));
        assertEquals(child2, parent.getElement(0));

        parent.removeElement(child2);
        assertEquals(0, parent.count());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);

        parent.removeAllElements();
        assertEquals(0, parent.count());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2/child3");
        XmlElement child3 = parent.addSubElement("child4");

        assertEquals(3, parent.count());
        assertEquals("child1", child1.getName());
        assertEquals("child2/child3", child2.getName());
        assertEquals("child4", child3.getName());
    }

    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2/child3");
        XmlElement child3 = parent.addSubElement("child4");

        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2/child3"));
        assertEquals(child3, parent.getElement("child4"));
        assertNull(parent.getElement("child5"));
    }

    @Test
    public void testClone() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child2/child3");
        XmlElement child3 = parent.addSubElement("child4");

        XmlElement clonedParent = (XmlElement) parent.clone();

        assertEquals("parent", clonedParent.getName());
        assertEquals(3, clonedParent.count());

        XmlElement clonedChild1 = (XmlElement) clonedParent.getElements().get(0);
        assertEquals("child1", clonedChild1.getName());

        XmlElement clonedChild2 = (XmlElement) clonedParent.getElements().get(1);
        assertEquals("child2/child3", clonedChild2.getName());

        XmlElement clonedChild3 = (XmlElement) clonedParent.getElements().get(2);
        assertEquals("child4", clonedChild3.getName());
    }
}