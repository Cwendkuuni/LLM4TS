package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementConstruction() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(0, xmlElement.count());
        xmlElement = new XmlElement("root");
        assertEquals("root", xmlElement.getName());
        xmlElement = new XmlElement("root", new Hashtable<String, String>());
        assertEquals("root", xmlElement.getName());
        assertEquals(0, xmlElement.count());
        xmlElement = new XmlElement("root", "");
        assertEquals("root", xmlElement.getName());
        assertEquals("", xmlElement.getData());
        xmlElement = new XmlElement("root", "data");
        assertEquals("root", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        xmlElement.addAttribute("attr2", null);
        assertNull(xmlElement.getAttribute("attr2"));
        xmlElement.addAttribute("attr1", "value2");
        assertEquals("value2", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        xmlElement.addAttribute("attr2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attr2", "defaultValue"));
        assertNull(xmlElement.getAttribute("attr3"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
        assertEquals(2, attributes.size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("root");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        assertEquals("attr1", attributeNames.nextElement());
        assertEquals("attr2", attributeNames.nextElement());
        assertFalse(attributeNames.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        parent.removeElement(child);
        assertEquals(0, parent.count());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertNull(parent.getElement("nonExistentChild"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSubElementWithNullPath() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addSubElement(null);
    }

    @Test
    public void testAddSubElementWithValidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        XmlElement grandChild = parent.addSubElement("child1/child3");
        assertEquals("child3", grandChild.getName());
        assertEquals(parent, grandChild.getParent());
    }

    @Test
    public void testAddSubElementWithExistingElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        XmlElement existingChild = new XmlElement("child1");
        parent.addSubElement("child1/existingChild", existingChild);
        assertEquals(existingChild, parent.getElement("child1/existingChild"));
    }

    @Test
    public void testAddSubElementWithExistingPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        assertThrows(IllegalArgumentException.class, () -> parent.addSubElement("child1", new XmlElement("child3")));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        xmlElement.addElement(new XmlElement("child1"));
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertEquals("root", clonedElement.getName());
        assertEquals("value1", clonedElement.getAttribute("attr1"));
        assertEquals("value2", clonedElement.getAttribute("attr2"));
        assertEquals(1, clonedElement.count());
        assertEquals(xmlElement.getElement(0), clonedElement.getElements().get(0));
    }

    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        String expectedOutput = "root:\n" +
                "attr1:value1\n" +
                "child1:\n";
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        XmlElement.printNode(xmlElement, "");
        String actualOutput = System.out.getBuffer().getCapacity() > 0 ? System.out.getBuffer().subSequence(0, System.out.getBuffer().position()).toString() : "";
        assertEquals(expectedOutput, actualOutput);
    }
}