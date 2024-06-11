package com.allenstudio.ir.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testXmlElementInitialization() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getName());
        assertEquals("", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.hasSubElements());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("elementName");
        assertEquals("elementName", xmlElement.getName());
        assertEquals("", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.hasSubElements());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("elementName", null);
        assertEquals("elementName", xmlElement.getName());
        assertEquals("", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.hasSubElements());
        assertNull(xmlElement.getParent());

        xmlElement = new XmlElement("elementName", "elementData");
        assertEquals("elementName", xmlElement.getName());
        assertEquals("elementData", xmlElement.getData());
        assertNotNull(xmlElement.getAttributes());
        assertFalse(xmlElement.hasSubElements());
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName", "attributeValue");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName", "defaultValue"));
        assertNotNull(xmlElement.getAttributes());
        assertEquals("attributeName=attributeValue", xmlElement.getAttributes().toString());

        xmlElement.addAttribute("attributeName2", "attributeValue2");
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2", "defaultValue"));
        assertNotNull(xmlElement.getAttributes());
        assertEquals("attributeName=attributeValue\nattributeName2=attributeValue2", xmlElement.getAttributes().toString());
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName", "attributeValue");

        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName", "defaultValue"));
        assertNull(xmlElement.getAttribute("nonExistentAttribute"));

        xmlElement.addAttribute("attributeName2", "attributeValue2");
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName", "defaultValue"));
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2"));
        assertEquals("attributeValue2", xmlElement.getAttribute("attributeName2", "defaultValue"));
        assertNull(xmlElement.getAttribute("nonExistentAttribute2"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attributeName", "attributeValue");
        xmlElement.setAttributes(attributes);

        assertEquals("attributeValue", xmlElement.getAttribute("attributeName"));
        assertEquals("attributeValue", xmlElement.getAttribute("attributeName", "defaultValue"));
        assertNotNull(xmlElement.getAttributes());
        assertEquals("attributeName=attributeValue", xmlElement.getAttributes().toString());

        xmlElement.setAttributes(null);
        assertNull(xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attributeName1", "attributeValue1");
        xmlElement.addAttribute("attributeName2", "attributeValue2");

        List<String> attributeNames = new ArrayList<>();
        while (xmlElement.hasMoreAttributeNames()) {
            attributeNames.add(xmlElement.getAttributeName());
        }

        assertEquals(2, attributeNames.size());
        assertTrue(attributeNames.contains("attributeName1"));
        assertTrue(attributeNames.contains("attributeName2"));
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("childElementName");

        parentElement.addElement(childElement);
        assertEquals(1, parentElement.count());
        assertTrue(parentElement.hasSubElements());
        assertEquals(childElement, parentElement.getElement(0));

        XmlElement anotherChildElement = new XmlElement("anotherChildElementName");
        parentElement.addElement(anotherChildElement);
        assertEquals(2, parentElement.count());
        assertTrue(parentElement.hasSubElements());
        assertTrue(parentElement.getElements().containsAll(Arrays.asList(childElement, anotherChildElement)));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        XmlElement childElement2 = new XmlElement("childElementName2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        assertTrue(parentElement.hasSubElements());

        parentElement.removeElement(childElement1);
        assertEquals(1, parentElement.count());
        assertFalse(parentElement.hasSubElements());
        assertNull(parentElement.getElement(0));

        parentElement.addElement(childElement1);
        parentElement.removeElement(childElement2);
        assertEquals(1, parentElement.count());
        assertFalse(parentElement.hasSubElements());
        assertEquals(childElement1, parentElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        XmlElement childElement2 = new XmlElement("childElementName2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        assertTrue(parentElement.hasSubElements());

        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
        assertFalse(parentElement.hasSubElements());
    }

    @Test
    public void testClone() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("childElementName1");
        XmlElement childElement2 = new XmlElement("childElementName2");

        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
        assertTrue(parentElement.hasSubElements());

        XmlElement clonedParentElement = (XmlElement) parentElement.clone();
        assertEquals(2, clonedParentElement.count());
        assertTrue(clonedParentElement.hasSubElements());
        assertTrue(clonedParentElement.getElements().containsAll(Arrays.asList(childElement1, childElement2)));

        for (XmlElement childElement : clonedParentElement.getElements()) {
            assertEquals(parentElement, childElement.getParent());
        }

        for (XmlElement childElement : parentElement.getElements()) {
            assertNull(childElement.getParent());
        }
    }
}